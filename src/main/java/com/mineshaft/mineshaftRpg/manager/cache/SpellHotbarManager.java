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

package com.mineshaft.mineshaftRpg.manager.cache;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftapi.manager.player.json.JsonSettingsBridge;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import com.mineshaft.mineshaftapi.util.maths.DirectionVertical;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SpellHotbarManager {

    public HashMap<UUID, ArrayList<ItemStack>> savedItems = new HashMap<>();
    public HashMap<UUID, HotbarType> hotbarType = new HashMap<>();
    public HashMap<UUID, Integer> initialWandSlot = new HashMap<>();

    // Toggle the spell hotbar when a wand is clicked
    public void toggleSpellHotbar(Player player) {
        if(hasSpellHotbar(player)) {
            deactivateSpellHotbar(player);
        } else if(!isHotbarInUse(player)) {
            activateSpellHotbar(player);
        }
        // Otherwise the hotbar is in use another way, so return
    }

    public void activateSpellHotbar(Player player) {
        if(!hasSpellHotbar(player) && !isHotbarInUse(player)) {
            // Update the cache
            hotbarType.put(player.getUniqueId(), HotbarType.SPELL_HOTBAR);

            // Save the inventory
            MineshaftPlayerBridge.saveInventory(player);

            // Save the wand
            ItemStack wand = player.getInventory().getItemInMainHand();

            // Save the hotbar
            ArrayList<ItemStack> hotbarItems = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                hotbarItems.add(i, player.getInventory().getItem(i));
            }
            this.savedItems.put(player.getUniqueId(), hotbarItems);
            this.initialWandSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());

            // Fill the hotbar
            fillSpellHotbar(player);

            // Set the wand
            player.getInventory().setItem(8, wand);
            player.getInventory().setHeldItemSlot(8);
        } else {
            player.sendMessage("Cannot open spell hotbar");
        }
    }

    public void deactivateSpellHotbar(Player player) {
        if(hasSpellHotbar(player)) {
            // Save the wand
            ItemStack wand = player.getInventory().getItemInMainHand();

            // Get the wand slot
            int slot = 8;
            if (initialWandSlot.containsKey(player.getUniqueId())) {
                slot = initialWandSlot.get(player.getUniqueId());
            }

            // Update the cache
            hotbarType.remove(player.getUniqueId());
            hotbarType.put(player.getUniqueId(), HotbarType.DEFAULT);

            // Return saved items
            ArrayList<ItemStack> itemList = savedItems.get(player.getUniqueId());
            for (int i = 0; i < 9; i++) {
                ItemStack is = itemList.get(i);
                if(is==null || is.isEmpty()) player.getInventory().setItem(i, new ItemStack(Material.AIR));
                player.getInventory().setItem(i, is);
            }

            // Return the wand
            if (slot > -1 && slot < 9) {
                player.getInventory().setHeldItemSlot(slot);
            }
            player.getInventory().setItemInMainHand(wand);
        } else {
            player.sendMessage("Cannot shut!");
        }
    }

    // Go up or down the hotbar in a given direction
    public void changeHotBar(Player player, DirectionVertical direction) {
        if(hasSpellHotbar(player)) {
            int hotBar = JsonSettingsBridge.getCurrentSpellHotbar(player);
            int maxHotbar = getHotbarCount()-1;

            if(direction.equals(DirectionVertical.UP)) {
                hotBar++;
            } else if(direction.equals(DirectionVertical.DOWN)) {
                hotBar--;
            } else {
                return;
            }

            if (hotBar > maxHotbar) {
                hotBar = 0;
            } else if(hotBar < 0) {
                hotBar=maxHotbar;
            }
            JsonSettingsBridge.setCurrentSpellHotbar(player,hotBar);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN,1.0f,1.0f);

            fillSpellHotbar(player);
        }
    }

    public int getCurrentHotbar(Player player) {
        return JsonSettingsBridge.getCurrentSpellHotbar(player);
    }

    public boolean hasSpellHotbar(Player player) {
        if(!hotbarType.containsKey(player.getUniqueId())) return false;
        return hotbarType.get(player.getUniqueId()).equals(HotbarType.SPELL_HOTBAR);
    }

    public boolean isHotbarInUse(Player player) {
        if(!hotbarType.containsKey(player.getUniqueId())) return false;
        return !hotbarType.get(player.getUniqueId()).equals(HotbarType.DEFAULT);
    }

    public void fillSpellHotbar(Player player) {
        if(hasSpellHotbar(player)) {
            for (int i = 0; i < 8; i++) {
                player.getInventory().setItem(i, getSpellHotbarItem(player, JsonSettingsBridge.getCurrentSpellHotbar(player), i));
            }
        }
    }

    public ItemStack getSpellHotbarItem(Player player, int spellHotbar, int slot) {
        String id = JsonSettingsBridge.getSpell(player, spellHotbar, slot);

        if(id==null || id.isBlank()) {
            ItemStack blank = new ItemStack(Material.PEONY);
            ItemMeta blankMeta = blank.getItemMeta();
            blankMeta.setDisplayName(" ");
            // TODO: add ButtonUtil definition
            blankMeta.setCustomModelData(99);
            blank.setItemMeta(blankMeta);
            NBT.modify(blank, nbt->{
                nbt.setBoolean("Immutable",true);
                nbt.setString("Spell",null);
            });
            return blank;
        }

        CustomAbilityClass spellClass = MineshaftRpg.getInstance().getCache().getAbility(id);

        ItemStack item = spellClass.getIcon();
        NBT.modify(item, nbt->{
            nbt.setString("Spell",id);
        });

        return item;
    }

    public int getHotbarCount() {return 3;}
}
