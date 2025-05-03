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

import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        if(!isUpdate) {
            genericInventoryOpen(player);
        }else{
            player.getInventory().clear();
        }
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
        JsonPlayerBridge.saveInventory(player);
        player.getInventory().clear();
    }

    public static void genericInventoryClose(Player player) {
        JsonPlayerBridge.loadInventory(player);
    }

}
