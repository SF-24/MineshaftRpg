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

package com.mineshaft.mineshaftRpg.command;

import com.mineshaft.mineshaftRpg.manager.ExperienceManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExperienceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        /*
        * TODO: IMPLEMENT add_party_per_player and add_party_split for giving xp to each player in the party
        * */

            /*
            * Listing XP and level of all players on the server
            * */
        if(args.length==1 && args[0].equalsIgnoreCase("list")) {
            if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                sender.sendMessage("");
                sender.sendMessage("SHOWING LIST OF SERVER PLAYERS AND THEIR EXP AND LEVELS:");

                for (Player user : Bukkit.getServer().getOnlinePlayers()) {
                    sender.sendMessage(ChatColor.AQUA + user.getName() + ChatColor.WHITE + " | LEVEL: " + ChatColor.GOLD + JsonPlayerBridge.getLevel(user) + ChatColor.WHITE + ", XP: " + ChatColor.GOLD + JsonPlayerBridge.getXp(user));
                }
            } else {
                sender.sendMessage("You cannot view levels and exp of all online players, because there are none.");
            }

            /*
            * Setting and giving xp
             */
        } else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add")) {
            // Declaring the player
            Player player;
            if(args.length == 3) {
                player = Bukkit.getPlayer(args[2]);
            } else if(args.length==2) {
                if(sender instanceof Player) {player= (Player) sender;} else {
                    sender.sendMessage(ChatColor.RED + "Please specify a player when executing from console.");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Syntax: /exp "+args[0]+" <amount> [player]");
                return false;
            }
            if(player==null) {
                sender.sendMessage(ChatColor.RED + "Aborting action. Null player specified.");
                return false;
            }

            // Declaring amount of xp
            int amount = 0;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Incorrect number format");
            }

            // Command execution
            if(args[0].equalsIgnoreCase("add")) {
                ExperienceManager.addXp(player, amount);
            } else if(args[0].equalsIgnoreCase("set")) {
                ExperienceManager.setXp(player, amount);
                player.sendMessage("Set your EXP to " + ChatColor.AQUA + amount);
            }
        }

        return false;
    }
}
