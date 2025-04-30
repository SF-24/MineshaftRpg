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

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftapi.manager.StringManager;
import org.bukkit.ChatColor;
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

    public static void levelUp(Player player, int oldLevel, int level, int xpToNext) {
        int skillPointsGet = 0;
        List<Integer> levelsGained = new ArrayList<>(Collections.emptyList());

        int cap = MineshaftRpg.getInstance().getConfigManager().getConfiguration().getList("experience-per-level").size();
        for(int i = oldLevel+1; i <= level ; i++) {
            player.sendMessage("leveled up to level: " + i);
            levelsGained.add(level);
            skillPointsGet += 1;
        }
        sendLevelUpMessage(player, level, xpToNext, skillPointsGet);
    }

    public static void sendLevelUpMessage(Player player, int level, int xpToNext, int skillPointsGained) {
        ArrayList<String> MessageList = new ArrayList<>();

        MessageList.add(ChatColor.GREEN + "_____________________________________________________");
        MessageList.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Level Up");
        MessageList.add("");
        MessageList.add(ChatColor.YELLOW + "You are now level " + ChatColor.GOLD.toString() + ChatColor.GOLD + level + ChatColor.YELLOW + "!");
        if(skillPointsGained == 1) {
            MessageList.add(ChatColor.YELLOW + "You have earned a skill point.");
        } else if (skillPointsGained > 1) {
            MessageList.add(ChatColor.YELLOW + "You have earned " + skillPointsGained + " skill points.");
        }
        MessageList.add("");

        if(xpToNext > 0) {
            int newLevelSoon = level + 1;
            MessageList.add(ChatColor.YELLOW + "You need " + ChatColor.GOLD + xpToNext + " xp " + ChatColor.YELLOW + "to reach level " + ChatColor.GOLD + newLevelSoon);
        } else {
            MessageList.add(ChatColor.YELLOW + "You have reached the level cap!");
        }
        MessageList.add(ChatColor.GREEN + "_____________________________________________________");

        for(String val : MessageList) {
            StringManager.sendCenteredMessage(player, val);
        }
    }

}
