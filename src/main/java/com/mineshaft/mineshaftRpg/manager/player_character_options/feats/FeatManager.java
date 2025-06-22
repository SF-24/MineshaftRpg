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

import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FeatManager {

    public static void addFeat(Player player, CustomFeatClass customFeatClass) {
        if(!hasPointsToLearnFeat(player, customFeatClass)||!canLearnFeat(player, customFeatClass)) {
            player.sendMessage(ChatColor.RED + "You cannot learn this feat right now.");
            return;
        }

        for(AbilityScores abilityScores : customFeatClass.getAbilityScoreIncreases().keySet()) {
            MineshaftPlayerBridge.Attributes.addAttribute(player,abilityScores,customFeatClass.getAbilityScoreIncreases().get(abilityScores));
        }
        for(String a : customFeatClass.getAbilities()) {
            MineshaftPlayerBridge.Abilities.addAbility(player,a);
        }
    }

    public static boolean canLearnFeat(Player player, CustomFeatClass customFeatClass) {

        // Culture check
        if(!CultureManager.hasCulture(player)&&customFeatClass.isCultureRestricted()) {
            player.sendMessage(ChatColor.RED + "You have not selected a culture.");
            return false;
        }
        if(!CultureManager.getCulture(player).equals(customFeatClass.getCulture())) {
            return false;
        }

        // Level check
        if(customFeatClass.getMinimumLevel()<JsonPlayerBridge.getLevel(player)) return false;

        // Ability score check
        for(AbilityScores abilityScores : customFeatClass.getMinimumAbilityScores().keySet()) {
            if(MineshaftPlayerBridge.Attributes.getAttribute(player,abilityScores)<customFeatClass.getMinimumAbilityScores().get(abilityScores)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPointsToLearnFeat(Player player, CustomFeatClass customFeatClass) {
        if(MineshaftPlayerBridge.Feats.getFeatPoints(player)>0) return true;
        return customFeatClass.getFeatType().equals(FeatType.CULTURAL_FEAT) && MineshaftPlayerBridge.Feats.getCultureFeatPoints(player) > 0;
    }
}
