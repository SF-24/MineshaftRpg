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
    private final boolean cultureRestricted = true;

    // The culture it is restricted to (if the above boolean is checked)
    private final String culture = "human_bree";

    // Minimum level
    private final int minimumLevel = 1;

    // Minimum ability scores
    private final Map<AbilityScores, Integer> minimumAbilityScores = Map.of(AbilityScores.WIS, 10, AbilityScores.DEX, 10);

    // Boolean - whether they just level up if you own them
    private final List<String> abilities = List.of("Ability1","Ability2","Ability3");
    private final List<String> passiveAbilities = List.of("PassiveAbility1","PassiveAbility2");

    // ASI
    private final Map<AbilityScores,Integer> abilityScoreIncreases = Map.of(AbilityScores.WIS, 1, AbilityScores.DEX, 1);
}
