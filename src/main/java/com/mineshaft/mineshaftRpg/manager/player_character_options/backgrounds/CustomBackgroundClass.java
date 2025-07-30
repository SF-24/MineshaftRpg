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

package com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds;

import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class CustomBackgroundClass {

    String name = "Example Background";
    String id = "example_background";
    String description = "Example Background Desc.";

    // Culture: leave empty for any
    List<String> culture = List.of("culture1","culture2");
    Map<String,String> events = Map.of("event1","package1","event2","package2");

    List<PlayerSkills> proficiencies = List.of(PlayerSkills.ACROBATICS, PlayerSkills.LORE);

    // Feat
    String feat = "feat_name";

    // Lore skills, all granted.
    List<String> loreSkills = List.of("type1","type2","type3","type4");

    // Pick 1 of the ones from the list.
    Map<AbilityScores, Integer> abilityScores = Map.of(AbilityScores.DEX, 2, AbilityScores.INT, 2);

    // TODO:

    public boolean isCultureRestricted() {return !culture.isEmpty();}
    public List<String> getCultures() {return culture;}
}
