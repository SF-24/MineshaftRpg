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
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.CustomBackgroundClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.CustomFeatClass;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftapi.dependency.beton_quest.BetonQuestBridge;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.manager.player.spells.SpellClass;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MineshaftPlayerBridge {

    public static void saveInventory(Player player) {
        JsonPlayerBridge.saveInventory(player, !MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().isHotbarInUse(player));
    }

    public static void savePlayerData(Player player) {
        saveInventory(player);
        JsonPlayerBridge.saveLocation(player);
        JsonPlayerBridge.saveEffects(player);
    }

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
                    case SPELL -> JsonPlayerBridge.addSpell(player,abilityName, new SpellClass(true));
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
            abilityScoreItemLore.add(ChatColor.WHITE + "STR: " + ChatColor.RED + JsonPlayerBridge.getAbilityScoreValue(player,"str") + ChatColor.DARK_RED +" (" + JsonPlayerBridge.getAbilityScoreModifier(player, "str") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "DEX: " + ChatColor.RED + JsonPlayerBridge.getAbilityScoreValue(player,"dex") + ChatColor.DARK_RED +" (" + JsonPlayerBridge.getAbilityScoreModifier(player, "dex") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "CON: " + ChatColor.RED + JsonPlayerBridge.getAbilityScoreValue(player,"con") + ChatColor.DARK_RED +" (" + JsonPlayerBridge.getAbilityScoreModifier(player, "con") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "INT: " + ChatColor.AQUA + JsonPlayerBridge.getAbilityScoreValue(player,"int") + ChatColor.BLUE +" (" + JsonPlayerBridge.getAbilityScoreModifier(player, "int") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "WIS: " + ChatColor.AQUA + JsonPlayerBridge.getAbilityScoreValue(player,"wis") + ChatColor.BLUE +" (" + JsonPlayerBridge.getAbilityScoreModifier(player, "wis") + ")");
            abilityScoreItemLore.add(ChatColor.WHITE + "CHA: " + ChatColor.AQUA + JsonPlayerBridge.getAbilityScoreValue(player,"cha") + ChatColor.BLUE +" (" + JsonPlayerBridge.getAbilityScoreModifier(player, "cha") + ")");
            return abilityScoreItemLore;
        }

        public static void addAbilityScore(Player player, AbilityScores abilityScores, int value) {
            int newValue = value + Math.max(getAbilityScore(player,abilityScores),8);
            JsonPlayerBridge.setAbilityScore(player, (abilityScores).name().toLowerCase(),newValue);
        }

        public static int getAbilityScore(Player player, AbilityScores abilityScores) {
            return JsonPlayerBridge.getAbilityScoreValue(player,abilityScores.name().toLowerCase());
        }

        public static int getAbilityScoreModifier(Player player, AbilityScores abilityScores) {
            return JsonPlayerBridge.getAbilityScoreModifier(player, abilityScores.name().toLowerCase());
        }
    }

    public static class Backgrounds {

        public static void setBackground(Player player, String background) {
            JsonPlayerBridge.setCharacterDataValue(player,"background",background);
        }

        public static String getBackground(Player player) {
            return JsonPlayerBridge.getCharacterDataValue(player,"background");
        }

        public static boolean hasBackground(Player player) {
            return JsonPlayerBridge.getCharacterDataValue(player,"background")!=null && !JsonPlayerBridge.getCharacterDataValue(player,"background").isBlank();
        }

        public static CustomBackgroundClass getBackgroundClass(Player player) {
            return MineshaftRpg.getInstance().getCache().getBackground(getBackground(player));
        }

        public static void giveBackground(Player player, CustomBackgroundClass background) {

            setBackground(player,background.getId());

            player.sendMessage(background.getId() + " is the id of the selected background");

            // Give the skill proficiencies
            for(PlayerSkills skills : background.getProficiencies()) {
                JsonPlayerBridge.setProficiencyLevel(player,skills,JsonPlayerBridge.getProficiencyLevel(player,skills)+1);
            }

            // TODO: Lore skills

            if(background.getEvents()!=null) {
                for (String eventName : background.getEvents().keySet()) {
                    BetonQuestBridge.runBetonPlayerEvent(player, background.getEvents().get(eventName), eventName);
                }
            }
            MineshaftPlayerBridge.savePlayerData(player);

            if(!hasBackgroundAbilityScores(player)) {
                PlayerMenuManager.Profile.openBackgroundAsiSelector(player);
            }
        }

        public static void giveBackgroundAbilityScores(Player player, AbilityScores abilityScores) {
            // Give the ability scores.
            if (getBackgroundClass(player).getAbilityScores().containsKey(abilityScores)) {
                MineshaftPlayerBridge.Attributes.addAbilityScore(player, abilityScores, getBackgroundClass(player).getAbilityScores().get(abilityScores));
            } else {
                player.sendMessage(ChatColor.RED + "Invalid ability score choice for the selected background");
            }
            JsonPlayerBridge.setCharacterDataValue(player,"hasBackgroundAsi","true");
        }

        public static boolean hasBackgroundAbilityScores(Player player) {
            return JsonPlayerBridge.getCharacterDataValue(player,"hasBackgroundAsi")!=null && JsonPlayerBridge.getCharacterDataValue(player,"hasBackgroundAsi").equals("true");
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
                JsonPlayerBridge.setCharacterDataValue(player, "featPoints", String.valueOf(0));
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

        public static List<String> getFeats(Player player) {
            if(JsonPlayerBridge.getCharDataListElement(player,"feats")==null) {
                return new ArrayList<>();
            }
            return JsonPlayerBridge.getCharDataListElement(player,"feats");
        }

        public static void giveFeat(Player player, CustomFeatClass feat) {
            ArrayList<String> feats = (ArrayList<String>) getFeats(player);
            feats.add(feat.getId());
            JsonPlayerBridge.setCharDataListElement(player, "feats",feats);
        }

        public boolean hasFeat(Player player, CustomFeatClass feat) {
            return getFeats(player).contains(feat.getId());
        }
    }

    public static class Skills {

        public static int getProficiencyBonus(Player player, PlayerSkills skill) {
            switch (JsonPlayerBridge.getProficiencyLevel(player,skill)) {
                case 0 ->{}
                case 1 -> {
                    return PlayerStatManager.getProficiencyBonus(JsonPlayerBridge.getLevel(player));
                }
                case 2 -> {
                    return (int) (PlayerStatManager.getProficiencyBonus(JsonPlayerBridge.getLevel(player))*1.5);
                }
                default -> {
                    return PlayerStatManager.getProficiencyBonus(JsonPlayerBridge.getLevel(player))*2;
                }
            }
            return 0;
        }

        public static int getSkillBonus(Player player, PlayerSkills skills) {
            // Proficiency system:
            // UNTRAINED -> TRAINED -> EXPERT -> MASTER
            //
            // TRAINED: +2 | +3 | +4 | +5  | +6
            // EXPERT:  +3 | +4 | +6 | +7  | +9
            // MASTER:  +4 | +6 | +8 | +10 | +12
            return Attributes.getAbilityScore(player, AbilityScores.valueOf(skills.getBaseAbilityScore().toUpperCase())) + getProficiencyBonus(player, skills);
        }
    }
}
