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
import com.mineshaft.mineshaftRpg.manager.PlayerCharacterManager;
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftapi.manager.event.click.ClickType;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonSettingsBridge;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.UIUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Objects;

public class UIListener implements Listener {

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) {
            String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());
            if (title.equals(ChatColor.BLACK + "Discoveries")||title.equals(ChatColor.BLACK + "Ability")||title.equals(ChatColor.BLACK + "Abilities")||title.equals(ChatColor.BLACK + "Quests")|| title.equals(ChatColor.BLACK + "Menu") && e.getClickedInventory() != null) {
                e.setCancelled(true);

//                System.out.printf("click: " + UIUtil.getOnclick(e.getCurrentItem()));

                if(e.getCurrentItem()==null) return;

                switch (UIUtil.getOnclick(e.getCurrentItem())) {
                    case "discoveries", "discovery_menu":
                        PlayerMenuManager.openDiscoveryMenu((Player) e.getWhoClicked(), true);
                        break;
                    case "category_town":
                        PlayerMenuManager.openTownRegionMenu((Player) e.getWhoClicked(),true);
                        break;
                    case "category_lore":
                        // TODO:
                        break;
                    case "category_mob":
                        // TODO:
                        break;
                    case "profile_menu":
                        PlayerMenuManager.openProfileMenu((Player) e.getWhoClicked(),true);
                        break;
                    case "reset_combo":
                        // Reset the combo
                        MineshaftRpg.getInstance().getCache().getPlayerCache().resetSettingClicks((Player) e.getWhoClicked());
                        // Reload the UI
                        NBT.modify(Objects.requireNonNull(e.getView().getTopInventory().getItem(11)), nbt->{
                            String abilityName=nbt.getString("ability");
                            PlayerMenuManager.openAbilityBindingUI((Player) e.getWhoClicked(),true,MineshaftRpg.getInstance().getCache().getAbility(abilityName));
                        });
                        break;
                    case "delete_combo":
                        // Save the ability combo
                        if(e.getView().getTopInventory().getItem(11)==null) return;
                        NBT.modify(Objects.requireNonNull(e.getView().getTopInventory().getItem(11)), nbt->{
                            String abilityName=nbt.getString("ability");
                            if(abilityName==null) {
                                Logger.logError("Detected null ability name!");
                                return;
                            }
                            JsonSettingsBridge.removeAbility((Player) e.getWhoClicked(),abilityName);
                        });
                        break;
                    case "add_click_to_combo":
                        // Add clicks to the ability combo
                        if(e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_LEFT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.LEFT)) {
                            MineshaftRpg.getInstance().getCache().getPlayerCache().addSettingClick((Player) e.getWhoClicked(), ClickType.LEFT);
                        } else if(e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_RIGHT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.RIGHT)) {
                            MineshaftRpg.getInstance().getCache().getPlayerCache().addSettingClick((Player) e.getWhoClicked(), ClickType.RIGHT);
                        } else if(e.getClick().equals(org.bukkit.event.inventory.ClickType.MIDDLE)||e.getClick().equals(org.bukkit.event.inventory.ClickType.UNKNOWN)||e.getClick().equals(org.bukkit.event.inventory.ClickType.DROP)) {
                            MineshaftRpg.getInstance().getCache().getPlayerCache().addSettingClick((Player) e.getWhoClicked(), ClickType.MIDDLE);
                        } else {
                            return;
                        }
                        // Reload the UI
                        NBT.modify(Objects.requireNonNull(e.getView().getTopInventory().getItem(11)), nbt->{
                            String abilityName=nbt.getString("ability");
                            JsonSettingsBridge.removeAbility((Player) e.getWhoClicked(),abilityName);
                            PlayerMenuManager.openAbilityBindingUI((Player) e.getWhoClicked(),true,MineshaftRpg.getInstance().getCache().getAbility(abilityName));
                        });
                        break;
                    case "save_combo":
                        // Save the ability combo
                        if(e.getView().getTopInventory().getItem(11)==null) return;
                        NBT.modify(Objects.requireNonNull(e.getView().getTopInventory().getItem(11)), nbt->{
                            String abilityName=nbt.getString("ability");
                            if(abilityName==null) {
                                Logger.logError("Detected null ability name!");
                                return;
                            }
                            MineshaftRpg.getInstance().getCache().getPlayerCache().saveSettingClicks((Player) e.getWhoClicked(),abilityName);
                        });
                        PlayerMenuManager.openAbilityUI((Player) e.getWhoClicked(),true, AbilityType.ACTIVE_ABILITY);
                        break;
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
                    case "abilities":
                        if(e.getClick().equals(org.bukkit.event.inventory.ClickType.LEFT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_LEFT)) {
                            e.getWhoClicked().sendMessage("Showing active abilities");
                            PlayerMenuManager.openAbilityUI((Player) e.getWhoClicked(), true,AbilityType.ACTIVE_ABILITY);
                        } else if(e.getClick().equals(org.bukkit.event.inventory.ClickType.RIGHT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_RIGHT)) {
                            e.getWhoClicked().sendMessage("Showing passive abilities");
                            PlayerMenuManager.openAbilityUI((Player) e.getWhoClicked(), true,AbilityType.PASSIVE_ABILITY);
                        }
                        break;
                    case "spells":
                        PlayerMenuManager.openAbilityUI((Player) e.getWhoClicked(), true,AbilityType.SPELL);
                        break;
                    case "ability":
                        CustomAbilityClass customAbilityClass = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getAbility(e.getCurrentItem()));
                        PlayerMenuManager.openAbilityBindingUI((Player) e.getWhoClicked(),true,customAbilityClass);
                        break;
                    case "bindSpell":
                        CustomAbilityClass spellClass = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getAbility(e.getCurrentItem()));
                        NBT.get(e.getCurrentItem(),nbt->{
                            int slot = nbt.getInteger("slot");
                            int hotbar = nbt.getInteger("hotbar");
                            JsonSettingsBridge.addSpell((Player)e.getWhoClicked(),UIUtil.getAbility(e.getCurrentItem()),slot,hotbar);
                            PlayerMenuManager.openAbilityBindingUI((Player) e.getWhoClicked(),true,spellClass);
                        });
                        break;
                    case "spellHotbarUp":
                        MineshaftRpg.getInstance().getCache().getPlayerCache().upEditedSpellHotbar((Player) e.getWhoClicked());
                        break;
                    case "spellHotbarDown":
                        MineshaftRpg.getInstance().getCache().getPlayerCache().downEditedSpellHotbar((Player) e.getWhoClicked());
                        break;
                    case "ability_scores":
                        PlayerMenuManager.openAbilityScoreMenu((Player) e.getWhoClicked(),true);
                        break;
                    case "skills":
                        // TODO: add skills. WIP
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

                    {
                        Player player = (Player) e.getWhoClicked();
                        PlayerMenuManager.openProfileNameSelector(player, true);
                    }
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
                            PlayerMenuManager.openTownDiscoveries((Player) e.getWhoClicked(),region,page+1,true);
                        });
                    } catch (NullPointerException ignored) {}
                } else if(e.getCurrentItem().getItemMeta()!=null && e.getCurrentItem().getItemMeta().getDisplayName().contains("Previous Page")) {
                    // Previous page
                    NBT.get(e.getCurrentItem(), nbt->{
                        String region = nbt.getString("Identifier");
                        int page = nbt.getInteger("Page");
                        PlayerMenuManager.openTownDiscoveries((Player) e.getWhoClicked(),region,page-1,true);
                    });
                }

            }
        } else {
            if(e.getCurrentItem()!=null && e.getCurrentItem().getType()!=Material.AIR) {
                try {
                    NBT.get(e.getCurrentItem(),nbt->{
                        if(nbt.getBoolean("Immutable")) {
                            e.setCancelled(true);
                        }
                    });
                } catch (NullPointerException ignored) {}
            }
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) {
            String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());
            if (title.equals(ChatColor.BLACK + "Menu") ||
                title.equals(ChatColor.BLACK + "Spells") ||
                title.equals(ChatColor.BLACK + "Spell") ||
                title.equals(ChatColor.BLACK + "Ability Scores") ||
                title.equals(ChatColor.BLACK + "Discoveries") ||
                title.equals(ChatColor.BLACK + "Abilities") ||
                title.equals(ChatColor.BLACK + "Ability") ||
                title.contains(ChatColor.BLACK + "Region") ||
                title.equals(ChatColor.BLACK + "Profiles") ||
                title.equals(ChatColor.BLACK + "Quests")) {

                PlayerMenuManager.genericInventoryClose((Player) e.getPlayer());
            }
        }

    }

}
