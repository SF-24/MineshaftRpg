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

import java.util.*;

public enum Cultures {

    // TODO: add more cultures

    HOBBIT_SHIRE("Hobbits of the Shire",
            "Hobbits are an unobtrusive but very ancient people, more numerous formerly than they are today; " +
                    "for the love peace and quiet and good tilled earth: a well-ordered and well-farmed countryside " +
                    "was their favourite haunt.",
            Map.of(AbilityScores.DEX,2), 0,
            List.of(PlayerSkills.STEALTH), List.of("Pipe"), List.of(/*"Brewer's Supplies", "Carpenter's Tools", "Cartographer's Tools", "Gardener's Tools", "Potter's Tools", "Smith's Tools", "Weaver's Tools", "Woodcarver's Tools",*/ "Cook's Utensils"), Collections.emptyList(),
            List.of(), false),

    DWARF_LONELY_MOUNTAIN("Dwarves of Erebor",
            "There now Dáin son of Nain took up his abode, and he became King under the Mountain, " +
                    "and in time many other Dwarves gathered to his throne in the ancient halls.",
            Map.of(AbilityScores.CON, 2), 0,
            Collections.emptyList(), List.of("Axe", "Handaxe", "Greataxe", "Light Hammer", "Warhammer"), List.of(/*"Jeweller's Tools", "Mason's Tools", "Smith's Tools", "Wood Carver's Tools",*/ "Miner's Tools"), List.of("Dalish", "Khuzdul"),
            List.of(), false),
    // And proficiency in a musical instrument?

    ELF_MIRKWOOD("Wood Elves",
            "In the great hall with pillars hewn out of the living stone sat the Elvenking on a chair of carven wood.",
            Map.of(AbilityScores.DEX, 2, AbilityScores.WIS, 1), 0,
            List.of(PlayerSkills.PERCEPTION,PlayerSkills.STEALTH), List.of("Dagger", "Spear", "Broadsword", "Short Sword", "Short Bow"), Collections.emptyList(), List.of("Sindarin","Silvan Elvish"),
            List.of(), false),

    HUMAN_BARDINGS("Bardings",
            "Bard had rebuilt the town in Dale and Men had gathered to him from the Lake and from South and West, " +
                    "and the desolation was now filled with birds and blossoms in spring and fruit and feasting in autumn.",
            Map.of(AbilityScores.STR, 1), 2, List.of(PlayerSkills.INSIGHT),
            Collections.emptyList(), Collections.emptyList(), List.of("Dalish"),
            List.of(), true),
    HUMAN_BEORNINGS("Beornings",
            "Beorn indeed became a great chief afterwards in those regions and " +
                    "ruled a wide land between the mountains and the wood...",
            Map.of(AbilityScores.STR, 1), 2, List.of(PlayerSkills.INTIMIDATION),
            Collections.emptyList(), Collections.emptyList(), List.of("Vale of Andiun Tongue"),
            List.of(), true),
    HUMAN_DUNEDAIN("Dúnedain",
            "In the wild lands beyond Bree, there were mysterious wanderers. " +
                    "The Bree-folk called them Rangers, and knew nothing of their origin.",
            Map.of(AbilityScores.STR, 1), 2, List.of(PlayerSkills.HISTORY, PlayerSkills.SURVIVAL),
            Collections.emptyList(), Collections.emptyList(), List.of("Sindarin"),
            List.of(), true),
    HUMAN_BREE("Humans of Bree",
            "According to their own tales.. they were the descendants of the first Men that ever wandered into the West " +
                    "of the middle-world. Few had survived the turmoils of the Elder days; but when the Kings returned again over " +
                    "the Great Sea they had found the Bree-men still there, " +
                    "and they were still there now, when the memory of the old Kings had faded into the grass.",
            Map.of(AbilityScores.WIS, 1), 2, List.of(PlayerSkills.PERCEPTION),
            Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
            List.of(), true),
    HUMAN_LAKETOWN("Humans of Laketown", "They had been wealthy and powerful, and there had been fleets of boats on the waters.",
            Map.of(AbilityScores.CHA, 1), 2, List.of(PlayerSkills.PERSUASION),
            Collections.emptyList(), Collections.emptyList(), List.of("Dalish"),
            List.of(), true),
    HUMAN_MINAS_TIRITH("Humans of Minas Tirith",
            "O Gondor, Gondor! Shall Men behold the Silver Tree, " +
                    "or West Wind blow again between the Mountains and the Sea?",
            Map.of(AbilityScores.INT, 1), 2, List.of(PlayerSkills.HISTORY),
            Collections.emptyList(), Collections.emptyList(), List.of("Sindarin"),
            List.of(), true),
    HUMAN_ROHAN("Riders of Rohan",
            "Where now is the horse and the rider? Where is the horn that was blowing? " +
                    "Where is the helm and the hauberk, and the bright hair flowing?",
            Map.of(AbilityScores.WIS, 1), 2, List.of(PlayerSkills.ANIMAL_HANDLING),
            Collections.emptyList(), Collections.emptyList(), List.of("Rohan","Sindarin"),
            List.of(), true),
    HUMAN_WILDERLAND("Woodmen of Wilderland",
            "There were many of them, and they were brave and well armed, and even the Wargs " +
                    "dared not attack them if there were many together, or in the bright day.",
            Map.of(AbilityScores.DEX, 1), 2, List.of(PlayerSkills.SURVIVAL),
            Collections.emptyList(), Collections.emptyList(), List.of("Vale of Andiun Tongue"),
            List.of(), true),
    ;


    final String name;
    final String description;
    final Map<AbilityScores, Integer> abilityScores;
    final int abilityScorePoints;
    final List<PlayerSkills> skillProficiencies;

    // Gain all
    final List<String> itemProficiencies;

    // Select one, !!!!
    final List<String> toolProficienciesSelect;
    final List<String> extraLanguages;
    private final List<String> abilities;
    private final boolean giveFeat;


    Cultures(String name, String description, Map<AbilityScores, Integer> abilityScores, int abilityScorePoints, List<PlayerSkills> skillProficiencies, List<String> weaponProficiencies, List<String> toolProficiencies, List<String> extraLanguages, List<String> abilities, boolean giveFeat) {
        this.name=name;
        this.description=description;
        this.abilityScores=abilityScores;
        this.abilityScorePoints=abilityScorePoints;
        this.skillProficiencies=skillProficiencies;
        this.itemProficiencies=weaponProficiencies;
        this.toolProficienciesSelect=toolProficiencies;
        this.extraLanguages=extraLanguages;
        this.giveFeat=giveFeat;
        this.abilities=abilities;
    }

    public String getName() {return name;}
    public String getDescription() {return description;}

    public Map<AbilityScores, Integer> getAbilityScores() {return abilityScores;}
    public int getAbilityScorePoints() {return abilityScorePoints;}

    public List<PlayerSkills> getSkillProficiencies() {return skillProficiencies;}
    public List<String> getWeaponProficiencies() {return itemProficiencies;}
    public List<String> getToolProficiencies() {return toolProficienciesSelect;}
    public List<String> getExtraLanguages() {return extraLanguages;}

    public boolean isGiveFeat() {
        return giveFeat;
    }

    public List<String> getAbilities() {return abilities;}
}
