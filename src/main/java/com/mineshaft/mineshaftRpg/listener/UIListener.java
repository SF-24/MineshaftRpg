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

import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftRpg.manager.ui.UIUtil;
import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class UIListener implements Listener {

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) {
            if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.BLACK + "Menu") && e.getClickedInventory() != null) {
                e.setCancelled(true);

                if(e.getCurrentItem()==null) return;
                switch (UIUtil.getOnclick(e.getCurrentItem())) {
                    case "ability_scores":
                        PlayerMenuManager.openAbilityScoreMenu((Player) e.getWhoClicked());
                        break;
                    case "abilities":
                        //TODO: add abilities
                        break;
                    case "skills":
                        //TODO: add skills
                        break;
                    case null, default:
                        break;
                }
            } else if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.BLACK + "Ability scores") && e.getClickedInventory() != null) {
                e.setCancelled(true);

                if(e.getCurrentItem()==null) return;
                if(UIUtil.getOnclick(e.getCurrentItem())!=null) {
                    Player player = (Player) e.getWhoClicked();
                    if(JsonPlayerBridge.getSkillPoints(player)>0) {
                        JsonPlayerBridge.setAttribute(player,UIUtil.getOnclick(e.getCurrentItem()),JsonPlayerBridge.getAttribute(player, UIUtil.getOnclick(e.getCurrentItem())));
                        JsonPlayerBridge.setSkillPoints(player,JsonPlayerBridge.getSkillPoints(player)-1);
                        PlayerMenuManager.openAbilityScoreMenu(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) {
            if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.BLACK + "Menu") || ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.BLACK + "Ability scores")) {
                PlayerMenuManager.genericInventoryClose((Player) e.getPlayer());
            }
        }

    }

}
