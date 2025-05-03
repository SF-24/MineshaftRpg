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

import com.mineshaft.mineshaftRpg.manager.ExperienceManager;
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Locale;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // If the player has not had data set, set the data.
        if(JsonPlayerBridge.getAttributeMap(event.getPlayer()).isEmpty()) {
            for(AbilityScores score : AbilityScores.values()) {
                JsonPlayerBridge.setAttribute(event.getPlayer(), score.name().toLowerCase(Locale.ROOT), 8);
            }
            JsonPlayerBridge.setSkillPoints(event.getPlayer(), ConfigBridge.getDefaultSkillPoints());
        }
        ExperienceManager.updateXpBar(event.getPlayer());
        JsonPlayerBridge.loadInventory(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        JsonPlayerBridge.saveInventory(event.getPlayer());
    }
}
