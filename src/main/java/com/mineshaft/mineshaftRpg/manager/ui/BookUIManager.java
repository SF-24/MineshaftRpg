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

package com.mineshaft.mineshaftRpg.manager.ui;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.CustomBackgroundClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookUIManager {

    public static BaseComponent[] getBackgroundPageDisplay(CustomBackgroundClass background) {

        TextComponent hoverable = new TextComponent("§4§l" + background.getName() + "\n");
        hoverable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(background.getDescription())));

        // Ability Scores
        ArrayList<TextComponent> asi = new ArrayList<>();

        TextComponent value = new TextComponent(ChatColor.BLACK + "Ability Scores, Pick 1");
        value.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Only one ability score increase from the list may be chosen")));

        for(AbilityScores abilityScores : background.getAbilityScores().keySet()) {
            if (abilityScores != null) {
                TextComponent clickable = new TextComponent(abilityScores.getDarkerColour() + "+" + ChatColor.BLACK + background.getAbilityScores().get(abilityScores) + " " + abilityScores.getName() + "\n");
                clickable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Only one ability score increase from the list may be chosen")));
                asi.add(clickable);
            }
        }
        asi.add(new TextComponent("\n"));

        // Proficiencies
        // SKILL, WEAPONS, TOOLS

        StringBuilder proficiencies = new StringBuilder("Skill Proficiencies: ");
        for(PlayerSkills e : background.getProficiencies()) {
            if(e!=null) {
                proficiencies.append(e.getName()).append(", ");
            } else {
                Logger.logError("Detected invalid skill in '" + e + "' declaration in culture " + background.getId());
            }
        }
        proficiencies.append("\n");
        TextComponent proficiencyList = (new TextComponent(String.valueOf(proficiencies)));
        proficiencyList.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Skill Proficiencies")));

        TextComponent feat;
        if(MineshaftRpg.getInstance().getCache().getFeat(background.getFeat())!=null) {
            feat = new TextComponent(ChatColor.DARK_PURPLE + "\nVirtue: " + MineshaftRpg.getInstance().getCache().getFeat(background.getFeat()).getName() + "\n" + MineshaftRpg.getInstance().getCache().getFeat(background.getFeat()).getDescription());
        } else {
            feat = new TextComponent();
        }
        feat.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Granted feat")));

        TextComponent select = new TextComponent("\n§3§lSELECT BACKGROUND" + "\n");
        select.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/charcreation set_background " + background.getId()));
        select.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to select the background. Make sure to first pick your ability score increases")));

        ComponentBuilder component = new ComponentBuilder().append(hoverable);
        for(TextComponent element : asi) {
            component.append(element);
        }
        component.append(proficiencyList).append(feat).append(select);
        return component.create();
    }

    public static BaseComponent[] getCulturePageDisplay(String culture) {

        String id;
        String name;
        String desc;
        Map<AbilityScores, Integer> scores;
        int scorePoints;
        List<String> weaponProficiencies;
        List<PlayerSkills> skillProficiencies;
        List<String> craftProficiencies = List.of();
        boolean extraFeat;

        CustomCultureClass customCulture = CultureManager.getCustomCulture(culture);
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
            if (abilityScores != null) {
                builder.append(abilityScores.getDarkerColour() + "+" + ChatColor.BLACK + scores.get(abilityScores) + " " + abilityScores.getName() + "\n");
            }
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
            if(e!=null) {
                proficiencies.append(e.getName()).append(", ");
            } else {
                Logger.logError("Detected invalid skill in '" + skillProficiencies + "' declaration in culture " + id);
            }
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
