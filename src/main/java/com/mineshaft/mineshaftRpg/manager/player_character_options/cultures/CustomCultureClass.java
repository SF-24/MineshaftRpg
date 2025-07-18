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

package com.mineshaft.mineshaftRpg.manager.player_character_options.cultures;

import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomCultureClass {

    @Getter
    private boolean locked = false;
    @Getter
    private boolean isSubculture = false;

    @Getter
    private List<String> subcultures = List.of();

    @Getter
    private String id = "culture_id_do_not_change_after_setting";
    @Getter
    private String name = "Name";

    @Getter
    private List<String> disabledWorlds = List.of();
    @Getter
    private List<String> requiredWorlds = List.of();

    @Getter
    private String description = "A description...";
    @Getter
    private Map<AbilityScores, Integer> abilityScores = Map.of(AbilityScores.DEX,2,AbilityScores.CHA,1);
    @Getter
    private int abilityScorePoints = 2;
    @Getter
    private List<PlayerSkills> skillProficiencies = List.of(PlayerSkills.PERCEPTION,PlayerSkills.ACROBATICS);

    // Gain all
    @Getter
    private List<String> weaponProficiencies = List.of("weapon_proficiency1","weapon_proficiency2");

    // Select one, !!!!
    @Getter
    private List<String> toolProficienciesSelect = List.of("tool_proficiency1","tool_proficiency2");
    @Getter
    private List<String> extraLanguages = List.of("extra_language1","extra_language2");

    @Getter
    private List<String> abilities = List.of("example");

    private List<String> startingItems = List.of("item1","item2");
    private List<Material> vanillaStartingItems = List.of(Material.ARROW, Material.BOW);
    private Map<String, String> betonQuestEvents = Map.of("event1","package1","event2","package2");

    private boolean culturalFeat = true;

    public boolean hasCulturalFeat() {
        return culturalFeat;
    }

    public List<Material> getVanillaStartingItems() {
        if(vanillaStartingItems==null) return List.of();
        return vanillaStartingItems;
    }

    public List<String> getStartingItems() {
        if(startingItems==null) return List.of();
        return startingItems;
    }

    public Map<String,String> getBetonQuestEvents() {
        if(betonQuestEvents==null) return Map.of();
        return betonQuestEvents;
    }

    public @NotNull ArrayList<CustomCultureClass> getSubcultureClasses() {
        ArrayList<CustomCultureClass> customCultureClasses = new ArrayList<>();
        for(String element : subcultures) {
            customCultureClasses.add(CultureManager.getCustomCulture(element));
        }
        return customCultureClasses;
    }

}
