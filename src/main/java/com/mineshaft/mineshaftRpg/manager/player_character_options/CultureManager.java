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

package com.mineshaft.mineshaftRpg.manager.player_character_options;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
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
        if(getCulture(culture)!=null)return true;
        if(getCustomCulture(culture)!=null)return true;
        return false;
    }

    public static Cultures getCulture(String culture) {
        for(Cultures c : Cultures.values()) {
            if(c.name().equalsIgnoreCase(culture)) {
                return c;
            }
        }
        return null;
    }

    public static CustomCultureClass getCustomCulture(String culture) {
        for(CustomCultureClass c : MineshaftRpg.getInstance().getCustomCultures()) {
            if(c.getId().equalsIgnoreCase(culture)) {
                return c;
            }
        }
        return null;
    }

    public static void setCulture(Player player, String culture, boolean isCustom) {
        JsonPlayerBridge.setCharacterDataValue(player, "culture", culture);
        JsonPlayerBridge.setCharacterDataValue(player, "isCultureCustom", String.valueOf(isCustom).toLowerCase());
    }

    public static void setLanguages(Player player, List<String> languages) {
        if(!languages.contains(ConfigBridge.getDefaultLanguage())) {
            JsonPlayerBridge.addToCharDataList(player, "languages",ConfigBridge.getDefaultLanguage() );
        }
        JsonPlayerBridge.addToCharDataList(player, "languages", languages);
    }

    public static boolean hasCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "culture") != null;
    }

    public static String getCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player, "culture");
    }

    public static boolean isCustomCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player,"isCultureCustom").equals("true");
    }

    public static void givePlayerCulture(Player player, String cultureName, boolean isCustom) {

        if(isCustom) {
            CustomCultureClass c = getCustomCulture(cultureName);
            // Base values

            for(AbilityScores element : c.getAbilityScores().keySet()) {
                MineshaftPlayerBridge.addAttribute(player,element,c.getAbilityScores().get(element));
            }
            JsonPlayerBridge.addSkillPoints(player,c.getAbilityScorePoints());
            CultureManager.setCulture(player, c.getId(), true);

            // Proficiencies

            JsonPlayerBridge.setProficiencyLevels(player, c.getSkillProficiencies(),1);
            JsonPlayerBridge.addWeaponProficiencies(player, c.getWeaponProficiencies());

            // Abilities and languages, WIP
            MineshaftPlayerBridge.giveCultureAbilities(player, c.getAbilities());
            setLanguages(player,c.getExtraLanguages());

            // Feat
            if(c.hasCulturalFeat()) {
                MineshaftPlayerBridge.giveCultureFeatPoint(player);
            }


        } else {
            Cultures culture= getCulture(cultureName);
            if(culture==null) {
                player.sendMessage(ChatColor.RED + "No Culture found with id " + cultureName);
                return;
            }
            for(AbilityScores element : culture.getAbilityScores().keySet()) {
                MineshaftPlayerBridge.addAttribute(player,element,culture.getAbilityScores().get(element));
            }
            JsonPlayerBridge.addSkillPoints(player,culture.getAbilityScorePoints());
            CultureManager.setCulture(player, culture.name.toLowerCase(), true);

            // Proficiencies
            JsonPlayerBridge.setProficiencyLevels(player, culture.getSkillProficiencies(),1);
            JsonPlayerBridge.addWeaponProficiencies(player, culture.getWeaponProficiencies());

            MineshaftPlayerBridge.giveCultureAbilities(player, culture.getAbilities());
            setLanguages(player,culture.getExtraLanguages());
            if(culture.isGiveFeat()) {
                MineshaftPlayerBridge.giveCultureFeatPoint(player);
            }

        }
//        MineshaftPlayerBridge.giveCultureAbilities(player,cultureName,isCustom);
        MineshaftPlayerBridge.giveCultureStartingItems(player,cultureName,isCustom);
    }



    public static BaseComponent[] getPageDisplay(String culture, boolean isCustom) {

        String id = "";
        String name = "";
        String desc = "";
        Map<AbilityScores, Integer> scores = Map.of();
        int scorePoints = 0;
        List<String> weaponProficiencies = List.of();
        List<PlayerSkills> skillProficiencies = List.of();
        List<String> toolProficiencies = List.of();
        boolean extraFeat = false;

        if(isCustom) {
            CustomCultureClass c = getCustomCulture(culture);
            id=c.getId().toLowerCase();
            name = c.getName();
            desc = c.getDescription();
            scores = c.getAbilityScores();
            scorePoints=c.getAbilityScorePoints();
            weaponProficiencies=c.getWeaponProficiencies();
            skillProficiencies=c.getSkillProficiencies();
            toolProficiencies=c.getToolProficienciesSelect();
            extraFeat=c.hasCulturalFeat();
        } else {
            Cultures c = getCulture(culture);
            id=c.name().toLowerCase();
            name = c.getName();
            desc = c.getDescription();
            scores = c.getAbilityScores();
            scorePoints=c.getAbilityScorePoints();
            weaponProficiencies=c.getWeaponProficiencies();
            skillProficiencies=c.getSkillProficiencies();
            toolProficiencies=c.getToolProficiencies();
            extraFeat=c.isGiveFeat();
        }



        TextComponent hoverable = new TextComponent("§4§l" + name + "\n");
        hoverable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(desc)));
        ArrayList<TextComponent> page = new ArrayList<>();

        // Ability Scores
        StringBuilder builder = new StringBuilder();
        for(AbilityScores abilityScores : scores.keySet()) {
            builder.append(abilityScores.getDarkerColour() + "+"  + ChatColor.BLACK + scores.get(abilityScores) + " " + abilityScores.getName() + "\n");
        }
        TextComponent abilityScores = new TextComponent(builder.toString());

        TextComponent points;
        if(scorePoints>0) {
            // Ability score points
            points=(new TextComponent(ChatColor.GOLD + "+" + ChatColor.BLACK + scorePoints +  " Ability Score Points\n\n"));
        } else {
            points=new TextComponent("\n");
        }

        // Proficiencies
        // SKILL, WEAPONS, TOOLS


        StringBuilder proficiencies = new StringBuilder("Skill Proficiencies: ");
        for(PlayerSkills e : skillProficiencies) {
            proficiencies.append(e.getName()).append(", ");
        }
        proficiencies.append("\n");
        TextComponent proficiencyList = (new TextComponent(String.valueOf(proficiencies)));

        StringBuilder otherProficiencies = new StringBuilder("Other Proficiencies: ");
        for(String e : weaponProficiencies) {
            otherProficiencies.append(e).append(", ");
        }
//            for(String e : c.getToolProficiencies()) {
//                otherProficiencies.append(e).append(", ");
//            }

        TextComponent proficiencyList2 = (new TextComponent(String.valueOf(otherProficiencies)+"\n"));

        TextComponent feat;
        if(extraFeat) {
            feat = new TextComponent(ChatColor.DARK_PURPLE+"\n+Cultural Virtue+\n");
        } else {
            feat = new TextComponent();
        }

        TextComponent select = new TextComponent("\n§3§lSELECT CULTURE" + "\n");
        select.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/charcreation set_culture " + id));

        // TODO: Abilities

        BaseComponent[] component = new ComponentBuilder().append(hoverable).append(abilityScores).append(points).append(proficiencyList).append(proficiencyList2).append(feat).append(select).create();
        return component;
    }

    // TODO:
    public static List<String> getCustomStartingItems(Cultures cultures) {
        switch (cultures) {
            default:
                break;
        }
        return Collections.emptyList();
    }

    public static List<Material> getVanillaStartingItems(Cultures cultures) {
        switch (cultures) {
            case HUMAN_ROHAN:
                return List.of(Material.HORSE_SPAWN_EGG);
            default:
                break;
        }
        return Collections.emptyList();
    }

}
