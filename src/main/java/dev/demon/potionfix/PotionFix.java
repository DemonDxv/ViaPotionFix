package dev.demon.potionfix;

import com.github.retrooper.packetevents.PacketEvents;
import dev.demon.potionfix.user.UserManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PotionFix extends JavaPlugin implements Listener {

    @Getter
    private static PotionFix instance;
    private final UserManager userManager = new UserManager();

    @Override
    public void onLoad() {
        instance = this;
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(new PacketHook());
        PacketEvents.getAPI().init();

        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("ViaPotionFix is now enabled!");
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.userManager.addUser(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.userManager.removeUser(event.getPlayer());
    }
}
