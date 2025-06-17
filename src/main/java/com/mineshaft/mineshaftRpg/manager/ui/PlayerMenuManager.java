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
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.player_data.CharacterCreationManager;
import com.mineshaft.mineshaftapi.dependency.world_guard.DiscoveryCategory;
import com.mineshaft.mineshaftapi.dependency.world_guard.Town;
import com.mineshaft.mineshaftapi.manager.player.json.JsonDiscoveryBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.chat.BaseComponent;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayerMenuManager {

    public static void openCharacterMenu(Player player) {
        genericInventoryOpen(player);
        // CREATE MENU INVENTORY
        Inventory ui = getMenuBackground("Menu");

        ui.setItem(1, UIButtonManager.getPlayerLevelButton(player));
        ui.setItem(2, UIButtonManager.getPlayerAbilityScoreItem(player));
        ui.setItem(3, UIButtonManager.getSkillsItem(player));
        ui.setItem(4, UIButtonManager.getAbilityItem(player));
        ui.setItem(4, UIButtonManager.getCodexItem());
        ui.setItem(7, UIButtonManager.getQuestItem());
        player.openInventory(ui);
    }

    public static void openAbilityUI(Player player, boolean isUpdate) {
        Inventory ui = getLargeMenuBackground("Abilities");

        player.openInventory(ui);
    }

    public static void openAbilityScoreMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Ability Scores");

        for(AbilityScores score : AbilityScores.values()) {
            ui.addItem(UIButtonManager.getAbilityScoreItem(player, score));
        }
        ui.addItem(UIButtonManager.getSkillPointItem(player));
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);
    }

    public static void openQuestMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Quests");
        ui.addItem(UIButtonManager.getQuestCanceller());
        ui.addItem(UIButtonManager.getQuestTracker());
        ui.addItem(UIButtonManager.getJournal());
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);
    }

    public static void openDiscoveryMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Discoveries");
        ui.addItem(UIButtonManager.getDiscoveryCategory(player, DiscoveryCategory.TOWN));
        ui.addItem(UIButtonManager.getDiscoveryCategory(player, DiscoveryCategory.MOB));
        ui.addItem(UIButtonManager.getDiscoveryCategory(player, DiscoveryCategory.LORE));
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);
    }

    public static void openTownRegionMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Discoveries");

        for(String region : JsonDiscoveryBridge.getDiscoveredRegions(player)) {
            ui.addItem(UIButtonManager.getLocationRegion(player,region));
        }
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);

    }

    // Open town discovery menu
    public static void openTownDiscoveries(Player player, String region, int page, boolean isUpdate) {
        ArrayList<ItemStack> items = new ArrayList<>();

        for(Town town : JsonDiscoveryBridge.getDiscoveredTowns(player)) {
            items.add(UIButtonManager.getTownDiscovery(town));
        }
        new GUI(player,page,Bukkit.createInventory(null,54, NamedTextColor.BLACK+"Region"), items, region);
    }

    public static void openProfileMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Profiles");

        for(String profile : JsonProfileBridge.getProfiles(player)) {
            ui.addItem(UIButtonManager.getProfileButton(player,profile));
        }

        ui.addItem(UIButtonManager.getPlusButton("New Profile"));
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);
    }

    public static void openProfileNameSelector(Player player, boolean isUpdate) {
        // Using anvil gui: https://github.com/WesJD/AnvilGUI
        // Thank you for making this plugin, WesJD!

        // Open anvil ui
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String name = stateSnapshot.getText();
                    return Arrays.asList(
                        AnvilGUI.ResponseAction.close(),
                        AnvilGUI.ResponseAction.run(() -> {
                            // On confirm
                            if(!JsonProfileBridge.getCurrentProfile(player).equalsIgnoreCase("Default") && JsonProfileBridge.getCurrentProfile(player)!=null) {
                                JsonPlayerBridge.saveInventory(player);
                                player.getInventory().clear();
                            }
                            JsonProfileBridge.setCurrentProfile(player, name);
                            JsonProfileBridge.addProfile(player, name);
                            CharacterCreationManager.setDefaultData(player);
                            openSpeciesSelector(player);
                        })
                    );
                })
                .preventClose().text("...").title("Character name").itemLeft(new ItemStack(Material.NAME_TAG)).itemOutput(new ItemStack(Material.NAME_TAG)).plugin(MineshaftRpg.getInstance()).open(player);

        inventoryManagement(player,isUpdate);
    }

    public static void openSpeciesSelector(Player player) {
        // TODO:
        player.sendMessage("Character successfully created");

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        assert bookMeta != null;
        bookMeta.addPage(TextDecoration.BOLD + "Select a culture: \n" +
                "Use the arrows underneath the book to select a page with your desired culture and press select.");

        ArrayList<BaseComponent[]> unlockedLockedCulturePages = new ArrayList<>();

        for(CustomCultureClass c : MineshaftRpg.getCache().getCultureCache()) {
            if (!c.isLocked()&&!c.getDisabledWorlds().contains(player.getWorld().getName())) {
                bookMeta.spigot().addPage(CultureManager.getPageDisplay(c.getId()));
            } else if(JsonProfileBridge.getUnlockedCultures(player).contains(c.getName().toLowerCase()) && !c.getDisabledWorlds().contains(player.getWorld().getName())) {
                unlockedLockedCulturePages.add(CultureManager.getPageDisplay(c.getId()));
            }
        }

        // Make unlocked cultures, which are marked as locked appear at the end of the ui book
        for(BaseComponent[] page : unlockedLockedCulturePages) {
            bookMeta.spigot().addPage(page);
        }

        book.setItemMeta(bookMeta);

        ItemStack mh = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        player.openBook(book);
        player.getInventory().setItemInMainHand(mh);
    }

    public static Inventory getLargeMenuBackground(String name) {
        Inventory ui = Bukkit.createInventory(null, 54, ChatColor.BLACK + name);

        // ui texture
//        ItemStack menuItem = new ItemStack(Material.PEONY);
//        ItemMeta menuItemMeta = menuItem.getItemMeta();
//        assert menuItemMeta != null;
//        menuItemMeta.setDisplayName(NamedTextColor.WHITE.toString());
////        menuItemMeta.setCustomModelData(19);
//        menuItem.setItemMeta(menuItemMeta);
//        ui.setItem(0, menuItem);
//
////        TODO: Implement down item and full UI
//        ItemStack menuItemDown = new ItemStack(Material.PEONY);
//        ItemMeta menuItemDownMeta = menuItemDown.getItemMeta();
//        assert menuItemDownMeta != null;
//        menuItemDownMeta.setDisplayName(NamedTextColor.WHITE.toString());
////        menuItemDownMeta.setCustomModelData(20);
//        menuItemDown.setItemMeta(menuItemDownMeta);
//        ui.setItem(8, menuItemDown);
        return ui;
    }


    public static Inventory getMenuBackground(String name) {
        Inventory ui = Bukkit.createInventory(null, 9, ChatColor.BLACK + name);

        // ui texture
        ItemStack menuItem = new ItemStack(Material.PEONY);
        ItemMeta menuItemMeta = menuItem.getItemMeta();
        assert menuItemMeta != null;
        menuItemMeta.setDisplayName(NamedTextColor.WHITE.toString());
        menuItemMeta.setCustomModelData(19);
        menuItem.setItemMeta(menuItemMeta);
        ui.setItem(0, menuItem);

//        TODO: Implement down item and full UI
        ItemStack menuItemDown = new ItemStack(Material.PEONY);
        ItemMeta menuItemDownMeta = menuItemDown.getItemMeta();
        assert menuItemDownMeta != null;
        menuItemDownMeta.setDisplayName(NamedTextColor.WHITE.toString());
        menuItemDownMeta.setCustomModelData(20);
        menuItemDown.setItemMeta(menuItemDownMeta);
        ui.setItem(8, menuItemDown);
        return ui;
    }

    public static void genericInventoryOpen(Player player) {
        JsonPlayerBridge.saveInventory(player);
        player.getInventory().clear();
    }

    public static void genericInventoryClose(Player player) {
        JsonPlayerBridge.loadInventory(player);
    }

    public static void inventoryManagement(Player player, boolean isUpdate) {
        if(!isUpdate) {
            genericInventoryOpen(player);
        }else{
            player.getInventory().clear();
        }

    }

}
