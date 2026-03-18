package dev.demon.potionfix.user;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.demon.potionfix.PotionFix;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class UserManager {
    private final Map<UUID, PlayerData> userMap = new ConcurrentHashMap<>();

    public void addUser(Player player) {
        // 1.20.4 and older do not have this issue.
        if (PacketEvents.getAPI().getPlayerManager().getUser(player).getClientVersion()
                .isOlderThanOrEquals(ClientVersion.V_1_20_3)) {
            return;
        }

        this.userMap.putIfAbsent(player.getUniqueId(), new PlayerData(player));
    }

    public void removeUser(Player player) {
        if (this.getUser(player) != null) {
            this.userMap.remove(player.getUniqueId());
        }
    }

    public PlayerData getUser(Player player) {
        return this.userMap.get(player.getUniqueId());
    }
}