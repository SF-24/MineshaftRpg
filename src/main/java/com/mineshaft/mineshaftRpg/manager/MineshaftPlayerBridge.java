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

import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.entity.Player;

import java.util.List;

public class MineshaftPlayerBridge {

    public static void addAttribute(Player player, AbilityScores abilityScores, int value) {
        int newValue = value + Math.max(getAttribute(player,abilityScores),8);
        JsonPlayerBridge.setAttribute(player, (abilityScores).name().toLowerCase(),newValue);
    }

    public static int getAttribute(Player player, AbilityScores abilityScores) {
        return JsonPlayerBridge.getAttribute(player,abilityScores.name().toLowerCase());
    }

    public static void giveFeatPoint(Player player) {
        int points = getFeatPoints(player)+1;
        JsonPlayerBridge.setCharacterDataValue(player,"featPoints", String.valueOf(points));
    }

    public static void giveCultureFeatPoint(Player player) {
        int points = getCultureFeatPoints(player)+1;
        JsonPlayerBridge.setCharacterDataValue(player,"cultureFeatPoints", String.valueOf(points));
    }

    public static int getFeatPoints(Player player) {
        try {
            return Integer.parseInt(JsonPlayerBridge.getCharacterDataValue(player, "featPoints"));
        } catch (NumberFormatException e) {
            Logger.logError("Cannot load feat points for " + player.getName());
            return 0;
        }
    }

    public static int getCultureFeatPoints(Player player) {
        try {
            return Integer.parseInt(JsonPlayerBridge.getCharacterDataValue(player, "cultureFeatPoints"));
        } catch (NumberFormatException e) {
            Logger.logError("Cannot load culture feat points for " + player.getName());
            return 0;
        }
    }

    public static void giveCultureStartingItems(Player player, String culture, boolean isCustom) {
        JsonPlayerBridge.setCharacterDataValue(player, "hasCultureStartingItems","true");
        // TODO:
    }


    public static boolean hasCultureStartingItems(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "hasCultureStartingItems").equals("true");
    }

    public static void giveCultureAbilities(Player player, List<String> abilities) {
//        JsonPlayerBridge.setCharacterDataValue(player, "hasCultureStartingAbilities","true");
        // TODO: coming soon
    }

    public static void giveCultureAbilities(Player player, String culture, boolean isCultureCustom) {}

    public static boolean hasCultureAbilities(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "hasCultureStartingAbilities").equals("true");
    }
}
