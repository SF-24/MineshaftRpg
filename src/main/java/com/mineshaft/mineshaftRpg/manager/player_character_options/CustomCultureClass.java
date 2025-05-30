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
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class CustomCultureClass {

    private boolean locked = false;

    private String id = "culture_id_do_not_change_after_setting";
    private String name = "Name";
    private String description = "A description...";
    private Map<AbilityScores, Integer> abilityScores = Map.of(AbilityScores.DEX,2,AbilityScores.CHA,1);
    private int abilityScorePoints = 2;
    private List<PlayerSkills> skillProficiencies = List.of(PlayerSkills.PERCEPTION,PlayerSkills.ACROBATICS);

    // Gain all
    private List<String> weaponProficiencies = List.of("weapon_proficiency1","weapon_proficiency2");

    // Select one, !!!!
    private List<String> toolProficienciesSelect = List.of("tool_proficiency1","tool_proficiency2");
    private List<String> extraLanguages = List.of("extra_language1","extra_language2");

    private List<String> abilities = List.of("example");

    private List<String> startingItems = List.of("item1","item2");
    private List<Material> vanillaStartingItems = List.of(Material.ARROW, Material.BOW);
    private Map<String, String> betonQuestEvents = Map.of("event1","package1","event2","package2");

    private boolean culturalFeat = true;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<AbilityScores, Integer> getAbilityScores() {
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

    public List<String> getAbilities() {
        return abilities;
    }

    public boolean hasCulturalFeat() {
        return culturalFeat;
    }

    public boolean isLocked() {
        return locked;
    }

    public List<Material> getVanillaStartingItems() {
        return vanillaStartingItems;
    }

    public List<String> getStartingItems() {
        return startingItems;
    }

    public Map<String,String> getBetonQuestEvents() {
        return betonQuestEvents;
    }
}
