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
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterCreationTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)) return List.of();
        if(args.length==1) {
            return StringUtil.copyPartialMatches(args[0], Collections.singleton("set_culture"),new ArrayList<>());
        } else if(args.length==2 && args[0].equals("set_culture")) {
            ArrayList<String> list = new ArrayList<>();
            for(CustomCultureClass c : MineshaftRpg.getCache().getCultureCache()) {
                if(!c.isLocked() || JsonProfileBridge.getUnlockedCultures((Player)sender).contains(c.getId())) {
                    list.add(c.getId());
                }
            }
        return StringUtil.copyPartialMatches(args[0],list,new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
