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
import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerActionlistener implements Listener {

    @EventHandler
    public void onPlayerPickupExperience(PlayerExpChangeEvent e) {
        JsonPlayerBridge.addXp(e.getPlayer(), e.getAmount());
        ExperienceManager.updateXpBar(e.getPlayer());
        e.setAmount(0);
    }

}
