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

package com.mineshaft.mineshaftRpg.manager.player_data;

import org.bukkit.ChatColor;

public enum AbilityScores {
    STR(ChatColor.RED.toString(), ChatColor.DARK_RED.toString(), "Strength"),
    DEX(ChatColor.GREEN.toString(), ChatColor.DARK_GREEN.toString(), "Dexterity"),
    CON(ChatColor.YELLOW.toString(), ChatColor.GOLD.toString(), "Constitution"),
    INT(ChatColor.AQUA.toString(),ChatColor.DARK_AQUA.toString(), "Intelligence"),
    WIS(ChatColor.BLUE.toString(),ChatColor.DARK_BLUE.toString(), "Wisdom"),
    CHA(ChatColor.LIGHT_PURPLE.toString(),ChatColor.DARK_PURPLE.toString(), "Charisma");

    private final String colour;
    private final String darkColour;
    private final String name;

    AbilityScores(String colour, String darkColour, String name) {
        this.colour=colour;
        this.darkColour=darkColour;
        this.name=name;
    }

    public String getColour() {return colour;}
    public String getDarkerColour() {return darkColour;}
    public String getName() {return name;}
}