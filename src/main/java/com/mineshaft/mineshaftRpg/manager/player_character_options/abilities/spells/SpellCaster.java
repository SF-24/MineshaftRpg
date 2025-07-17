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

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.AbilityExecutor;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.StringManager;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.spells.SpellClass;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.maths.RNGUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpellCaster {

    public static void attemptCastSpell(Player player, CustomAbilityClass spell, boolean isQuickCast) {

        if(spell==null) {
            Logger.logError("Null spell detected. Aborting!");
            return;
        }

        if(JsonPlayerBridge.getSpellClass(player,spell.getId())==null) {
            JsonPlayerBridge.addSpell(player,spell.getId(),new SpellClass());
        }

        // If the spell has been learned
        if(JsonPlayerBridge.getSpellClass(player, spell.getId()).isLearned()) {
            attemptCastLearnedSpell(player, spell, isQuickCast);
        // If it has not yet been learned
        } else {
            attemptCastNotLearnedSpell(player, spell, isQuickCast);
        }
    }

    // Cast a spell which has not yet been learned
    public static void attemptCastNotLearnedSpell(Player player, CustomAbilityClass spell, boolean isQuickCast) {

        // Basic parameters
        int primaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.INT);
        int secondaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.WIS);

        int requiredEnergy = spell.getCastCost();

        // Get the learning progress
        int learnProgress = JsonPlayerBridge.getSpellClass(player, spell.getId()).getProgress();

        // Get the base casting dice roll
        int castRoll = RNGUtil.DiceUtil.rollDice(20);
        // Unused. Used for debugging.
        int originalCastRoll = castRoll;

        // Add the modifiers to the roll
        castRoll+=2*primaryCastingMod+secondaryCastingMod;
        // Get the learning modifier, capped at +10, with a minimum of -5
        castRoll += Math.max(Math.min(PlayerStatManager.calculateAbilityScoreModifier(learnProgress), 10), -5);

        SpellCastResult result = getNotLearnedSpellCastResult(spell, castRoll);

        // if req. energy >0
        if(requiredEnergy>0) {
            if (!MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().hasEnergy(player,requiredEnergy)) {
                StringManager.sendActionBar(player, "Not enough energy to cast spell");
                result = SpellCastResult.FAILS;
            }
            MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().takeEnergy(player,requiredEnergy);
        }

        if(result.equals(SpellCastResult.SUCCESS)) {
            int spellStrength = ((castRoll-spell.getCastDifficulty())/40)*spell.getCastDifficulty()+1;
            Logger.logDebug("Spell Strength: " + spellStrength + " | Learn Difficulty: " + spell.getCastDifficulty());
            Logger.logDebug("Original D20 roll: " + originalCastRoll + " | D20 roll with modifier: " + castRoll);

            AbilityExecutor.executeAbilityOnSelf(player,spell);
            learnSpell(player,spell);
        } else {
            int randomExp = RNGUtil.randIntInRange(result.minimumExperience, result.maximumExperience);
            player.sendMessage(Component.text("+" + randomExp + " EXP for spell " + spell.getName(), NamedTextColor.BLUE, TextDecoration.BOLD));

            SpellClass spellClass = JsonPlayerBridge.getSpellClass(player, spell.getId());
            if (spellClass == null) {
                JsonPlayerBridge.addSpell(player,spell.getId(), new SpellClass(1, randomExp));
            } else {
                spellClass.addProgress(randomExp);
                JsonPlayerBridge.addSpell(player, spell.getId(), spellClass);
            }

            // Make the spell fizzle effect play
            fizzleSpell(player,result);
        }
    }

    // Cast a learned spell
    private static @NotNull SpellCastResult getNotLearnedSpellCastResult(CustomAbilityClass spell, int castRoll) {
        int spellDifficulty = spell.getCastDifficulty();

        SpellCastResult result = SpellCastResult.FIZZLES;

        if(castRoll <spellDifficulty) {
            // The spell fails

            if(castRoll <spellDifficulty-10) {
                // Fails badly
                result = SpellCastResult.FAILS;
            }

            if(castRoll >=spellDifficulty-2) {
                // Almost succeeds
                result = SpellCastResult.SEMI_SUCCESS;
            }

            // Otherwise fizzles.
        } else {
            // If the spell progress is >= to the spell difficulty, it is learned.
            result = SpellCastResult.SUCCESS;
        }
        return result;
    }

    // Get the cast result for a learned spell
    private static @NotNull SpellCastResult getLearnedSpellCastResult(Player player, CustomAbilityClass spell, int castRoll, boolean isQuickCast) {
        int energyBonus = (MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().getEnergy(player))-spell.getCastCost();
        if(energyBonus>0) {
            castRoll+= energyBonus/6;
        } else if(energyBonus<0) {
            castRoll-=energyBonus*5;
        }

        // If the spell is not quick cast, apply a cast bonus
        if(!isQuickCast) castRoll+=5;

        if(castRoll> spell.getCastDifficulty()) {
            MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().takeEnergy(player,spell.getCastCost());
            return SpellCastResult.SUCCESS;
        }
        MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().takeEnergy(player,spell.getCastCost()/2);
        return SpellCastResult.FIZZLES;
    }

    // Get the cast result for a not learned spell
    public static void attemptCastLearnedSpell(Player player, CustomAbilityClass spell, boolean isQuickCast) {

        // Basic parameters
        int primaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.INT);
        int secondaryCastingMod = MineshaftPlayerBridge.Attributes.getAbilityScoreModifier(player, AbilityScores.WIS);

        int requiredEnergy = spell.getCastCost();

        // Get the base casting dice roll
        int castRoll = RNGUtil.DiceUtil.rollDice(20);

        // Unused, used only for debug
        // TODO: remove
        int tempOriginalRoll = castRoll;

        // Add the modifiers to the roll
        castRoll+=primaryCastingMod+secondaryCastingMod/2;
        castRoll+=JsonPlayerBridge.getSpellClass(player,spell.getId()).getLevel()-1;

        // Ignored. Used only for debug
        // TODO: remove
        int tempEnergyBonus = (MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().getEnergy(player))-spell.getCastCost();

        // Get the cast result, includes taking the energy cast cost
        SpellCastResult result = getLearnedSpellCastResult(player,spell,castRoll,isQuickCast);

        // cast spell if cast is successful
        int spellStrength = (castRoll-spell.getCastDifficulty())+1;
        int newSpellStrength = (spellStrength/20)*spell.getCastDifficulty()+1;

        // debug
        Logger.logDebug("STR: " + spellStrength + " | NEW STR: " + newSpellStrength + " | CDIFF: " + spell.getCastDifficulty());
        Logger.logDebug("ROLL: " + tempOriginalRoll + " | ROLL WITH MOD: " + castRoll);
        Logger.logDebug("PR MOD: " + primaryCastingMod + " | SEC MOD: " + secondaryCastingMod);
        Logger.logDebug("SKILL MOD: " + 0 + " | SPELL LVL: " + JsonPlayerBridge.getSpellClass(player,spell.getId()).getLevel());
        Logger.logDebug("ENERGY BONUS: " + tempEnergyBonus + " | IS QUICK CASTED: " + isQuickCast);

        // Give EXP for casting the spell
        giveExpForLearnedSpell(player,spell,RNGUtil.randIntInRange(result.minimumExperience, result.maximumExperience));

        // Execute the spell
        AbilityExecutor.executeAbilityOnSelf(player,spell);
    }

    // Make the player learn the given spell
    public static void learnSpell(Player player, CustomAbilityClass spell) {
        JsonPlayerBridge.addSpell(player,spell.getId(), new SpellClass(1, 0,true));

        player.sendMessage("");
        player.sendMessage(Component.text("Spell learned: ", NamedTextColor.AQUA).append(Component.text(spell.getName(), NamedTextColor.GOLD, TextDecoration.BOLD)));
        player.sendMessage("");

        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.5f);
    }

    // Spell levelling and experience management
    public static void giveExpForLearnedSpell(Player player, CustomAbilityClass spell, int exp) {
        SpellClass spellClass = JsonPlayerBridge.getSpellClass(player,spell.getId());

        if(spellClass==null && spell.getMaximumLevel()>1) {
            spellClass = new SpellClass(1, exp, true);
        } else if(spellClass!=null && spell.getMaximumLevel()>spellClass.getLevel()) {
            spellClass.addProgress(exp);
        } else {
            return;
        }


        if(spell.getMaximumLevel()>spellClass.getLevel()) {
            int newLevel = spellClass.getLevel();

            while(true) {
                if(spell.getMaximumLevel()>spellClass.getLevel()) {
                    if (spellClass.getProgress() > spell.getExperienceToNextLevel(spellClass.getLevel())) {
                        spellClass.setProgress(spellClass.getProgress() - spell.getExperienceToNextLevel(spellClass.getLevel()));
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if(newLevel>spellClass.getLevel()) {
                if (newLevel > spellClass.getLevel() + 1) {
                    StringManager.sendActionBar(player, "§l§b" + "Spell Leveled Up: " + "§r§l§6" + spell.getName() + " §l§b| §r§9" + spellClass.getLevel() + "§r§c->§r§9" + newLevel);
                } else {
                    StringManager.sendActionBar(player, "§l§b" + "Spell Leveled Up: " + "§r§l§6" + spell.getName());
                }
            }
        }
        JsonPlayerBridge.addSpell(player, spell.getId(), spellClass);
    }

    public static void fizzleSpell(Player player, SpellCastResult result) {
        switch (result) {
            case FAILS -> StringManager.sendActionBar(player,"§c§l" + "Your spell failed");
            case FIZZLES, SEMI_SUCCESS -> {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS,1.0f,1.0f);
                StringManager.sendActionBar(player,"§c§l" + "Your spell fizzled");

            }
        }
    }

}
