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

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MineshaftRpgCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length==1 && args[0].equalsIgnoreCase("reload")) {
            MineshaftRpg.getInstance().clearCustomCultureCache();
            MineshaftRpg.getInstance().getJsonCustomCultures().reloadData();
            sender.sendMessage("Reloaded");
        } else if(args.length==1 && args[0].equalsIgnoreCase("generate_examples")) {
            MineshaftRpg.getInstance().getJsonCustomCultures().makeExample();
            MineshaftRpg.getInstance().getJsonCustomFeats().makeExample();
            sender.sendMessage("Generating examples...");
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /mineshaft_rpg <reload|generate_examples>");
        }
        return false;
    }
}
