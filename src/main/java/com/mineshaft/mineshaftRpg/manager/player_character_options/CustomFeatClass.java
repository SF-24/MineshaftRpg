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

import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;

import java.util.Map;

public class CustomFeatClass {

    // Whether it is restricted to a culture
    private boolean cultureRestricted = true;

    // The culture it is restricted to (if the above boolean is checked)
    private String culture = "human_bree";
    private boolean isCultureCustom = false;

    // Display values
    private String description = "Virtue Description";
    private String virtueName = "Virtue Name";

    // Minimum level
    private int minimumLevel = 1;

    // Minimum ability scores
    private Map<AbilityScores, Integer> minimumAbilityScores = Map.of(AbilityScores.WIS, 10, AbilityScores.DEX, 10);

    // Boolean - whether it's a passive ability
    private Map<String, Boolean> abilities = Map.of("Ability1",false,"Ability2",false,"PassiveAbility",true);
    private Map<AbilityScores,Integer> abilityScoreIncreases = Map.of(AbilityScores.WIS, 1, AbilityScores.DEX, 1);


    public String getName() {return virtueName;}
    public String getDescription() {return description;}

    public boolean isCultureRestricted() {return cultureRestricted;}
    public String getCulture() {return culture;}
    public boolean isCultureCustom() {return isCultureCustom;}

    public int getMinimumLevel() {return minimumLevel;}

    public Map<AbilityScores, Integer> getMinimumAbilityScores() {return minimumAbilityScores;}

    public Map<String, Boolean> getAbilities() {return abilities;}
    public Map<AbilityScores, Integer> getAbilityScoreIncreases() {return abilityScoreIncreases;}
}
