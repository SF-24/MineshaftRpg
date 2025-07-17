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

package com.mineshaft.mineshaftRpg.dependencies;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private MineshaftRpg plugin = MineshaftRpg.getInstance(); // This would be the plugin your expansion depends on

    @Override
    public @NotNull String getIdentifier() {
        return "rpg";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Sebastian Frynas";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        // This sets plugin to the SomePlugin instance you get through the PluginManager
        return (plugin = (MineshaftRpg) Bukkit.getPluginManager().getPlugin("MineshaftRpg")) != null;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("energy")){
            if(MineshaftRpg.getInstance().getCache().getPlayerCache().getPlayerEnergy().containsKey(player.getUniqueId())){
                return String.valueOf(MineshaftRpg.getInstance().getCache().getPlayerCache().getPlayerEnergy().get(player.getUniqueId()));
            } else {
                return String.valueOf(MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().getMaxEnergy());
            }
        }
        return null; // Placeholder is unknown by the expansion
    }

}
