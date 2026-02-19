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

package com.mineshaft.mineshaftRpg.manager.ui;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftapi.manager.event.click.ClickType;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonSettingsBridge;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.UIUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonClickExecutor {

    public static void click(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(e.getCurrentItem()==null) return;

        switch (UIUtil.getOnclick(e.getCurrentItem())) {
            case "null", "", "nil":
                return;
            case "discoveries", "discovery_menu":
                PlayerMenuManager.Discoveries.openDiscoveryMenu(player, true);
                break;
            case "profile_menu":
                PlayerMenuManager.Profile.openProfileMenu(player,true);
                break;
            case "reset_combo":
                // Reset the combo
                MineshaftRpg.getInstance().getCache().getPlayerCache().getClickCache().resetSettingClicks(player);
                // Reload the UI
                NBT.modify(Objects.requireNonNull(e.getView().getTopInventory().getItem(11)), nbt->{
                    String abilityName=nbt.getString("ability");
                    PlayerMenuManager.Abilities.openAbilityBindingUI(player,true,MineshaftRpg.getInstance().getCache().getAbility(abilityName));
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
                    JsonSettingsBridge.removeAbility(player,abilityName);
                });
                break;
            case "add_click_to_combo":
                // Add clicks to the ability combo
                if(e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_LEFT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.LEFT)) {
                    MineshaftRpg.getInstance().getCache().getPlayerCache().getClickCache().addSettingClick(player, ClickType.LEFT);
                } else if(e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_RIGHT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.RIGHT)) {
                    MineshaftRpg.getInstance().getCache().getPlayerCache().getClickCache().addSettingClick(player, ClickType.RIGHT);
                } else if(e.getClick().equals(org.bukkit.event.inventory.ClickType.MIDDLE)||e.getClick().equals(org.bukkit.event.inventory.ClickType.UNKNOWN)||e.getClick().equals(org.bukkit.event.inventory.ClickType.DROP)) {
                    MineshaftRpg.getInstance().getCache().getPlayerCache().getClickCache().addSettingClick(player, ClickType.MIDDLE);
                } else {
                    return;
                }
                // Reload the UI
                NBT.modify(Objects.requireNonNull(e.getView().getTopInventory().getItem(11)), nbt->{
                    String abilityName=nbt.getString("ability");
                    JsonSettingsBridge.removeAbility(player,abilityName);
                    PlayerMenuManager.Abilities.openAbilityBindingUI(player,true,MineshaftRpg.getInstance().getCache().getAbility(abilityName));
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
                    MineshaftRpg.getInstance().getCache().getPlayerCache().getClickCache().saveSettingClicks(player,abilityName);
                });
                PlayerMenuManager.Abilities.openAbilityUI(player,true, AbilityType.ACTIVE_ABILITY);
                break;
            case "quest_tracker":
                player.closeInventory();
                Bukkit.getServer().dispatchCommand(player,"compass");
                break;
            case "quest_canceller":
                player.closeInventory();
                Bukkit.getServer().dispatchCommand(player,"cancelquest");
                break;
            case "quest_journal":
                player.closeInventory();
                Bukkit.getServer().dispatchCommand(player,"journal");
                break;
            case "quest_menu":
                PlayerMenuManager.Quests.openOldQuestMenu(player, true);
                break;
            case "quest_menu_previous_page":
                if(e.getInventory().getItem(53)==null) return;
                final int[] pagePrevious = new int[1];
                NBT.get(e.getInventory().getItem(53), nbt->{
                    pagePrevious[0] =nbt.getInteger("page")-1;
                });
                PlayerMenuManager.Quests.openQuestMenu(player,Math.max(pagePrevious[0],0),true);
                break;
            case "quest_menu_next_page":
                if(e.getInventory().getItem(53)==null) return;
                final int[] page_next = new int[1];
                NBT.get(e.getInventory().getItem(53), nbt->{
                    page_next[0] =nbt.getInteger("page")+1;
                });
                PlayerMenuManager.Quests.openQuestMenu(player,Math.min(page_next[0], MineshaftPlayerBridge.Quests.getQuestMaxPage(player)),true);
                break;
            case "quest_list":
                PlayerMenuManager.Quests.openQuestMenu(player, 0, true);
            case "abilities":
                if(e.getClick().equals(org.bukkit.event.inventory.ClickType.LEFT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_LEFT)) {
                    PlayerMenuManager.Abilities.openAbilityUI(player, true,AbilityType.ACTIVE_ABILITY);
                } else if(e.getClick().equals(org.bukkit.event.inventory.ClickType.RIGHT) || e.getClick().equals(org.bukkit.event.inventory.ClickType.SHIFT_RIGHT)) {
                    PlayerMenuManager.Abilities.openAbilityUI(player, true,AbilityType.PASSIVE_ABILITY);
                }
                break;
            case "spells", "spellUiClose":
                PlayerMenuManager.Abilities.openAbilityUI(player, true,AbilityType.SPELL);
                break;
            case "ability":
                CustomAbilityClass customAbilityClass = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getAbility(e.getCurrentItem()));
                PlayerMenuManager.Abilities.openAbilityBindingUI(player,true,customAbilityClass);
                break;
            case "spell":
                CustomAbilityClass spell = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getString(e.getCurrentItem(),"spell"));
                PlayerMenuManager.Abilities.openSpellBindingUI(player,true,spell);
                break;
            case "bindSpell":
                CustomAbilityClass spellClass = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getAbility(e.getCurrentItem()));
                NBT.get(e.getCurrentItem(),nbt->{
                    int slot = nbt.getInteger("slot");
                    int hotbar = nbt.getInteger("hotbar");
                    player.sendMessage("Spell " + spellClass.getName() + " bound to slot " + slot + " in hotbar " + hotbar);
                    JsonSettingsBridge.addSpell(player,UIUtil.getAbility(e.getCurrentItem()),hotbar,slot);
                    PlayerMenuManager.Abilities.openSpellBindingUI(player,true,spellClass);
                });
                break;
            case "spellHotbarUp":
                MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellCache().upEditedSpellHotbar(player);
                CustomAbilityClass spellClass1 = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getString(Objects.requireNonNull(e.getInventory().getItem(5)),"spell"));
                PlayerMenuManager.Abilities.openSpellBindingUI(player,true,spellClass1);
                break;
            case "spellUnbind":
                CustomAbilityClass spellClass4 = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getString(Objects.requireNonNull(e.getInventory().getItem(5)),"spell"));
                JsonSettingsBridge.removeSpell(player,spellClass4.getId());
                PlayerMenuManager.Abilities.openSpellBindingUI(player,true,spellClass4);
                break;
            case "spellHotbarDown":
                MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellCache().downEditedSpellHotbar(player);
                CustomAbilityClass spellClass2 = MineshaftRpg.getInstance().getCache().getAbility(UIUtil.getString(Objects.requireNonNull(e.getInventory().getItem(5)),"spell"));
                PlayerMenuManager.Abilities.openSpellBindingUI(player,true,spellClass2);
                break;
            case "ability_scores":
                PlayerMenuManager.openAbilityScoreMenu(player,true);
                break;
            case "skills":
                // TODO: add skills. WIP
                break;
            case "virtues":
                PlayerMenuManager.openFeatMenu(player, 0,true);
                break;
            default:
                Logger.logInfo("default case!");
                break;
        }
    }

}
