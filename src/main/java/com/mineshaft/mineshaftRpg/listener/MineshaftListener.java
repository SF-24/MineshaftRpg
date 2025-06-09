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
import com.mineshaft.mineshaftapi.events.MineshaftTownDiscoveryEvent;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.ui.notification.NotificationSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MineshaftListener implements Listener {

    @EventHandler
    public void onPlayerDiscoverTown(MineshaftTownDiscoveryEvent e) {
        // Give experience on town discovery
        ExperienceManager.addXp(e.getPlayer(),
                ExperienceManager.getTownDiscoveryExperiencePerLevel(
                        e.getPlayer(),
                        JsonPlayerBridge.getLevel(e.getPlayer()),
                        e.getTown().getSize()
                )
        );
        // Send title
        NotificationSender.sendTownDiscoveryTitle(e.getPlayer(),e.getTown());
    }
}
