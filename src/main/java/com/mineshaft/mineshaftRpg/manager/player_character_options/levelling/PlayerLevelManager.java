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

package com.mineshaft.mineshaftRpg.manager.player_character_options.levelling;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftapi.manager.StringManager;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerLevelManager {

    public static int calculateLevel(int exp) {

        List<Integer> list = new ArrayList<>();
        list = MineshaftRpg.getInstance().getConfig().getIntegerList("experience-per-level");

        int level = 1;

        for(Integer element : list) {
            if(exp >= element) {
                level = list.indexOf(element) + 1;
            }
        }
        return level;
    }

    public static void levelUp(Player player, int oldLevel, int newLevel, int xpToNext) {
        int skillPointsGet = 0;
        List<Integer> levelsGained = new ArrayList<>(Collections.emptyList());

        int cap = MineshaftRpg.getInstance().getConfigManager().getConfiguration().getList("experience-per-level").size();

        // Iterate through levels and level up the player
        for(int iteratedLevel = oldLevel+1; iteratedLevel <= newLevel ; iteratedLevel++) {
            player.sendMessage("leveled up to level: " + iteratedLevel);
            levelsGained.add(iteratedLevel);
            giveLevel(player, iteratedLevel);
            skillPointsGet += getAbilityScoreIncreasesForLevel(iteratedLevel);
        }
        sendLevelUpMessage(player, newLevel, xpToNext, skillPointsGet);
    }

    public static int getFeatsForLevel(int level) {
        if((level-1)%3==0) return 1;
        return 0;
    }

    public static int getAbilityScoreIncreasesForLevel(int level) {
        if(level<=10) return 1;
        if(level%2==0) return 1;
        return 0;
    }

    public static void giveLevel(Player player, int level) {
        switch(level) {
            case 4,7,10,13,16,19 -> MineshaftPlayerBridge.giveFeatPoint(player);
            default -> throw new IllegalStateException("Unexpected value: " + level);
        }
    }

    public static void sendLevelUpMessage(Player player, int level, int xpToNext, int skillPointsGained) {
        ArrayList<String> MessageList = new ArrayList<>();

        MessageList.add(NamedTextColor.GREEN + "_____________________________________________________");
        MessageList.add(NamedTextColor.WHITE + "" + TextDecoration.BOLD + "Level Up");
        MessageList.add("");
        MessageList.add(NamedTextColor.YELLOW + "You are now level " + NamedTextColor.GOLD.toString() + NamedTextColor.GOLD + level + NamedTextColor.YELLOW + "!");
        if(skillPointsGained == 1) {
            MessageList.add(NamedTextColor.YELLOW + "You have earned a skill point.");
        } else if (skillPointsGained > 1) {
            MessageList.add(NamedTextColor.YELLOW + "You have earned " + skillPointsGained + " skill points.");
        }
        if(getFeatsForLevel(level)==1) {
            MessageList.add(NamedTextColor.YELLOW + "You have earned a feat point.");
        } else if(getFeatsForLevel(level)>1) {
            MessageList.add(NamedTextColor.YELLOW + "You have earned " + getFeatsForLevel(level) + " feat points.");
        }

        MessageList.add("");

        if(xpToNext > 0 && (ConfigBridge.getLevelCap()<1 || ConfigBridge.getLevelCap()>level)) {
            int newLevelSoon = level + 1;
            MessageList.add(NamedTextColor.YELLOW + "You need " + NamedTextColor.GOLD + xpToNext + " xp " + NamedTextColor.YELLOW + "to reach level " + NamedTextColor.GOLD + newLevelSoon);
        } else {
            MessageList.add(NamedTextColor.YELLOW + "You have reached the level cap!");
        }
        MessageList.add(NamedTextColor.GREEN + "_____________________________________________________");

        for(String val : MessageList) {
            StringManager.sendCenteredMessage(player, val);
        }
    }

}
