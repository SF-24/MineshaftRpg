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

import com.mineshaft.mineshaftRpg.manager.player_data.AttributeManager;
import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;

public class UIButtonManager {

    public static ItemStack getPlayerLevelButton(Player player) {
        // level item
        ItemStack levelItem = new ItemStack(Material.PEONY);
        ItemMeta levelItemMeta = levelItem.getItemMeta();
        assert levelItemMeta != null;
        levelItemMeta.setCustomModelData(10);
        levelItemMeta.setDisplayName(ChatColor.WHITE + "Information:");
        ArrayList<String> levelItemLore = new ArrayList<>();
        levelItemLore.add(ChatColor.GRAY + "Level " + ChatColor.AQUA + JsonPlayerBridge.getLevel(player));
        levelItemLore.add(ChatColor.GRAY + "Exp " + ChatColor.GREEN + JsonPlayerBridge.getXp(player));
        levelItemMeta.setLore(levelItemLore);
        levelItem.setItemMeta(levelItemMeta);
        return levelItem;
    }

    public static ItemStack getPlayerAbilityScoreItem(Player player) {
        // ability scores item
        ItemStack abilityScoreItem = new ItemStack((Material.PLAYER_HEAD));
        SkullMeta abilityScoreItemMeta = (SkullMeta) abilityScoreItem.getItemMeta();
        abilityScoreItemMeta.setDisplayName(ChatColor.AQUA + "Ability Scores:");
        ArrayList<String> abilityScoreItemLore = new ArrayList<>();
        abilityScoreItemLore.add(ChatColor.WHITE + "STR: " + ChatColor.RED + JsonPlayerBridge.getAttribute(player,"str") + ChatColor.DARK_RED +" (" + AttributeManager.calculateAttributeModifier(player, "str") + ")");
        abilityScoreItemLore.add(ChatColor.WHITE + "DEX: " + ChatColor.RED + JsonPlayerBridge.getAttribute(player,"dex") + ChatColor.DARK_RED +" (" + AttributeManager.calculateAttributeModifier(player, "dex") + ")");
        abilityScoreItemLore.add(ChatColor.WHITE + "CON: " + ChatColor.RED + JsonPlayerBridge.getAttribute(player,"con") + ChatColor.DARK_RED +" (" + AttributeManager.calculateAttributeModifier(player, "con") + ")");
        abilityScoreItemLore.add(ChatColor.WHITE + "INT: " + ChatColor.AQUA + JsonPlayerBridge.getAttribute(player,"int") + ChatColor.BLUE +" (" + AttributeManager.calculateAttributeModifier(player, "int") + ")");
        abilityScoreItemLore.add(ChatColor.WHITE + "WIS: " + ChatColor.AQUA + JsonPlayerBridge.getAttribute(player,"wis") + ChatColor.BLUE +" (" + AttributeManager.calculateAttributeModifier(player, "wis") + ")");
        abilityScoreItemLore.add(ChatColor.WHITE + "CHA: " + ChatColor.AQUA + JsonPlayerBridge.getAttribute(player,"cha") + ChatColor.BLUE +" (" + AttributeManager.calculateAttributeModifier(player, "cha") + ")");
        abilityScoreItemLore.add(ChatColor.WHITE.toString());
        abilityScoreItemLore.add(ChatColor.WHITE + "Skill points: " + ChatColor.GREEN + JsonPlayerBridge.getSkillPoints(player));
        abilityScoreItemLore.add("");
        abilityScoreItemLore.add(ChatColor.WHITE + "Click for more information");
        abilityScoreItemMeta.setLore(abilityScoreItemLore);
        abilityScoreItemMeta.setOwningPlayer(player);
        abilityScoreItemMeta.setCustomModelData(1);
        abilityScoreItem.setItemMeta(abilityScoreItemMeta);
        return abilityScoreItem;
    }

    public static ItemStack getSkillsItem(Player player) {
        ItemStack skillsItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta skillsItemMeta = skillsItem.getItemMeta();
        assert skillsItemMeta != null;
        skillsItemMeta.setDisplayName(ChatColor.WHITE + "Skills");
        skillsItemMeta.setLore(Collections.singletonList(ChatColor.RED + "Locked"));
        skillsItem.setItemMeta(skillsItemMeta);
        return skillsItem;
    }

    public static ItemStack getAbilityItem(Player player) {
        ItemStack abilityItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta abilityItemMeta = abilityItem.getItemMeta();
        assert abilityItemMeta != null;
        abilityItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Abilities");
        abilityItemMeta.setLore(Collections.singletonList(ChatColor.RED + "Locked"));
        abilityItem.setItemMeta(abilityItemMeta);
        return abilityItem;
    }
}
