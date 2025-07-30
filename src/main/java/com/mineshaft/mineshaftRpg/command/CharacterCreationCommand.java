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
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.cache.MineshaftCache;
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.CustomBackgroundClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.FeatManager;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CharacterCreationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /char_c set_culture <culture>");
            player.sendMessage(ChatColor.RED + "Usage: /char_c set_background <background>");
            player.sendMessage(ChatColor.RED + "Usage: /char_c set_asi <background> <asi> [page]");
            return false;
        }
        if(args[0].equalsIgnoreCase("set_culture")) {
            if(args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /char_c set_culture <culture>");
            } else {
                CustomCultureClass c = CultureManager.getCustomCulture(args[1]);

                if(!c.isSubculture() && CultureManager.hasCulture(player)) {
                    player.sendMessage(ChatColor.RED + "You have already selected a culture.");
                    return false;
                } else if(c.isSubculture() && CultureManager.hasSubCulture(player)) {
                    player.sendMessage(ChatColor.RED + "You have already selected a culture type.");
                    return false;
                } else if(!CultureManager.isValidCulture(args[1])) {
                    player.sendMessage(ChatColor.RED + "Invalid culture.");
                    return false;
                } else {
                    if(CultureManager.getCustomCulture(args[1]) != null) {
                        if (c!=null && c.isSubculture() && CultureManager.getCustomCulture(CultureManager.getCulture(player))!=null
                                && CultureManager.getCustomCulture(CultureManager.getCulture(player)).getSubcultures()!=null
                                && CultureManager.getCustomCulture(CultureManager.getCulture(player)).getSubcultures().contains(c.getId()
                        )) {
                            CultureManager.givePlayerCulture(player, c.getId());
                        } else if (CultureManager.getCustomCulture(args[1]) != null) {
                            CultureManager.givePlayerCulture(player, c.getId());
                        } else {
                            player.sendMessage(ChatColor.RED + "You may not select this option");
                        }
                    }
                }
            }

                player.sendMessage(ChatColor.AQUA + "You have successfully selected a culture.");
                // TODO: Next part of setup - class?

        } else if(args[0].equalsIgnoreCase("set_background")) {

            CustomBackgroundClass background = MineshaftRpg.getInstance().getCache().getBackground(args[1]);

            player.sendMessage(args[1] + " | " + background.getId() + " | " + background.getName());

            if(background == null) {
                player.sendMessage(ChatColor.RED + "Invalid background.");
                return false;
            } else if(MineshaftPlayerBridge.Backgrounds.hasBackground(player)) {
                player.sendMessage(ChatColor.RED + "You have already selected a background.");
                return false;
            } else {
                MineshaftPlayerBridge.Backgrounds.giveBackground(player, background);
            }
        } else if(args[0].equalsIgnoreCase("set_background_asi")) {
            try {
                MineshaftPlayerBridge.Backgrounds.giveBackgroundAbilityScores(player,AbilityScores.valueOf(args[1].toUpperCase()));
            } catch (NullPointerException ignored) {
                player.sendMessage(ChatColor.RED + "Invalid ability score selection.");
            }
        }
        return false;
    }
}
