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

import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import lombok.Getter;

import java.util.Map;

@Getter
public class CustomFeatClass {

    // Whether it is restricted to a culture
    private final boolean cultureRestricted = true;

    // The feat type
    private final FeatType featType = FeatType.SKILL_FEAT;

    private final String name = "Example Feat";

    // The culture it is restricted to (if the above boolean is checked)
    private final String culture = "human_bree";

    // Display values
    private final String description = "Virtue Description";
    private final String id = "enter_id_here";

    // Minimum level
    private final int minimumLevel = 1;

    // Minimum ability scores
    private final Map<AbilityScores, Integer> minimumAbilityScores = Map.of(AbilityScores.WIS, 10, AbilityScores.DEX, 10);

    // Boolean - whether it's a passive ability
    private final Map<String, Boolean> abilities = Map.of("Ability1",false,"Ability2",false,"PassiveAbility",true);
    private final Map<AbilityScores,Integer> abilityScoreIncreases = Map.of(AbilityScores.WIS, 1, AbilityScores.DEX, 1);
}
