package dev.demon.potionfix.user;

import com.github.retrooper.packetevents.protocol.player.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {
    private UUID uuid;
    private Player player;
    private User user;

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = this.player.getUniqueId();
    }
}
