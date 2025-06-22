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
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.player_data.CharacterCreationManager;
import com.mineshaft.mineshaftapi.dependency.world_guard.DiscoveryCategory;
import com.mineshaft.mineshaftapi.dependency.world_guard.Town;
import com.mineshaft.mineshaftapi.manager.player.json.JsonDiscoveryBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.maths.Direction2D;
import com.mineshaft.mineshaftapi.util.ui.ButtonUtil;
import com.mineshaft.mineshaftapi.util.ui.ButtonVariant;
import net.kyori.adventure.text.format.NamedTextColor;
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

        ui.setItem(1, UIButtonManager.Character.getPlayerLevelButton(player));
        ui.setItem(2, UIButtonManager.Character.getPlayerAbilityScoreItem(player));
        ui.setItem(3, UIButtonManager.Character.getSkillsItem(player));
        ui.setItem(4, UIButtonManager.Character.getAbilityItem(player));
        ui.setItem(5, UIButtonManager.Character.getCodexItem());
        ui.setItem(7, UIButtonManager.Character.getQuestItem());
        player.openInventory(ui);
    }

    public static void openAbilityUI(Player player, boolean isUpdate) {
        Inventory ui = getMediumMenuBackground("Abilities");

        ArrayList<String> included = new ArrayList<>();

        for(String abilityId : JsonPlayerBridge.getAbilities(player).keySet()) {
            if(!included.contains(abilityId)) {
                CustomAbilityClass ability = MineshaftRpg.getInstance().getCache().getAbility(abilityId);
                ui.addItem(UIButtonManager.Abilities.getAbilityItem(player, ability, true));
                included.add(abilityId);
            }
        }

        new GUI(player, 0, ui, UIButtonManager.Abilities.getAbilityItemArray(player,true),"",19,25, ButtonUtil.getArrowDirectionButton(Direction2D.LEFT, ButtonVariant.GREEN),ButtonUtil.getArrowDirectionButton(Direction2D.RIGHT,ButtonVariant.GREEN));
        inventoryManagement(player, isUpdate);
    }

    public static void openAbilityBindingUI(Player player, boolean isUpdate, CustomAbilityClass abilityClass) {
        Inventory ui = getMediumMenuBackground("Ability");

        ui.setItem(11, UIButtonManager.Abilities.getAbilityItem(player, abilityClass, false));
        ui.setItem(13, UIButtonManager.Abilities.getComboAddClickItem(player));
        ui.setItem(15, UIButtonManager.Abilities.getComboSaveItem());
        ui.setItem(16, UIButtonManager.Abilities.getComboResetItem());
//        ui.setItem(17, UIButtonManager.Abilities.getComboClearItem());

        player.openInventory(ui);
        inventoryManagement(player, isUpdate);

    }

    public static void openAbilityScoreMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Ability Scores");

        for(AbilityScores score : AbilityScores.values()) {
            ui.addItem(UIButtonManager.Character.getAbilityScoreItem(player, score));
        }
        ui.addItem(UIButtonManager.Character.getSkillPointItem(player));
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);
    }

    public static void openQuestMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Quests");
        ui.addItem(UIButtonManager.Quests.getQuestCanceller());
        ui.addItem(UIButtonManager.Quests.getQuestTracker());
        ui.addItem(UIButtonManager.Quests.getJournal());
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);
    }

    public static void openDiscoveryMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Discoveries");
        ui.addItem(UIButtonManager.Discoveries.getDiscoveryCategory(player, DiscoveryCategory.TOWN));
        ui.addItem(UIButtonManager.Discoveries.getDiscoveryCategory(player, DiscoveryCategory.MOB));
        ui.addItem(UIButtonManager.Discoveries.getDiscoveryCategory(player, DiscoveryCategory.LORE));
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);
    }

    public static void openTownRegionMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Discoveries");

        for(String region : JsonDiscoveryBridge.getDiscoveredRegions(player)) {
            ui.addItem(UIButtonManager.Discoveries.getLocationRegion(player,region));
        }
        player.openInventory(ui);

        inventoryManagement(player,isUpdate);

    }

    // Open town discovery menu
    public static void openTownDiscoveries(Player player, String region, int page, boolean isUpdate) {
        ArrayList<ItemStack> items = new ArrayList<>();

        for(Town town : JsonDiscoveryBridge.getDiscoveredTowns(player)) {
            items.add(UIButtonManager.Discoveries.getTownDiscovery(town));
        }
        new GUI(player,page,Bukkit.createInventory(null,54, NamedTextColor.BLACK+"Region"), items, region);
    }

    public static void openProfileMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Profiles");

        for(String profile : JsonProfileBridge.getProfiles(player)) {
            ui.addItem(UIButtonManager.Character.getProfileButton(player,profile));
        }

        ui.addItem(UIButtonManager.getGreenPlusButton("New Profile"));
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
        bookMeta.addPage(ChatColor.BOLD + "Select a culture: \n" +
                "Use the arrows underneath the book to select a page with your desired culture and press select.");

        ArrayList<BaseComponent[]> unlockedLockedCulturePages = new ArrayList<>();

        for(CustomCultureClass c : MineshaftRpg.getInstance().getCache().getCultureCache()) {
            Logger.logDebug("Successfull iteration");

            if(!c.getDisabledWorlds().isEmpty() && c.getDisabledWorlds().contains(player.getWorld().getName())) {
                Logger.logDebug("Detected disabled world '" + player.getWorld().getName() + "' for culture " + c.getName());
                continue;
            } else if(!c.getRequiredWorlds().isEmpty() && !c.getRequiredWorlds().contains(player.getWorld().getName())) {
                Logger.logDebug("'" + player.getWorld().getName() + "' is not a required world for culture " + c.getName());
                continue;
            }
            // Culture mechanic
            if (!c.isLocked()) {
                bookMeta.spigot().addPage(CultureManager.getPageDisplay(c.getId()));
                Logger.logDebug("Displaying culture page for: " + c.getName());
            } else if(JsonProfileBridge.getUnlockedCultures(player).contains(c.getName().toLowerCase())) {
                unlockedLockedCulturePages.add(CultureManager.getPageDisplay(c.getId()));
                Logger.logDebug("Displaying culture page for: " + c.getName());
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

//         ui texture
        ItemStack menuItem = new ItemStack(Material.PEONY);
        ItemMeta menuItemMeta = menuItem.getItemMeta();
        assert menuItemMeta != null;
        menuItemMeta.setDisplayName(NamedTextColor.WHITE+ "");
        menuItemMeta.setCustomModelData(13);
        menuItem.setItemMeta(menuItemMeta);
        ui.setItem(45, menuItem);

//        TODO: Implement down item and full UI
        ItemStack menuItemDown = new ItemStack(Material.PEONY);
        ItemMeta menuItemDownMeta = menuItemDown.getItemMeta();
        assert menuItemDownMeta != null;
        menuItemDownMeta.setDisplayName(NamedTextColor.WHITE+"");
        menuItemDownMeta.setCustomModelData(18);
        menuItemDown.setItemMeta(menuItemDownMeta);
        ui.setItem(53, menuItemDown);
        return ui;
    }

    public static Inventory getMediumMenuBackground(String name) {
        Inventory ui = Bukkit.createInventory(null, 27, ChatColor.BLACK + name);

//         ui texture
        ItemStack menuItem = new ItemStack(Material.PEONY);
        ItemMeta menuItemMeta = menuItem.getItemMeta();
        assert menuItemMeta != null;
        menuItemMeta.setDisplayName(NamedTextColor.WHITE+"");
        menuItemMeta.setCustomModelData(13);
        menuItem.setItemMeta(menuItemMeta);
        ui.setItem(18, menuItem);

//        TODO: Implement down item and full UI
        ItemStack menuItemDown = new ItemStack(Material.PEONY);
        ItemMeta menuItemDownMeta = menuItemDown.getItemMeta();
        assert menuItemDownMeta != null;
        menuItemDownMeta.setDisplayName(NamedTextColor.WHITE+"");
        menuItemDownMeta.setCustomModelData(18);
        menuItemDown.setItemMeta(menuItemDownMeta);
        ui.setItem(26, menuItemDown);
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
        menuItemMeta.setDisplayName("");
        menuItemMeta.setCustomModelData(19);
        menuItem.setItemMeta(menuItemMeta);
        ui.setItem(0, menuItem);

//        TODO: Implement down item and full UI
        ItemStack menuItemDown = new ItemStack(Material.PEONY);
        ItemMeta menuItemDownMeta = menuItemDown.getItemMeta();
        assert menuItemDownMeta != null;
        menuItemDownMeta.setDisplayName("");
        menuItemDownMeta.setCustomModelData(20);
        menuItemDown.setItemMeta(menuItemDownMeta);
        ui.setItem(8, menuItemDown);
        return ui;
    }

    public static void genericInventoryOpen(Player player) {
        JsonPlayerBridge.saveInventory(player);
        player.getInventory().clear();
        MineshaftRpg.getInstance().addUiBrowsingPlayer(player.getUniqueId());
    }

    public static void genericInventoryClose(Player player) {
        JsonPlayerBridge.loadInventory(player);
        MineshaftRpg.getInstance().removeUiBrowsingPlayer(player.getUniqueId());
    }

    public static void inventoryManagement(Player player, boolean isUpdate) {
        if(!isUpdate) {
            genericInventoryOpen(player);
        }else{
            player.getInventory().clear();
        }
        MineshaftRpg.getInstance().addUiBrowsingPlayer(player.getUniqueId());
    }

}
