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
import com.mineshaft.mineshaftapi.manager.player_skills.PlayerSkills;

import java.util.HashMap;
import java.util.List;

public class CustomCultureClass {

    String id = "culture_id_do_not_change_after_setting";
    String name = "Name";
    String description = "A description...";
    HashMap<AbilityScores, Integer> abilityScores = new HashMap<>();
    int abilityScorePoints = 2;
    List<PlayerSkills> skillProficiencies = List.of(PlayerSkills.PERCEPTION,PlayerSkills.SLEIGHT_OF_HAND);

    // Gain all
    List<String> weaponProficiencies = List.of("weapon_proficiency1","weapon_proficiency2");

    // Select one, !!!!
    List<String> toolProficienciesSelect = List.of("tool_proficiency1","tool_proficiency2");
    List<String> extraLanguages = List.of("extra_language1","extra_language2");

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<AbilityScores, Integer> getAbilityScores() {
        return abilityScores;
    }

    public int getAbilityScorePoints() {
        return abilityScorePoints;
    }

    public List<PlayerSkills> getSkillProficiencies() {
        return skillProficiencies;
    }

    public List<String> getExtraLanguages() {
        return extraLanguages;
    }

    public List<String> getToolProficienciesSelect() {
        return toolProficienciesSelect;
    }

    public List<String> getWeaponProficiencies() {
        return weaponProficiencies;
    }
}
