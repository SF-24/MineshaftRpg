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

package com.mineshaft.mineshaftRpg.manager.player_character_options.feats;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FeatManager {

    public static void addFeat(Player player, CustomFeatClass customFeatClass) {
        if(!hasPointsToLearnFeat(player, customFeatClass)||!canLearnFeat(player, customFeatClass)) {
            player.sendMessage(ChatColor.RED + "You cannot learn this feat right now.");
            return;
        }

        for(AbilityScores abilityScores : customFeatClass.getAbilityScoreIncreases().keySet()) {
            MineshaftPlayerBridge.Attributes.addAbilityScore(player,abilityScores,customFeatClass.getAbilityScoreIncreases().get(abilityScores));
        }
        for(String a : customFeatClass.getAbilities()) {
            MineshaftPlayerBridge.Abilities.addAbility(player,a);
        }
    }

    // Duplicate function
    public static boolean canLearnFeat(Player player, CustomFeatClass customFeatClass) {

        // Culture check
        if(!CultureManager.hasCulture(player)&&customFeatClass.isCultureRestricted()) {
            player.sendMessage(ChatColor.RED + "You have not selected a culture. Please rejoin and select one.");
            return false;
        }
        return customFeatClass.canPickFeat(player);
    }

    public static boolean hasPointsToLearnFeat(Player player, CustomFeatClass customFeatClass) {
        if(MineshaftPlayerBridge.Feats.getFeatPoints(player)>0) return true;
        return customFeatClass.getFeatType().equals(FeatType.CULTURAL_FEAT) && MineshaftPlayerBridge.Feats.getCultureFeatPoints(player) > 0;
    }

    public static ArrayList<CustomFeatClass> getAncestryFeatList(Player player, int level) {
        ArrayList<CustomFeatClass> feats = new ArrayList<>();
        for(CustomFeatClass customFeatClass : MineshaftRpg.getInstance().getCache().getFeatCache()) {
            if(customFeatClass.getFeatType()==FeatType.CULTURAL_FEAT) {
                if(!customFeatClass.isCultureRestricted()) {
                    if(customFeatClass.getMinimumLevel()<=JsonPlayerBridge.getLevel(player)) {
                        feats.add(customFeatClass);
                    }
                } else if(customFeatClass.getCultures().contains(CultureManager.getCulture(player)) || customFeatClass.getCultures().contains(CultureManager.getSubCulture(player)) || (CultureManager.hasAdoptedCulture(player) && customFeatClass.getCultures().contains(CultureManager.getAdoptedAncestry(player)))) {
                    feats.add(customFeatClass);
                } else if(customFeatClass.canPickFeat(player) || customFeatClass.hasFeat(player)) {
                    feats.add(customFeatClass);
                }
            }
        }
        return feats;
    }

    public static ArrayList<CustomFeatClass> getSkillFeatList(Player player, int level) {
        // 1st, 5th, 9th, 13th, 17th?
        ArrayList<CustomFeatClass> feats = new ArrayList<>();
        for(CustomFeatClass customFeatClass : MineshaftRpg.getInstance().getCache().getFeatCache()) {
            if(customFeatClass.getFeatType()==FeatType.SKILL_FEAT) {
                if(!customFeatClass.isCultureRestricted() || customFeatClass.getCultures().contains(CultureManager.getCulture(player)) || customFeatClass.getCultures().contains(CultureManager.getSubCulture(player)) || (CultureManager.hasAdoptedCulture(player) && customFeatClass.getCultures().contains(CultureManager.getAdoptedAncestry(player)))) {
                    feats.add(customFeatClass);
                } else if(customFeatClass.canPickFeat(player) || customFeatClass.hasFeat(player)) {
                    feats.add(customFeatClass);
                }
            }
        }
        return feats;
    }

    public static ArrayList<CustomFeatClass> getFeatSlotList(Player player) {
        ArrayList<CustomFeatClass> ancestryFeatList = FeatManager.getAncestryFeatList(player,0);
        ArrayList<CustomFeatClass> skillFeatList = FeatManager.getSkillFeatList(player,0);

        ArrayList<CustomFeatClass> slotList = new ArrayList<>();

        // Level 1;
        slotList.addAll(getFeatsOfLevel(ancestryFeatList, 1));
        // New Line
        while (slotList.size() %9!=0) {slotList.add(null);}

        // Level 5
        slotList.addAll(getFeatsOfLevel(ancestryFeatList, 5));

        // Level 9
        slotList.addAll(getFeatsOfLevel(ancestryFeatList, 9));

        // New Line
        while (slotList.size() %9!=0) {slotList.add(null);}
        
        // Level 13
        slotList.addAll(getFeatsOfLevel(ancestryFeatList, 13));

        // Level 17
        slotList.addAll(getFeatsOfLevel(ancestryFeatList, 17));

        // New Line
        while (slotList.size() %9!=0) {slotList.add(null);}

        // Skill Feats
        slotList.addAll(skillFeatList);
        
        return slotList;
    }

    public static List<CustomFeatClass> getFeatsOfLevel(ArrayList<CustomFeatClass> featList, int level) {
        return featList.stream().filter(element -> (
                element.getMinimumLevel()==level || (level==1 && element.getMinimumLevel()<1)
        )).collect(Collectors.toList());
    }
}
