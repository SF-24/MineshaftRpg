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
import org.bukkit.entity.Player;

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
        for(CustomCultureClass c : MineshaftRpg.getInstance().getCustomCultures()) {
            if(c.getId().equalsIgnoreCase(culture)) {
                return c;
            }
        }
        return null;
    }

    public static void setCulture(Player player, String culture) {
        JsonPlayerBridge.setCharacterDataValue(player, "culture", culture);
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

    public static void givePlayerCulture(Player player, String cultureName) {
        CustomCultureClass c = getCustomCulture(cultureName);

        // Base values
        for(AbilityScores element : c.getAbilityScores().keySet()) {
            MineshaftPlayerBridge.addAttribute(player,element,c.getAbilityScores().get(element));
        }
        JsonPlayerBridge.addSkillPoints(player,c.getAbilityScorePoints());
        CultureManager.setCulture(player, c.getId());

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
        MineshaftPlayerBridge.giveCultureStartingItems(player,cultureName);
    }



    public static BaseComponent[] getPageDisplay(String culture) {

        String id = "";
        String name = "";
        String desc = "";
        Map<AbilityScores, Integer> scores = Map.of();
        int scorePoints = 0;
        List<String> weaponProficiencies = List.of();
        List<PlayerSkills> skillProficiencies = List.of();
        List<String> craftProficiencies = List.of();
        boolean extraFeat = false;

        CustomCultureClass customCulture = getCustomCulture(culture);
        id=customCulture.getId().toLowerCase();
        name = customCulture.getName();
        desc = customCulture.getDescription();
        scores = customCulture.getAbilityScores();
        scorePoints=customCulture.getAbilityScorePoints();
        weaponProficiencies=customCulture.getWeaponProficiencies();
        skillProficiencies=customCulture.getSkillProficiencies();
        craftProficiencies=customCulture.getToolProficienciesSelect();
        extraFeat=customCulture.hasCulturalFeat();


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

        TextComponent proficiencyList2 = (new TextComponent(otherProficiencies +"\n"));

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
}
