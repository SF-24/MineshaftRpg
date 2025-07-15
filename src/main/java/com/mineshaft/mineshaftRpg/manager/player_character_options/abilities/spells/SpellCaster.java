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

package com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.spells;

import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.maths.DiceUtil;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpellCaster {

    public static void attemptCastSpell(Player player, CustomAbilityClass spell, Vector dir, boolean isQuickCast) {

        // If the spell has been learned
        if(JsonPlayerBridge.getSpellClass(player, spell.getId()).isLearned()) {
            attemptCastSpell(player, spell, dir, isQuickCast);
        // If it has not yet been learned
        } else {
            attemptCastSpell(player, spell, dir, isQuickCast);
        }
    }

    public static void attemptCastNotLearnedSpell(Player player, CustomAbilityClass spell, Vector dir, boolean isQuickCast) {

        // Basic parameters
        int primaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.INT);
        int secondaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.WIS);

        int requiredEnergy = spell.getCastCost();

        // Get the learning progress
        int learnProgress = JsonPlayerBridge.getSpellClass(player, spell.getId()).getProgress();

        // Get the learning modifier, capped at +10, with a minimum of -5
        int spellProgressModifier = Math.max(Math.min(PlayerStatManager.calculateAbilityScoreModifier(learnProgress), 10), -5);

        // Get the base casting dice roll
        int castRoll = DiceUtil.rollDice(20);

        // Add the modifiers to the roll
        castRoll+=2*primaryCastingMod+secondaryCastingMod;

        int spellDifficulty = spell.getLearnDifficulty();

        SpellCastResult result = SpellCastResult.FIZZLES;

        if(castRoll<spellDifficulty) {
            // The spell fails

            if(castRoll<spellDifficulty-10) {
                // Fails badly
                result = SpellCastResult.FAILS;
            }

            if(castRoll>=spellDifficulty-2) {
                // Almost succeeds
                result = SpellCastResult.SEMI_SUCCESS;
            }

            // Otherwise fizzles.
        } else {
            // If the spell progress is >= to the spell difficulty, it is learned.
            result = SpellCastResult.SUCCESS;
        }

        // if req. energy >0
        if(requiredEnergy>0) {
            if (requiredEnergy > EnergyManager.getEnergyCount(player)) {
                player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Not enough energy to cast spell");
                spellCastType = SpellCastType.FAILS;
            }
            EnergyManager.takeEnergy(rpg, player, requiredEnergy);
        }






    }

    public static void attemptCastLearnedSpell(Player player, CustomAbilityClass spell, Vector dir, boolean isQuickCast) {

        // Basic parameters
        int primaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.INT);
        int secondaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.WIS);

        int requiredEnergy = spell.getCastCost();


    }

    public static void learnSpell(Player player, CustomAbilityClass spell) {


    }

}
