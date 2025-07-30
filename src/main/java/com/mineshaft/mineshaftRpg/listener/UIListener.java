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

import com.mineshaft.mineshaftRpg.manager.PlayerCharacterManager;
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.ui.ButtonClickExecutor;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import com.mineshaft.mineshaftapi.util.UIUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public class UIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        System.out.println(e.getClick());

        if (e.getInventory().getHolder() == null) {
            String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());
            if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.BLACK + "Ability Scores") && e.getClickedInventory() != null) {
                e.setCancelled(true);

                if(e.getCurrentItem()==null) return;
                // Increase ability score if it can be increased
                if(UIUtil.getOnclick(e.getCurrentItem())!=null) {
                    Player player = (Player) e.getWhoClicked();
                    String onClick = UIUtil.getOnclick(e.getCurrentItem());
                    if(JsonPlayerBridge.getSkillPoints(player)>0 && JsonPlayerBridge.getAbilityScoreValue(player,onClick)< ConfigBridge.getAbilityScoreCap(JsonPlayerBridge.getLevel(player))) {
                        JsonPlayerBridge.setAbilityScore(player,UIUtil.getOnclick(e.getCurrentItem()),1+JsonPlayerBridge.getAbilityScoreValue(player, onClick));
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
                    PlayerMenuManager.Profile.openProfileNameSelector(player, true);
                } else if(e.getCurrentItem().getItemMeta()!=null) {
                    if(UIUtil.getOnclick(e.getCurrentItem())!=null) {
                        Player player = (Player) e.getWhoClicked();
                        String profile = ChatColor.stripColor(UIUtil.getOnclick(e.getCurrentItem()));

                        if(JsonProfileBridge.getProfiles(player).contains(profile)) {
                            PlayerCharacterManager.setProfile((Player) e.getWhoClicked(), profile);
                        } else {
                            player.sendMessage(ChatColor.RED + "This profile does not exist!");
                            // TODO: make new character. Add selection options
                        }
                    }
                }

            } else if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains(ChatColor.BLACK + "Region") && e.getClickedInventory() != null) {
                // Discovery screen

                e.setCancelled(true);

                if(e.getCurrentItem()==null) {return;}

                if(e.getCurrentItem().getItemMeta()!=null && e.getCurrentItem().getItemMeta().getDisplayName().contains("Next Page")) {
                    // Next page
                    try {
                        NBT.get(e.getCurrentItem(), nbt->{
                            String region = nbt.getString("Identifier");
                            int page = nbt.getInteger("Page");
                            PlayerMenuManager.Discoveries.openTownDiscoveries((Player) e.getWhoClicked(),region,page+1,true);
                        });
                    } catch (NullPointerException ignored) {}
                } else if(e.getCurrentItem().getItemMeta()!=null && e.getCurrentItem().getItemMeta().getDisplayName().contains("Previous Page")) {
                    // Previous page
                    NBT.get(e.getCurrentItem(), nbt->{
                        String region = nbt.getString("Identifier");
                        int page = nbt.getInteger("Page");
                        PlayerMenuManager.Discoveries.openTownDiscoveries((Player) e.getWhoClicked(),region,page-1,true);
                    });
                }

            } else if (menuList.contains(ChatColor.translateAlternateColorCodes('&',title)) && e.getClickedInventory() != null) {
                e.setCancelled(true);

                // Button execution
                ButtonClickExecutor.click(e);
            }
        } else {
            if(e.getClick().equals(ClickType.WINDOW_BORDER_LEFT) || e.getClick().equals(ClickType.WINDOW_BORDER_RIGHT)) {
                PlayerMenuManager.openCharacterMenu((Player) e.getWhoClicked());
            } else if(e.getCurrentItem()!=null && e.getCurrentItem().getType()!=Material.AIR) {
                try {
                    NBT.get(e.getCurrentItem(),nbt->{
                        if(nbt.getBoolean("Immutable")) {
                            e.setCancelled(true);
                        }
                        if(nbt.getString("onClick")!=null) {
                            ButtonClickExecutor.click(e);
                        }
                    });
                } catch (NullPointerException ignored) {}
            } else if(e.getCurrentItem()==null || e.getCurrentItem().getType().equals(Material.AIR)) {
                System.out.println(e.getSlot());
                if(e.getInventory().getType().equals(InventoryType.PLAYER) && e.getSlotType().equals(InventoryType.SlotType.RESULT)) {
                    PlayerMenuManager.openCharacterMenu((Player) e.getWhoClicked());
                }
            }
        }
    }

    public static List<String> menuList = List.of(
        ChatColor.BLACK + "Menu",
        ChatColor.BLACK + "Spell",
        ChatColor.BLACK + "Spells",
        ChatColor.BLACK + "Ability Scores",
        ChatColor.BLACK + "Discoveries",
        ChatColor.BLACK + "Abilities",
        ChatColor.BLACK + "Ability",
        ChatColor.BLACK + "Region",
        ChatColor.BLACK + "Profiles",
        ChatColor.BLACK + "Virtues",
        ChatColor.BLACK + "Quests"
    );

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) {
            String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());
            if (menuList.contains(title)) {
                PlayerMenuManager.genericInventoryClose((Player) e.getPlayer());
            }
        }
    }

}
