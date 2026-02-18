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

package com.mineshaft.mineshaftRpg.manager.player_character_options.cultures;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.beton_quest.BetonQuestBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CultureManager {

    public static boolean isCustomCulturesInitialised() {
        try {
            Class.forName("CustomCultures");
        } catch (ClassCastException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidCulture(String culture) {
        if(getCustomCulture(culture)!=null)return true;
        return false;
    }

    public static CustomCultureClass getCustomCulture(String culture) {
        for(CustomCultureClass c : MineshaftRpg.getInstance().getCache().getCultureCache()) {
            if(c.getId().equalsIgnoreCase(culture)) {
                return c;
            }
        }
        return null;
    }

    public static void setCulture(Player player, String culture) {
        JsonPlayerBridge.setCharacterDataValue(player, "culture", culture);
    }

    public static void setSubCulture(Player player, String culture) {
        JsonPlayerBridge.setCharacterDataValue(player, "subCulture", culture);
    }

    public static void setAdoptedCulture(Player player, String culture) {
        JsonPlayerBridge.setCharacterDataValue(player, "adoptedCulture", culture);
    }

    public static boolean hasCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "culture") != null;
    }

    public static boolean hasSubCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "subCulture") != null;
    }

    public static boolean hasAdoptedCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "adoptedCulture") != null;
    }

    public static String getCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "culture");
    }

    public static String getSubCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "subCulture");
    }

    public static String getAdoptedAncestry(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "adoptedCulture");
    }

    public static void givePlayerCulture(Player player, String cultureName) {
        CustomCultureClass c = getCustomCulture(cultureName);
        if(c==null) return;

        // Base values
        if(c.getAbilityScores()!=null) {
            for (AbilityScores element : c.getAbilityScores().keySet()) {
                MineshaftPlayerBridge.Attributes.addAbilityScore(player, element, c.getAbilityScores().get(element));
            }
        }

        JsonPlayerBridge.addSkillPoints(player,c.getAbilityScorePoints());
        if(!c.isSubculture()) {
            CultureManager.setCulture(player, c.getId());
            // Open subculture menu, if the culture has any subcultures
            if(c.getSubcultures()!=null && !c.getSubcultures().isEmpty()) {
                ArrayList<CustomCultureClass> subCultures = new ArrayList<>();
                for(String id : c.getSubcultures()) {
                    subCultures.add(getCustomCulture(id));
                }
                PlayerMenuManager.Profile.openSubspeciesSelector(player,subCultures);
            } else if(!MineshaftPlayerBridge.Backgrounds.hasBackground(player)) {
                // Open the player background menu
                PlayerMenuManager.Profile.openBackgroundSelector(player);
            }
        } else {
            CultureManager.setSubCulture(player, c.getId());
            if(!MineshaftPlayerBridge.Backgrounds.hasBackground(player)) {
                // Open the player background menu
                PlayerMenuManager.Profile.openBackgroundSelector(player);
            }
        }
        // Proficiencies

        JsonPlayerBridge.setProficiencyLevels(player, c.getSkillProficiencies(),1);
        JsonPlayerBridge.addWeaponProficiencies(player, c.getWeaponProficiencies());

        // Abilities and languages, WIP
        Abilities.giveCultureAbilities(player, c.getAbilities());
        MineshaftPlayerBridge.setLanguages(player,c.getExtraLanguages());

        // Feat
        if(c.hasCulturalFeat()) {
            MineshaftPlayerBridge.Feats.giveCultureFeatPoint(player);
        }
        Items.giveCultureStartingItems(player,cultureName);
    }

    public static class Abilities {
        public static void giveCultureAbilities(Player player, List<String> abilities) {
//        JsonPlayerBridge.setCharacterDataValue(player, "hasCultureStartingAbilities","true");
            // TODO: coming soon
        }

        public static void giveCultureAbilities(Player player, String culture) {

        }

        public static boolean hasCultureAbilities(Player player) {
            return JsonPlayerBridge.getCharacterDataValue(player, "hasCultureStartingAbilities").equals("true");
        }
    }

    public static class Items {
        // Starting items

        public static void giveCultureStartingItems(Player player, String culture) {
            if(!CultureManager.getCustomCulture(culture).isSubculture()) {
                JsonPlayerBridge.setCharacterDataValue(player, "hasCultureStartingItems","true");
                BetonQuestBridge.runBetonPlayerEvent(player, ConfigBridge.getBetonQuestStartingItemEventPackage(), ConfigBridge.getBetonQuestStartingItemEvent());
            } else {
                JsonPlayerBridge.setCharacterDataValue(player, "hasSubCultureStartingItems","true");
            }
            if(CultureManager.getCustomCulture(culture).getStartingItems()!=null && !CultureManager.getCustomCulture(culture).getStartingItems().isEmpty()) {
                for (String item : CultureManager.getCustomCulture(culture).getStartingItems()) {
                    if (item != null && MineshaftApi.getInstance().getItemManagerInstance().getItem(item) != null) {
                        player.getInventory().addItem(MineshaftApi.getInstance().getItemManagerInstance().getItem(item));
                    }
                }
            }
            if(CultureManager.getCustomCulture(culture).getVanillaStartingItems()!=null && !CultureManager.getCustomCulture(culture).getVanillaStartingItems().isEmpty()) {
                for (Material material : CultureManager.getCustomCulture(culture).getVanillaStartingItems()) {
                    player.getInventory().addItem(new ItemStack(material));
                }
            }
            if(CultureManager.getCustomCulture(culture).getBetonQuestEvents()!=null && !CultureManager.getCustomCulture(culture).getBetonQuestEvents().keySet().isEmpty()) {
                for (String eventName : CultureManager.getCustomCulture(culture).getBetonQuestEvents().keySet()) {
                    BetonQuestBridge.runBetonPlayerEvent(player, CultureManager.getCustomCulture(culture).getBetonQuestEvents().get(eventName), eventName);
                }
            }

            MineshaftPlayerBridge.savePlayerData(player);
            // TODO:
        }


        public static boolean hasCultureStartingItems(Player player) {
            return JsonPlayerBridge.getCharacterDataValue(player, "hasCultureStartingItems").equals("true");
        }
    }
}
