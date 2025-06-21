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

package com.mineshaft.mineshaftRpg.manager.player_character_options.abilities;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AbilityExecutor {

    public static CustomAbilityClass getAbilityClass(@NotNull String id) {
        final CustomAbilityClass[] returnValue = {null};
        MineshaftRpg.getInstance().getCache().getAbilityCache().forEach(customAbilityClass -> {
            if(customAbilityClass.getId().equals(id)) {
                returnValue[0]=customAbilityClass;
            }
        });
        return returnValue[0];
    }

    // Cast the ability on the player
    public static void executeAbilityOnSelf(Player player, CustomAbilityClass ability) {
        if(ability == null) {
            return;
        }
        // TODO: add spell mechanics

        for(String eventName : ability.getCustomEvents()) {
            Event event = MineshaftApi.getInstance().getEventManagerInstance().getEvent(eventName);
            if (event != null) {
                MineshaftApi.getInstance().getEventManagerInstance().runEvent(event, player.getLocation(), player.getUniqueId(), player);
            } else {
                player.sendMessage(Component.text("Error, event: " + eventName + " is null"));
            }
        }
        for(String hardcodedEventName : ability.getHardcodedEvents()) {
            // TODO: Add hardcoded event support (very complex mechanics)
        }
        // TODO: Add cast cost and cooldown

        // Passive abilities are not triggered, due to no having active execution mechanics.
    }

    public static void parseAbilityTriggerCommand(CommandSender sender, @NotNull String @NotNull [] args, boolean checkOwnership) {
        if(sender instanceof Player player) {
            if (args.length == 1) {
                if(MineshaftRpg.getInstance().getCache().getAbilityIds().contains(args[0])) {
                    if (!checkOwnership || JsonPlayerBridge.getAbilities(player).containsKey(args[0])) {
                        // Trigger the ability:
                        executeAbilityOnSelf(player,getAbilityClass(args[0]));
                    } else {
                        player.sendMessage(Component.text("You have not learned this ability",NamedTextColor.RED));
                    }
                } else {
                    player.sendMessage(Component.text("This ability does not exist",NamedTextColor.RED));
                }
            } else {
                sender.sendMessage(Component.text("Invalid arguments. Please specify only one ability.",NamedTextColor.RED));
            }
        } else {
            sender.sendMessage(Component.text("Only players may execute this command.",NamedTextColor.RED));
        }
    }

}
