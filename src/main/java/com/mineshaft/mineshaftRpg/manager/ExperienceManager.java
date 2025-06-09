/*
 * Copyright (c) 2025. Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.mineshaft.mineshaftRpg.manager;

import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftapi.manager.StringManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.entity.Player;

public class ExperienceManager {

    // Updates the experience bar
    public static void updateXpBar(Player player) {

        if(!ConfigBridge.getDisabledWorlds().contains(player.getWorld().getName())) {

            int level = JsonPlayerBridge.getLevel(player);
            player.setLevel(level);
            float levelXp = Float.parseFloat(String.valueOf(ConfigBridge.getXpPerLevel().get(level)));
            float lastLevelXp = Float.parseFloat(String.valueOf(ConfigBridge.getXpPerLevel().get(level-1)));

            if(level < ConfigBridge.getXpPerLevel().size()) {
                float percentage = (JsonPlayerBridge.getXp(player)-lastLevelXp)/(levelXp-lastLevelXp);
                if(percentage>=0) {
                    player.setExp(percentage);
                } else {
                    player.setExp(0);
                }
            } else {
                player.setExp(0.0f);
            }

        } else {
            Logger.logError("Cannot update xp bar for player " + player.getName());
        }
    }

    // Gives the player a certain amount of experience points
    public static void addXp(Player player, int amount) {

        int newLevel = PlayerLevelManager.calculateLevel(JsonPlayerBridge.getXp(player));
        int toNext;

        if(newLevel <= ConfigBridge.getXpPerLevel().size()) {
            toNext = (int) ConfigBridge.getXpPerLevel().get(newLevel);
            toNext -= JsonPlayerBridge.getXp(player);
        } else {
            toNext = -1;
        }

        StringManager.sendActionBar(player, "§b§l+" + amount + " EXP");
        if(newLevel > JsonPlayerBridge.getLevel(player)) {
            PlayerLevelManager.levelUp(player, JsonPlayerBridge.getLevel(player), newLevel, toNext);
        }

        JsonPlayerBridge.setLevel(player, newLevel);
        JsonPlayerBridge.setXp(player, JsonPlayerBridge.getXp(player)+amount);

        ExperienceManager.updateXpBar(player);
    }

    // Sets player xp
    public static void setXp(Player player, int amount) {

        int newLevel = PlayerLevelManager.calculateLevel(amount);

        if(amount > JsonPlayerBridge.getXp(player)) {
            int toNext;

            if (newLevel <= ConfigBridge.getXpPerLevel().size()) {
                toNext = (int) ConfigBridge.getXpPerLevel().get(newLevel);
                toNext -= JsonPlayerBridge.getXp(player);
            } else {
                toNext = -1;
            }
            if(newLevel > JsonPlayerBridge.getLevel(player)) {
                PlayerLevelManager.levelUp(player, JsonPlayerBridge.getLevel(player), newLevel, toNext);
            }
        }
        JsonPlayerBridge.setLevel(player, newLevel);
        JsonPlayerBridge.setXp(player, amount);
        updateXpBar(player);
    }

    public static int getDiscoveryExperiencePerLevel(int level, double multiplier, int roundValue) {
        int base = 10 + (level+1)*(level+1);
        return (int) (Math.round(multiplier*base / roundValue) * roundValue);
    }

    public static int getTownDiscoveryExperiencePerLevel(Player player, int level, double townSize) {
        double multiplier = (2*townSize-1)/3;
        if(JsonPlayerBridge.getProficiencyLevel(player,PlayerSkills.LORE)>0) {
            if(JsonPlayerBridge.getProficiencyLevel(player,PlayerSkills.LORE)>1) {
                multiplier*=1.666;
            } else {
                multiplier*=1.333;
            }
        }
        if(townSize<2&&level<5) {
            return getDiscoveryExperiencePerLevel(level, multiplier, 2);
        } else {
            return getDiscoveryExperiencePerLevel(level, multiplier, 5);
        }
    }
}
