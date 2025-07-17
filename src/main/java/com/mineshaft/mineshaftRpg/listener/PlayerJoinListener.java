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

package com.mineshaft.mineshaftRpg.listener;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.PlayerCharacterManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.ExperienceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Redundant null check?
        PlayerCharacterManager.initialiseCharacter(event.getPlayer());
        event.getPlayer().sendMessage(Component.text("This server uses the plugin MineshaftRpg by https://github.com/SF-24", NamedTextColor.AQUA));
        MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().setEnergy(event.getPlayer(), 20);
        MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().updateRegistry();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().hasSpellHotbar(event.getPlayer())) {
            MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().deactivateSpellHotbar(event.getPlayer());
        }
        MineshaftPlayerBridge.savePlayerData(event.getPlayer());
        MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().updateRegistry();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        ExperienceManager.updateXpBar(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(MineshaftRpg.getInstance(),()->{
            ExperienceManager.updateXpBar(e.getPlayer());
        },1/20);
    }
}
