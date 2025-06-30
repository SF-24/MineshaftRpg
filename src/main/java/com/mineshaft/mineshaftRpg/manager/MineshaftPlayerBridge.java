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
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.passive_events.PassiveAbilities;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.player_data.AttributeManager;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MineshaftPlayerBridge {

    public static void setLanguages(Player player, List<String> languages) {
        if(!languages.contains(ConfigBridge.getDefaultLanguage())) {
            JsonPlayerBridge.addToCharDataList(player, "languages",ConfigBridge.getDefaultLanguage() );
        }
        JsonPlayerBridge.addToCharDataList(player, "languages", languages);
    }


    public static class Abilities {

        public static ArrayList<CustomAbilityClass> getAbilitiesOfType(Player player, AbilityType type) {
            ArrayList<CustomAbilityClass> abilities = new ArrayList<>();
            for(String ability : JsonPlayerBridge.getAbilities(player).keySet()) {
                if(MineshaftRpg.getInstance().getCache().getAbility(ability)==null || !MineshaftRpg.getInstance().getCache().getAbility(ability).getAbilityType().equals(type)) continue;
                abilities.add(MineshaftRpg.getInstance().getCache().getAbility(ability));
            }
            return abilities;
        }

        public static ArrayList<CustomAbilityClass> getAbilities(Player player) {
            ArrayList<CustomAbilityClass> abilities = new ArrayList<>();
            for(String ability : JsonPlayerBridge.getAbilities(player).keySet()) {
                if(MineshaftRpg.getInstance().getCache().getAbility(ability)==null) continue;
                abilities.add(MineshaftRpg.getInstance().getCache().getAbility(ability));
            }
            return abilities;
        }

        public static void addAbility(Player player, String abilityName) {
            if(isValidAbility(abilityName)) {
                AbilityType type = MineshaftRpg.getInstance().getCache().getAbility(abilityName).getAbilityType();
                switch (type) {
                    case ACTIVE_ABILITY -> JsonPlayerBridge.addAbility(player, abilityName, 1);
                    case PASSIVE_ABILITY -> JsonPlayerBridge.addPassiveAbility(player,abilityName, 1);
                    case SPELL -> JsonPlayerBridge.addSpell(player,abilityName, 1);
                }

            }
        }

        public static boolean isValidAbility(String ability) {
            return MineshaftRpg.getInstance().getCache().getAbilityIds().contains(ability);
        }

        public static PassiveAbilities getPassiveAbility(String ability) {
            for(PassiveAbilities a : PassiveAbilities.values()) {
                if(a.name().equalsIgnoreCase(ability)) {
                    return a;
                }
            }
            return null;
        }
    }

    public static class Attributes {
        public static ArrayList<String> getAbilityScoreStrings(Player player) {
            ArrayList<String> abilityScoreItemLore = new ArrayList<>();
            abilityScoreItemLore.add(ChatColor.WHITE + "STR: " + ChatColor.RED + JsonPlayerBridge.getAbilityScoreValue(player,"str") + ChatColor.DARK_RED +" (" + AttributeManager.calculateAttributeModifier(player, "str") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "DEX: " + ChatColor.RED + JsonPlayerBridge.getAbilityScoreValue(player,"dex") + ChatColor.DARK_RED +" (" + AttributeManager.calculateAttributeModifier(player, "dex") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "CON: " + ChatColor.RED + JsonPlayerBridge.getAbilityScoreValue(player,"con") + ChatColor.DARK_RED +" (" + AttributeManager.calculateAttributeModifier(player, "con") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "INT: " + ChatColor.AQUA + JsonPlayerBridge.getAbilityScoreValue(player,"int") + ChatColor.BLUE +" (" + AttributeManager.calculateAttributeModifier(player, "int") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "WIS: " + ChatColor.AQUA + JsonPlayerBridge.getAbilityScoreValue(player,"wis") + ChatColor.BLUE +" (" + AttributeManager.calculateAttributeModifier(player, "wis") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "CHA: " + ChatColor.AQUA + JsonPlayerBridge.getAbilityScoreValue(player,"cha") + ChatColor.BLUE +" (" + AttributeManager.calculateAttributeModifier(player, "cha") + ")");
            return abilityScoreItemLore;
        }

        public static void addAttribute(Player player, AbilityScores abilityScores, int value) {
            int newValue = value + Math.max(getAttribute(player,abilityScores),8);
            JsonPlayerBridge.setAbilityScore(player, (abilityScores).name().toLowerCase(),newValue);
        }

        public static int getAttribute(Player player, AbilityScores abilityScores) {
            return JsonPlayerBridge.getAbilityScoreValue(player,abilityScores.name().toLowerCase());
        }
    }

    public static class Feats {
        // Feats

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
    }
}
