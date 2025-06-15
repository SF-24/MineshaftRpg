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

import com.mineshaft.mineshaftapi.nbtapi.NBT;
import com.mineshaft.mineshaftapi.util.UIUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class GUI {

    public GUI(Player player, int page, Inventory baseInventory, ArrayList<ItemStack> items, String guiIdentifier) {
        Inventory ui = Bukkit.createInventory(null, baseInventory.getSize(), Component.text("Discoveries"));

        ItemStack leftButton;
        ItemMeta leftButtonMeta;

        if(UIUtil.isPageValid(items, page-1, baseInventory.getSize()-9)) {
            leftButton = new ItemStack(Material.ARROW);
            leftButtonMeta=leftButton.getItemMeta();
            leftButtonMeta.setDisplayName(ChatColor.WHITE+"Previous page");
            leftButton.setItemMeta(leftButtonMeta);

            NBT.modify(leftButton,nbt->{
                nbt.setInteger("Page", page);
                nbt.setString("Identifier",guiIdentifier);
            });

            ui.setItem(45,leftButton);
        }

        ItemStack rightButton;
        ItemMeta rightButtonMeta;

        if(UIUtil.isPageValid(items, page-1, baseInventory.getSize()-9)) {
            rightButton = new ItemStack(Material.ARROW);
            rightButtonMeta=rightButton.getItemMeta();
            rightButtonMeta.setDisplayName(ChatColor.WHITE+"Next page");
            rightButton.setItemMeta(rightButtonMeta);
            NBT.modify(rightButton,nbt->{
                nbt.setInteger("Page", page);
                nbt.setString("Identifier",guiIdentifier);
            });

            ui.setItem(53,rightButton);
        }

        for(ItemStack item : UIUtil.getPageItem(items, 1, baseInventory.getSize()-9)) {
            ui.addItem(item);
        }
        player.openInventory(ui);
    }

}
