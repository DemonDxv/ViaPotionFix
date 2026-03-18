package dev.demon.potionfix.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerData {
    private Player player;

    public PlayerData(Player player) {
        this.player = player;
    }
}
