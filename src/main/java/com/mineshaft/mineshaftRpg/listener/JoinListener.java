package com.mineshaft.mineshaftRpg.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    void joinListener(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // On join
    }

}
