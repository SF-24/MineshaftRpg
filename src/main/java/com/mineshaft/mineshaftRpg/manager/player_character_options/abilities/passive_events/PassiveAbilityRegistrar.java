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

package com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.passive_events;

import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.AbilityType;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.passive_modifiers.PassiveModifiers;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;

public class PassiveAbilityRegistrar {
    public static boolean hasPassiveAbility(Player player, PassiveAbilities ability) {
        return getHardcodedAbilities(player).contains(ability);
    }

    public static ArrayList<PassiveAbilities> getHardcodedAbilities(Player player) {
        ArrayList<PassiveAbilities> passiveAbilities = new ArrayList<>();
        for(CustomAbilityClass abilityClass : MineshaftPlayerBridge.Abilities.getAbilitiesOfType(player, AbilityType.PASSIVE_ABILITY)) {
            for(String event : abilityClass.getHardcodedPassiveModifiers()) {
                passiveAbilities.add(PassiveAbilities.valueOf(event.toUpperCase()));
            }
        }
        return passiveAbilities;
    }

    public static void initialisePassiveAbilities(Player player) {
        JsonPlayerBridge.clearPlayerStatBonuses(player);

        double unarmedDamage = 1.0d;

        for(CustomAbilityClass abilityClass : MineshaftPlayerBridge.Abilities.getAbilitiesOfType(player, AbilityType.PASSIVE_ABILITY)) {
            for(String event : abilityClass.getPassiveModifiers().keySet()) {
                if(PassiveModifiers.getPassiveModifierType(event)==null) {
                    Logger.logError("Invalid parameter: " + event + " in ability " + abilityClass.getId());
                }
                PassiveModifiers.PassiveModifierType modifierType = PassiveModifiers.getPassiveModifierType(event);
                Map<String, Double> params = abilityClass.getPassiveModifiers().get(event);

                switch(modifierType) {
                    case PLAYER_STAT_BONUS -> {
                        for(String paramName : params.keySet()) {
                            try {
                                ItemStats stat = ItemStats.valueOf(paramName.toUpperCase());
                                JsonPlayerBridge.addPlayerStatBonus(player, stat, params.get(paramName));
                            } catch(IllegalArgumentException e) {
                                Logger.logError("Invalid declared player stat: " + paramName + " in ability " + abilityClass.getId());
                            }
                        }
                    }
                    case UNARMED_ATTACK -> {
                        if(params.get("value")!=null) {
                            unarmedDamage += params.get("value");
                        } else {
                            Logger.logError("Missing parameter 'value' for ARMOUR_CLASS_BONUS");
                        }
                    }
                    case DUMMY -> {
                        Logger.logError("Dummy parameter declared in ability " + abilityClass.getId() + " . Skipping");
                    }
                }
            }
        }
        JsonPlayerBridge.setUnarmedDamage(player, unarmedDamage);
    }
}
