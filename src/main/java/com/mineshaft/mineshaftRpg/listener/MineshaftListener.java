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
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.passive_events.PassiveAbilityRegistrar;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.ExperienceManager;
import com.mineshaft.mineshaftapi.events.MineshaftAbilityModifyEvent;
import com.mineshaft.mineshaftapi.events.MineshaftClickTypeEvent;
import com.mineshaft.mineshaftapi.events.MineshaftTownDiscoveryEvent;
import com.mineshaft.mineshaftapi.events.MineshaftUseItemEvent;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.ui.notification.NotificationSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MineshaftListener implements Listener {

    // Discoveries
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

    // Key press, via AriKeys from Mineshaft
    @EventHandler
    public void onClickKey(MineshaftClickTypeEvent e) {
        MineshaftRpg.getInstance().getCache().getClickCache().cacheClick(e.getPlayer(), e.getClickType());
    }

    // Ability change
    @EventHandler
    public void onAbilityChange(MineshaftAbilityModifyEvent e) {
        // Reload passive abilities if the passive abilities are modified.
        if(MineshaftRpg.getInstance().getCache().getAbility(e.getAbilityId())!=null && MineshaftRpg.getInstance().getCache().getAbility(e.getAbilityId()).getAbilityType().equals(AbilityType.PASSIVE_ABILITY)) {
            PassiveAbilityRegistrar.initialisePassiveAbilities(e.getPlayer());
        }
    }

    // Use of an item with events
    @EventHandler
    public void onItemUse(MineshaftUseItemEvent e) {
        if(e.getEvents().contains("wand")) {
            // TODO: Toggle spell ui.
        }
    }


}
