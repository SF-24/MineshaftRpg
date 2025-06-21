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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GUI {

    public GUI(Player player, int page, Inventory baseInventory, ArrayList<ItemStack> items, String guiIdentifier) {

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

            baseInventory.setItem(45,leftButton);
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

            baseInventory.setItem(53,rightButton);
        }

        for(ItemStack item : UIUtil.getPageItem(items, 1, baseInventory.getSize()-9)) {
            baseInventory.addItem(item);
        }
        player.openInventory(baseInventory);
    }

    public GUI(Player player, int page, Inventory baseInventory, ArrayList<ItemStack> items, String guiIdentifier, int leftButtonSlot, int rightButtonSlot) {

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

            baseInventory.setItem(leftButtonSlot,leftButton);
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

            baseInventory.setItem(rightButtonSlot,rightButton);
        }

        for(ItemStack item : UIUtil.getPageItem(items, 1, baseInventory.getSize()-9)) {
            baseInventory.addItem(item);
        }
        player.openInventory(baseInventory);
    }

    public GUI(Player player, int page, Inventory baseInventory, ArrayList<ItemStack> items, String guiIdentifier, int leftButtonSlot, int rightButtonSlot, ItemStack leftItem, ItemStack rightItem) {

        if(UIUtil.isPageValid(items, page-1, baseInventory.getSize()-9)) {
            NBT.modify(leftItem,nbt->{
                nbt.setInteger("Page", page);
                nbt.setString("Identifier",guiIdentifier);
            });

            baseInventory.setItem(leftButtonSlot,leftItem);
        }

        if(UIUtil.isPageValid(items, page-1, baseInventory.getSize()-9)) {
            NBT.modify(rightItem,nbt->{
                nbt.setInteger("Page", page);
                nbt.setString("Identifier",guiIdentifier);
            });

            baseInventory.setItem(rightButtonSlot,rightItem);
        }

        for(ItemStack item : UIUtil.getPageItem(items, 1, baseInventory.getSize()-9)) {
            baseInventory.addItem(item);
        }
        player.openInventory(baseInventory);
    }

}
