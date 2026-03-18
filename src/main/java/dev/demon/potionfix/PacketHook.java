package dev.demon.potionfix;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import dev.demon.potionfix.user.PlayerData;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PacketHook extends PacketListenerAbstract {
    public PacketHook() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = event.getPlayer();
            PlayerData data = PotionFix.getInstance().getUserManager().getUser(player);

            if (data != null) {
                if (event.getPacketType() == PacketType.Play.Client.CREATIVE_INVENTORY_ACTION) {
                    WrapperPlayClientCreativeInventoryAction creativePacket =
                            new WrapperPlayClientCreativeInventoryAction(event);
                    ItemStack item = creativePacket.getItemStack();
                    String nbt = "CustomPotionEffects";

                    // So via version I believe messed up and forgot to scrub the NBT data from it, making it so
                    // older server versions couldn't handle the nbt making the potion not activate.
                    if (item != null && isPotion(item.getType())) {
                        if (item.getNBT() != null && item.getNBT().contains(nbt)) {
                            item.getNBT().removeTag(nbt);
                            creativePacket.setItemStack(item);
                            event.markForReEncode(true);
                        }
                    }
                }

                if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
                    WrapperPlayClientPlayerBlockPlacement blockPlace = new WrapperPlayClientPlayerBlockPlacement(event);
                    if (blockPlace.getItemStack().isPresent()) {
                        ItemStack item = blockPlace.getItemStack().get();
                        String nbt = "CustomPotionEffects";
                        if (isPotion(item.getType()) && item.getNBT() != null && item.getNBT().contains(nbt)) {
                            // if they some-how the player still gets access to a potion with the NBT data
                            // on use we will just re-encode the item so there is no issues
                            item.getNBT().removeTag(nbt);
                            blockPlace.setItemStack(Optional.of(item));
                            event.markForReEncode(true);
                        }
                    }
                }
            }
        }
    }

    private boolean isPotion(ItemType itemType) {
        return itemType == ItemTypes.POTION
                || itemType == ItemTypes.LINGERING_POTION
                || itemType == ItemTypes.SPLASH_POTION;
    }
}