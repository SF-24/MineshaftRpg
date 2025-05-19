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
import com.mineshaft.mineshaftRpg.manager.player_character_options.Cultures;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.player_data.CharacterCreationManager;
import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.json.JsonProfileBridge;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class PlayerMenuManager {

    public static void openCharacterMenu(Player player) {
        genericInventoryOpen(player);
        // CREATE MENU INVENTORY
        Inventory ui = getMenuBackground("Menu");

        ui.setItem(1, UIButtonManager.getPlayerLevelButton(player));
        ui.setItem(2, UIButtonManager.getPlayerAbilityScoreItem(player));
        ui.setItem(3, UIButtonManager.getSkillsItem(player));
        ui.setItem(4, UIButtonManager.getAbilityItem(player));

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

    public static void openProfileMenu(Player player, boolean isUpdate) {
        Inventory ui = getMenuBackground("Profiles");

        for(String profile : JsonProfileBridge.getProfiles(player)) {
            // TODO:
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

        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        assert bookMeta != null;
        bookMeta.addPage(ChatColor.BOLD + "Select a culture: \n" +
                "Use the arrows underneath the book to select a page with your desired culture and press done.");
        for(Cultures c : Cultures.values()) {
            TextComponent hoverable = new TextComponent("§4§l" + c.getName() + "\n");
            hoverable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(c.getDescription())));
            ArrayList<TextComponent> page = new ArrayList<>();

            for(AbilityScores abilityScores : c.getAbilityScores().keySet()) {
                TextComponent desc = new TextComponent(abilityScores.getDarkerColour() + "+"  + ChatColor.WHITE + c.getAbilityScores().get(abilityScores) + " " + abilityScores.getName() + "\n");
                page.add(desc);
            }
            if(c.getAbilityScorePoints()>0) {
                page.add(new TextComponent(ChatColor.GOLD + "+" + ChatColor.WHITE + c.getAbilityScorePoints() + " Ability Score Points\n"));
                page.add(new TextComponent("\n"));
            }

            BaseComponent[] pageComp = new BaseComponent[]{};
            page.toArray(pageComp);
            bookMeta.spigot().addPage(pageComp);

            // TODO: Finish description
        }

        // TODO: Custom cultures implementation, W.I.P.
        // TODO: Add loading custom cultures from JSON config

        book.setItemMeta(bookMeta);
        player.openBook(book);
    }

    public static Inventory getMenuBackground(String name) {
        Inventory ui = Bukkit.createInventory(null, 9, ChatColor.BLACK + name);

        // ui texture
        ItemStack menuItem = new ItemStack(Material.PEONY);
        ItemMeta menuItemMeta = menuItem.getItemMeta();
        assert menuItemMeta != null;
        menuItemMeta.setDisplayName(ChatColor.WHITE.toString());
        menuItemMeta.setCustomModelData(19);
        menuItem.setItemMeta(menuItemMeta);
        ui.setItem(0, menuItem);

//        TODO: Implement down item and full UI
        ItemStack menuItemDown = new ItemStack(Material.PEONY);
        ItemMeta menuItemDownMeta = menuItemDown.getItemMeta();
        assert menuItemDownMeta != null;
        menuItemDownMeta.setDisplayName(ChatColor.WHITE.toString());
        menuItemDownMeta.setCustomModelData(20);
        menuItemDown.setItemMeta(menuItemDownMeta);
        ui.setItem(8, menuItemDown);
        return ui;
    }

    public static void genericInventoryOpen(Player player) {
        JsonPlayerBridge.setTempArmourClass(player);
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
