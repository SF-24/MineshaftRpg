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

import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PlayerMenuManager {

    public static void openCharacterMenu(Player player) {
        // CREATE MENU INVENTORY
        Inventory ui = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Menu");

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

        ui.setItem(1, UIButtonManager.getPlayerLevelButton(player));
        ui.setItem(2, UIButtonManager.getPlayerAbilityScoreItem(player));
        ui.setItem(3, UIButtonManager.getSkillsItem(player));
        ui.setItem(4, UIButtonManager.getAbilityItem(player));

        player.openInventory(ui);
    }

}
