package com.mineshaft.mineshaftRpg.manager;

import org.bukkit.entity.Player;

public class ExperienceManager {

    public void updateXpBar(Player player) {
        int xpAmount = 0;
        int level = 0;

        // TODO: implement correct EXP calculations

        player.setExp(0);
        player.setLevel(0);
    }

}
