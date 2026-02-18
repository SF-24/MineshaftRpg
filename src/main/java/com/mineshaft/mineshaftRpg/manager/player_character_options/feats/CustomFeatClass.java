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
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class CustomFeatClass {

    private final String id = "enter_id_here";
    private final String name = "Example Feat";

    // Display values
    private final String description = "Virtue Description";

    // Whether it is restricted to a culture
    private final FeatType featType = FeatType.SKILL_FEAT;

    // The feat type
    private final boolean cultureRestricted = false;

    // The culture it is restricted to (if the above boolean is checked)
    private final List<String> cultures = Collections.singletonList("human_bree");

    // Minimum level
    private final int minimumLevel = 1;

    // Minimum ability scores
    private final Map<AbilityScores, Integer> minimumAbilityScores = Map.of(AbilityScores.WIS, 10, AbilityScores.DEX, 10);

    // Boolean - whether they just level up if you own them
    private final List<String> abilities = List.of("Ability1","Ability2","Ability3");

    // ASI
    private final Map<AbilityScores,Integer> abilityScoreIncreases = Map.of(AbilityScores.WIS, 1, AbilityScores.DEX, 1);

    public boolean canPickFeat(Player player) {
        // TODO: CHECK IF THE PLAYER HAS THE FEAT ALREADY

        if(!(featType.equals(FeatType.CULTURAL_FEAT) && MineshaftPlayerBridge.Feats.getCultureFeatPoints(player)>0) && MineshaftPlayerBridge.Feats.getFeatPoints(player)<0) {
            System.out.println("Not enough points to take the chosen feat!");
            return false;
        }

        if(!MineshaftPlayerBridge.Feats.getFeats(player).contains(id)) {
            if(!isCultureRestricted() || cultures.contains(CultureManager.getCulture(player))) {
                if(minimumLevel<=JsonPlayerBridge.getLevel(player)) {
                    for(AbilityScores abilityScore : minimumAbilityScores.keySet()) {
                        if(MineshaftPlayerBridge.Attributes.getAbilityScore(player,abilityScore)<minimumAbilityScores.get(abilityScore)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasFeat(Player player) {return MineshaftPlayerBridge.Feats.getFeats(player).contains(id);}
}
