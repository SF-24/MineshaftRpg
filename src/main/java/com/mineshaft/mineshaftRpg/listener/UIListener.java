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

import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftRpg.manager.ui.UIUtil;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class UIListener implements Listener {

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) {
            String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());
            if (title.equals(ChatColor.BLACK + "Quests")|| title.equals(ChatColor.BLACK + "Menu") && e.getClickedInventory() != null) {
                e.setCancelled(true);

                System.out.printf("click: " + UIUtil.getOnclick(e.getCurrentItem()));

                if(e.getCurrentItem()==null) return;

                switch (UIUtil.getOnclick(e.getCurrentItem())) {
                    case "quest_tracker":
                        e.getWhoClicked().closeInventory();
                        Bukkit.getServer().dispatchCommand(e.getWhoClicked(),"compass");
                        break;
                    case "quest_canceller":
                        e.getWhoClicked().closeInventory();
                        Bukkit.getServer().dispatchCommand(e.getWhoClicked(),"cancelquest");
                        break;
                    case "quest_journal":
                        e.getWhoClicked().closeInventory();
                        Bukkit.getServer().dispatchCommand(e.getWhoClicked(),"journal");
                        break;
                    case "quest_menu":
                        PlayerMenuManager.openQuestMenu((Player) e.getWhoClicked(), true);
                        break;
                    case "ability_scores":
//                        e.getWhoClicked().closeInventory();
//                        PlayerMenuManager.genericInventoryOpen((Player) e.getWhoClicked());
                        PlayerMenuManager.openAbilityScoreMenu((Player) e.getWhoClicked(),true);
                        break;
                    case "abilities":
                        //TODO: add abilities
                        break;
                    case "skills":
                        //TODO: add skills
                        break;
                    default:
                        Logger.logInfo("default case!");
                        break;
                }
            } else if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.BLACK + "Ability Scores") && e.getClickedInventory() != null) {
                e.setCancelled(true);

                if(e.getCurrentItem()==null) return;
                // Increase ability score if it can be increased
                if(UIUtil.getOnclick(e.getCurrentItem())!=null) {
                    Player player = (Player) e.getWhoClicked();
                    String onClick = UIUtil.getOnclick(e.getCurrentItem());
                    if(JsonPlayerBridge.getSkillPoints(player)>0 && JsonPlayerBridge.getAttribute(player,onClick)< ConfigBridge.getAbilityScoreCap(JsonPlayerBridge.getLevel(player))) {
                        JsonPlayerBridge.setAttribute(player,UIUtil.getOnclick(e.getCurrentItem()),1+JsonPlayerBridge.getAttribute(player, onClick));
                        JsonPlayerBridge.setSkillPoints(player,JsonPlayerBridge.getSkillPoints(player)-1);
                        PlayerMenuManager.openAbilityScoreMenu(player,true);
                    }
                }
            } else if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.BLACK + "Profiles") && e.getClickedInventory() != null) {
                // Profile creation screen

                    e.setCancelled(true);

                if(e.getCurrentItem()==null) {return;}

                else if(e.getCurrentItem().getType().equals(Material.PEONY)) {

                    Player player = (Player) e.getWhoClicked();
                    PlayerMenuManager.openProfileNameSelector(player,true);

                } else if(e.getCurrentItem().getItemMeta()!=null) {
                    Player player = (Player) e.getWhoClicked();
                    String loadCharacterName = e.getCurrentItem().getItemMeta().getDisplayName();

                        // TODO: Load character
                        if(JsonProfileBridge.getProfiles(player).contains(loadCharacterName)) {
                            JsonProfileBridge.setCurrentProfile(player, loadCharacterName);
                        } else {
                            JsonProfileBridge.addProfile(player, loadCharacterName);
                            JsonProfileBridge.setCurrentProfile(player, loadCharacterName);

                            // TODO: make new character. Add selection options
                        }

                        e.getWhoClicked().closeInventory();
                    }

                }
            }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) {
            String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());
            if (title.equals(ChatColor.BLACK + "Menu") ||
                title.equals(ChatColor.BLACK + "Ability Scores") ||
                title.equals(ChatColor.BLACK + "Profiles") ||
                title.equals(ChatColor.BLACK + "Quests")) {

                PlayerMenuManager.genericInventoryClose((Player) e.getPlayer());
            }
        }

    }

}
