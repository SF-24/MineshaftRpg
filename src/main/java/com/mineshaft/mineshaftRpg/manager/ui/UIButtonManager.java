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
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftRpg.manager.player_data.AttributeManager;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.world_guard.DiscoveryCategory;
import com.mineshaft.mineshaftapi.dependency.world_guard.Town;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonDiscoveryBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UIButtonManager {

    public static ItemStack getQuestTracker() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Quest Tracker");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Makes your compass point to the selected quest"));
        item.setItemMeta(meta);
        NBT.modify(item, nbt->{
            nbt.setString("onClick", "quest_tracker");
        });
        return item;
    }

    public static ItemStack getJournal() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Quest Journal");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to get a quest journal"));
        item.setItemMeta(meta);
        NBT.modify(item, nbt->{
            nbt.setString("onClick", "quest_journal");
        });
        return item;
    }

    public static ItemStack getQuestCanceller() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Cancel a Quest");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to open the quest canceller"));
        item.setItemMeta(meta);
        NBT.modify(item, nbt->{
            nbt.setString("onClick", "quest_canceller");
        });
        return item;
    }

    public static ItemStack getQuestItem() {
        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Quests");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to open the quest menu"));
        item.setItemMeta(meta);
        NBT.modify(item, nbt->{
            nbt.setString("onClick", "quest_menu");
        });
        return item;
    }

    public static ItemStack getCodexItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Discoveries");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to open your discoveries"));
        item.setItemMeta(meta);
        NBT.modify(item, nbt->{
            nbt.setString("onClick", "discovery_menu");
        });
        return item;
    }

    public static ItemStack getPlayerLevelButton(Player player) {
        // level item
        ItemStack levelItem = new ItemStack(Material.PEONY);
        ItemMeta levelItemMeta = levelItem.getItemMeta();
        assert levelItemMeta != null;
        levelItemMeta.setCustomModelData(10);
//        levelItemMeta.setItemModel();
        levelItemMeta.setDisplayName(ChatColor.WHITE + "Information:");
        ArrayList<String> levelItemLore = new ArrayList<>();
        levelItemLore.add(ChatColor.GRAY + "Name " + ChatColor.WHITE + JsonProfileBridge.getCurrentProfile(player));
        levelItemLore.add(ChatColor.GRAY + "Level " + ChatColor.AQUA + JsonPlayerBridge.getLevel(player));
        levelItemLore.add(ChatColor.GRAY + "Exp " + ChatColor.GREEN + JsonPlayerBridge.getXp(player));
        levelItemLore.add(ChatColor.GRAY + "");
        levelItemLore.add(ChatColor.GRAY + "Armour Class " + ChatColor.GREEN + (int)PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player));
        levelItemLore.add("");
        levelItemLore.add(ChatColor.GRAY + "Click to open profile menu");
        levelItemMeta.setLore(levelItemLore);
        levelItem.setItemMeta(levelItemMeta);
        NBT.modify(levelItem, nbt->{
            nbt.setString("onClick", "profile_menu");
        });

        return levelItem;
    }

    public static ItemStack getPlayerAbilityScoreItem(Player player) {
        // ability scores item
        ItemStack abilityScoreItem = new ItemStack((Material.PLAYER_HEAD));
        SkullMeta abilityScoreItemMeta = (SkullMeta) abilityScoreItem.getItemMeta();
        abilityScoreItemMeta.setDisplayName(ChatColor.AQUA + "Ability Scores:");

        ArrayList<String> abilityScoreItemLore = (MineshaftPlayerBridge.getAbilityScoreStrings(player));
        abilityScoreItemLore.add(ChatColor.WHITE.toString());
        abilityScoreItemLore.add(ChatColor.WHITE + "Skill points: " + ChatColor.GREEN + JsonPlayerBridge.getSkillPoints(player));
        abilityScoreItemLore.add("");
        abilityScoreItemLore.add(ChatColor.WHITE + "Click for more information");
        abilityScoreItemMeta.setLore(abilityScoreItemLore);
        abilityScoreItemMeta.setOwningPlayer(player);
        abilityScoreItemMeta.setCustomModelData(1);
        abilityScoreItem.setItemMeta(abilityScoreItemMeta);
        UIUtil.setOnclick(abilityScoreItem, "ability_scores");
        return abilityScoreItem;
    }

    public static ItemStack getSkillsItem(Player player) {
        ItemStack skillsItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta skillsItemMeta = skillsItem.getItemMeta();
        assert skillsItemMeta != null;
        skillsItemMeta.setDisplayName(ChatColor.WHITE + "Skills");

        ArrayList<String> lore = new ArrayList<>();

        for(PlayerSkills skill : PlayerSkills.values()) {
            switch (JsonPlayerBridge.getProficiencyLevel(player,skill)) {
                case 0 -> lore.add(AbilityScores.valueOf(skill.getBaseAbilityScore()).getColour() + skill.getName() + AbilityScores.valueOf(skill.getBaseAbilityScore()).getDarkerColour());
                case 1 -> lore.add(AbilityScores.valueOf(ChatColor.BOLD + skill.getBaseAbilityScore()).getColour() + skill.getName() + AbilityScores.valueOf(skill.getBaseAbilityScore()).getDarkerColour() + " (Proficient)");
                case 2 -> lore.add(AbilityScores.valueOf(ChatColor.BOLD + skill.getBaseAbilityScore()).getColour() + skill.getName() + AbilityScores.valueOf(skill.getBaseAbilityScore()).getDarkerColour() + " (Expertise)");
            }
        }

        skillsItemMeta.setLore(lore);
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


    public static ItemStack getAbilityScoreItem(Player player, AbilityScores abilityScore) {
        ItemStack abilityScoreItem = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta abilityScoreItemMeta = abilityScoreItem.getItemMeta();

        assert abilityScoreItemMeta != null;
        abilityScoreItemMeta.setDisplayName(ChatColor.WHITE + abilityScore.getName());
        ArrayList<String> strLore = new ArrayList<>();
        strLore.add(abilityScore.getColour() + JsonPlayerBridge.getAttribute(player, abilityScore.name().toLowerCase()) + abilityScore.getDarkerColour() +" (" + AttributeManager.calculateAttributeModifier(player, abilityScore.name().toLowerCase(Locale.ROOT)) + ")");

        if(JsonPlayerBridge.getAttribute(player, abilityScore.name().toLowerCase())>= ConfigBridge.getAbilityScoreCap(JsonPlayerBridge.getLevel(player))) {
            strLore.add(ChatColor.GOLD + "Can no longer be increased");
        } else if (JsonPlayerBridge.getSkillPoints(player) > 0){
            strLore.add(ChatColor.YELLOW + "Click to increase");
        } else {
            strLore.add(ChatColor.RED + "Not enough skill points to increase");
        }
        abilityScoreItemMeta.setLore(strLore);

        abilityScoreItem.setItemMeta(abilityScoreItemMeta);

        UIUtil.setOnclick(abilityScoreItem, abilityScore.name().toLowerCase());
        return abilityScoreItem;
    }

    public static ItemStack getSkillPointItem(Player player) {
        ItemStack skillPointItem = new ItemStack(Material.PEONY);
        ItemMeta skillPointMeta = skillPointItem.getItemMeta();
        assert skillPointMeta != null;


        skillPointMeta.setDisplayName(ChatColor.RED + String.valueOf(JsonPlayerBridge.getSkillPoints(player)) + ChatColor.DARK_RED + " skill point" + (JsonPlayerBridge.getSkillPoints(player)>1?"s":"") + " remaining");
        skillPointMeta.setCustomModelData(10);

        skillPointItem.setItemMeta(skillPointMeta);
        return skillPointItem;
    }

    public static ItemStack getPlusButton(String name) {
        ItemStack item = new ItemStack(Material.PEONY);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        itemMeta.setDisplayName(ChatColor.WHITE + name);
        itemMeta.setCustomModelData(26);

        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getProfileButton(Player player,String profile) {

        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        String culture = JsonPlayerBridge.getJsonPlayerManager(player,profile).getCharacterDataValue("culture");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + culture);
        lore.add("");
        lore.addAll(MineshaftPlayerBridge.getAbilityScoreStrings(player));

        itemMeta.setDisplayName(ChatColor.WHITE + profile);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        item = UIUtil.setOnclick(item, profile);
        return item;
    }

    public static ItemStack getDiscoveryCategory(Player player, DiscoveryCategory category) {
        ItemStack categoryItem = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta categoryItemMeta = categoryItem.getItemMeta();
        categoryItemMeta.setDisplayName(ChatColor.WHITE + category.getName());

        switch (category) {
            case TOWN -> {
                int discoveries= JsonDiscoveryBridge.getDiscoveredTowns(player).size();

                categoryItemMeta.setLore(List.of(
                    ChatColor.GRAY.toString() + discoveries + " discovered"
                ));
            }
            case MOB -> {
                // TODO: add mob amount calculation
                int discoveries = 0;

                categoryItemMeta.setLore(List.of(
                        ChatColor.GRAY.toString() + discoveries + " discovered"
                ));
            }
            case LORE -> {
                // TODO: Add lore amount calculation
                int discoveries = 0;

                categoryItemMeta.setLore(List.of(
                        ChatColor.GRAY.toString() + discoveries + " discovered"
                ));
            }
        }
        categoryItem.setItemMeta(categoryItemMeta);

        // Set category id
        NBT.modify(categoryItem, nbt->{
            nbt.setString("Category",category.name().toLowerCase());
        });
        return categoryItem;
    }

    public static ItemStack getLocationRegion(Player player, String regionName) {
        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + regionName.replace("-"," ").replace("_"," "));
        itemMeta.setLore(List.of(
                ChatColor.GRAY.toString() + -1 + " discovered"
        ));
        item.setItemMeta(itemMeta);

        // Set category id
        NBT.modify(item, nbt->{
            nbt.setString("Region",regionName.toLowerCase());
        });
        return item;
    }

    public static ItemStack getTownDiscovery(Town town) {
        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + town.getName());

        item.setItemMeta(itemMeta);
        return item;
    }
}
