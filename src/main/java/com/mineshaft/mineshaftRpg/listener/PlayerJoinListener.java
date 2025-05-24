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
import com.mineshaft.mineshaftRpg.manager.ExperienceManager;
import com.mineshaft.mineshaftRpg.manager.player_data.CharacterCreationManager;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(JsonProfileBridge.getCurrentProfile(event.getPlayer()).equals("Default")) {
            // TODO: Create Profile
            PlayerMenuManager.openProfileMenu(event.getPlayer(),true);
        }
        CharacterCreationManager.setDefaultData(event.getPlayer());

        ExperienceManager.updateXpBar(event.getPlayer());
        JsonPlayerBridge.loadInventory(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        JsonPlayerBridge.saveInventory(event.getPlayer());
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
