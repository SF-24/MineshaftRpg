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
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.CustomFeatClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.FeatStatus;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.FeatType;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.dependency.world_guard.DiscoveryCategory;
import com.mineshaft.mineshaftapi.dependency.world_guard.Town;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonDiscoveryBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonSettingsBridge;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.ui.ButtonType;
import com.mineshaft.mineshaftapi.util.ui.ButtonUtil;
import com.mineshaft.mineshaftapi.util.ui.ButtonVariant;
import com.mineshaft.mineshaftapi.util.ui.UIUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UIButtonManager {

    public static ItemStack getGreenPlusButton(String name) {
        ItemStack item = new ItemStack(Material.PEONY);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        itemMeta.setDisplayName(ChatColor.WHITE + name);
        itemMeta.setCustomModelData(ButtonType.PLUS.getCustomModelData(ButtonVariant.GREEN));

        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Spells
     * */
    public static class Spells {

        public static ItemStack getSpellItem(Player player, CustomAbilityClass customAbilityClass, boolean slotInfo) {
            ItemStack itemStack = Abilities.getAbilityItem(player, customAbilityClass, false);
            if(slotInfo) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                ArrayList<String> lore = (ArrayList<String>) itemMeta.getLore();
                if (lore==null) lore = new ArrayList<>();

                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        }

        public static ItemStack getSpellItem(Player player, int hotbar, int slot, boolean slotInfo) {
            String spell = JsonSettingsBridge.getSpell(player,hotbar,slot);
            return getSpellItem(player, MineshaftRpg.getInstance().getCache().getAbility(spell), slotInfo);
        }

        public static ItemStack getHotbarItem(Player player) {
            return ButtonUtil.getButton(ButtonType.REFRESH,ButtonVariant.YELLOW,"Current hotbar: " + MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellCache().getEditedSpellHotbar(player),List.of(ChatColor.GRAY + "Click to unbind spell"),"spellUnbind");
        }

        public static ItemStack getHotbarDownItem() {
            return ButtonUtil.getButton(ButtonType.ARROW_DOWN,ButtonVariant.GREEN,"Down",new ArrayList<>(),"spellHotbarDown");
        }

        public static ItemStack getHotbarUpItem() {
            return ButtonUtil.getButton(ButtonType.ARROW_UP,ButtonVariant.GREEN,"Up",new ArrayList<>(),"spellHotbarUp");
        }

        public static ItemStack getBackItem() {
            return ButtonUtil.getButton(ButtonType.TICK,ButtonVariant.GREEN,"Back",new ArrayList<>(),"spellUiClose");
        }

        public static ItemStack getSpellBindToSlotItem(Player player, int hotbar, int slot, String spellName) {
            ItemStack button;
            if(JsonSettingsBridge.getSpell(player,hotbar,slot) != null) {
                CustomAbilityClass spell = MineshaftRpg.getInstance().getCache().getAbility(JsonSettingsBridge.getSpell(player,hotbar,slot));
                button = ButtonUtil.getButton(ButtonType.PLUS,ButtonVariant.YELLOW,"Click to bind to slot " + slot, List.of(ChatColor.GRAY + "Current spell: " + spell.getName()),"bindSpell");
            } else {
                button = ButtonUtil.getButton(ButtonType.PLUS,ButtonVariant.GREEN,"Click to bind to slot " + slot, List.of(ChatColor.GRAY + "No bound spell"),"bindSpell");
            }
            NBT.modify(button, nbt->{
                nbt.setInteger("slot",slot);
                nbt.setInteger("hotbar",hotbar);
                nbt.setString("ability",spellName);
            });
            return button;
        }
    }

    /**
     * Abilities
     */
    public static class Abilities {
        public static ArrayList<ItemStack> getAbilityItemArray(Player player, ArrayList<CustomAbilityClass> abilityList, boolean onClick) {
            ArrayList<ItemStack> itemArray = new ArrayList<>();
            for (CustomAbilityClass ability : abilityList) {
                if(ability==null) {
                    Logger.logError("Found null ability");
                    continue;
                }
                itemArray.add(getAbilityItem(player, MineshaftRpg.getInstance().getCache().getAbility(ability.getId()),onClick));
            }
            return itemArray;
        }

        public static ItemStack getComboResetItem() {
            return ButtonUtil.getButton(ButtonType.REFRESH, ButtonVariant.YELLOW, "Reset Clicks", new ArrayList<>(), "reset_combo");
        }

        public static ItemStack getComboClearItem() {
            return ButtonUtil.getButton(ButtonType.PLUS, ButtonVariant.RED, "Clear All Combos", new ArrayList<>(), "delete_combo");
        }

        public static ItemStack getComboSaveItem() {
            return ButtonUtil.getButton(ButtonType.TICK, ButtonVariant.GREEN, "Save Clicks", new ArrayList<>(), "save_combo");
        }

        public static ItemStack getComboAddClickItem(Player player) {
            ArrayList<String> lore = MineshaftRpg.getInstance().getCache().getPlayerCache().getClickCache().getSettingClicksAsStringList(player,ChatColor.GRAY.toString(),"Click");
            return ButtonUtil.getButton(ButtonType.PLUS, ButtonVariant.GREEN, "Add Clicks", lore, "add_click_to_combo");
        }

        // TODO: Add more ability options
        /**
         * @param ability determines the display properties of the button
         * @param onClick whether the ability ui is opened when this button is clicked
         * */
        public static ItemStack getAbilityItem(Player player, CustomAbilityClass ability, boolean onClick) {
            // TODO: Add icon option
            if(ability==null) {
                Logger.logError("Null ability found");
                return new ItemStack(Material.BARRIER);
            }
            ItemStack abilityItem = new ItemStack(ability.getIcon().getType(),1);
            ItemMeta abilityItemMeta = abilityItem.getItemMeta();
            assert abilityItemMeta != null;
            abilityItemMeta.setCustomModelData(ability.getCustomModelData());
            abilityItemMeta.setDisplayName(ChatColor.WHITE + ability.getName());

            ArrayList<String> lore = new ArrayList<>();

            if(ability.getAbilityType().equals(AbilityType.ACTIVE_ABILITY)) {
                for (String abilityIteration : JsonSettingsBridge.getAbilities(player).keySet()) {
                    if (abilityIteration.equalsIgnoreCase(ability.getId())) {
                        lore.add(ChatColor.GRAY + JsonSettingsBridge.getAbilities(player).get(abilityIteration));
                    }
                }
            }
            if(ability.getAbilityType().equals(AbilityType.SPELL)) {
                lore.add(ChatColor.GRAY + "Level " + JsonPlayerBridge.getSpellClass(player,ability.getId()).getLevel());

                if (JsonSettingsBridge.getSpellHotbar(player,ability.getId())>=0) {
                    lore.add(ChatColor.GRAY + "Hotbar " + JsonSettingsBridge.getSpellHotbar(player, ability.getId()));
                    lore.add(ChatColor.GRAY + "Slot " + JsonSettingsBridge.getSpellSlot(player, ability.getId()));
                } else {
                    lore.add(ChatColor.GRAY + "Unbound");
                }
            }
            abilityItemMeta.setLore(lore);
            abilityItem.setItemMeta(abilityItemMeta);

            NBT.modify(abilityItem, nbt -> {
                switch (ability.getAbilityType()) {
                    case ACTIVE_ABILITY -> {
                        if(onClick) nbt.setString("onClick", "ability");
                        nbt.setString("ability", ability.getId());
                    }
                    case PASSIVE_ABILITY -> {
                        if(onClick) nbt.setString("onClick", "passiveAbility");
                        nbt.setString("passiveAbility", ability.getId());

                    }
                    case SPELL -> {
                        if(onClick) nbt.setString("onClick", "spell");
                        nbt.setString("spell", ability.getId());

                    }
                }
            });
            return abilityItem;
        }
    }

    // Everything related to feats
    public static class Virtues {

        public static ItemStack getFeatItem(CustomFeatClass featClass) {
            return getFeatItem(featClass,null);
        }

        public static ItemStack getFeatItem(CustomFeatClass featClass, Player player) {
            FeatStatus featStatus;
            if(featClass.hasFeat(player)) {
                featStatus=FeatStatus.OWNED;
            } else if(featClass.canPickFeat(player)) {
                featStatus=FeatStatus.AVAILABLE;
            } else {
                featStatus=FeatStatus.LOCKED;
            }

            // Set item and its meta
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();

            // Name
            meta.setDisplayName(ChatColor.WHITE + featClass.getName());

            ArrayList lore = new ArrayList();

            // Feat Type:
            if(featClass.getFeatType()==FeatType.CULTURAL_FEAT && !featClass.getCultures().isEmpty()) {
                StringBuilder cultures = new StringBuilder();
                for(String ancestryId : featClass.getCultures()) {
                    if(ancestryId==null) continue;
                    if(!cultures.isEmpty()) {
                        cultures.append(", ");
                    }
                    cultures.append(CultureManager.getCustomCulture(ancestryId).getName());
                }
                lore.add(ChatColor.GRAY + featClass.getFeatType().getName().toUpperCase(Locale.ROOT) + " (" + cultures + ")");
            } else {
                lore.add(ChatColor.GRAY + featClass.getFeatType().getName().toUpperCase(Locale.ROOT));
            }

            // Desc.
            lore.add(ChatColor.GRAY + featClass.getDescription());

            lore.add(" ");

            // ASIs
            for(AbilityScores asi :  featClass.getAbilityScoreIncreases().keySet()) {
                lore.add(asi.getColour() + asi.getName() + " " + asi.getDarkerColour() + featClass.getAbilityScoreIncreases().get(asi));
            }

            // Abilities
            if(!featClass.getAbilities().isEmpty()) {
                lore.add(ChatColor.GRAY + "Abilities:");
                for(String abilityId : featClass.getAbilities()) {
                    try {
                        CustomAbilityClass abilityClass = MineshaftRpg.getInstance().getCache().getAbility(abilityId);
                        lore.add(ChatColor.GRAY + "- " + abilityClass.getName());
                    } catch (NullPointerException ignored) {
                        Logger.logWarning("Detected nonexistent ability: '" + abilityId + "' in feat: '" + featClass.getId() + "'");
                    }
                }
            }

            //

            lore.add(" ");

            // Requirements
            if(JsonPlayerBridge.getLevel(player)<featClass.getMinimumLevel()) {
                lore.add(ChatColor.RED + "Requires level: " + featClass.getMinimumLevel());
            } else if(featClass.getMinimumLevel()>1) {
                lore.add(ChatColor.WHITE + "Requires level: " + featClass.getMinimumLevel());
            }
            for(AbilityScores asr : featClass.getMinimumAbilityScores().keySet()) {
                if(JsonPlayerBridge.getAbilityScoreValue(player,asr.name())>=featClass.getMinimumAbilityScores().get(asr)) {
                    lore.add(ChatColor.WHITE + "Requires " + asr.getDarkerColour() + asr.getName() + " " + ChatColor.WHITE + featClass.getMinimumAbilityScores().get(asr));
                } else {
                    lore.add(ChatColor.RED + "Requires " + asr.getDarkerColour() + asr.getName() + " " + ChatColor.RED + featClass.getMinimumAbilityScores().get(asr));
                }
            }

            switch (featStatus) {
                case OWNED -> {
                    lore.add(ChatColor.DARK_GREEN + "Unlocked");
                    meta.setCustomModelData(1);
                }
                case AVAILABLE -> {
                    if(MineshaftPlayerBridge.Feats.getFeatPoints(player)>0) {
                        lore.add(ChatColor.GOLD + "Click to unlock");
                    } else if(featClass.getFeatType()== FeatType.CULTURAL_FEAT && MineshaftPlayerBridge.Feats.getCultureFeatPoints(player)>0) {
                        lore.add(ChatColor.GOLD + "Click to unlock");
                    } else {
                        lore.add(ChatColor.RED + "No feat choices remaining.");
                    }
                }
                case LOCKED -> {
                    lore.add(ChatColor.RED + "Locked!");
                    meta.setCustomModelData(2);
                }
            }


            meta.setLore(lore);

            item.setItemMeta(meta);

            // Set nbt
            NBT.modify(item, nbt -> {
                nbt.setString("feat", featClass.getId());
            });
            return item;
        }
    }

    public static class Character {

        public static ItemStack getProfileButton(Player player,String profile) {

            ItemStack item = new ItemStack(Material.IRON_SWORD);
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;

            CustomCultureClass cultureClass = CultureManager.getCustomCulture(JsonPlayerBridge.getJsonPlayerManager(player, profile).getCharacterDataValue("culture"));

            String culture;
            if(cultureClass != null) {
                culture=cultureClass.getName();
            } else {
                culture="No culture selected";
            }
            if(CultureManager.hasSubCulture(player)) {
                culture+=CultureManager.getCustomCulture(CultureManager.getSubCulture(player)).getName() + " ";
            }

            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + culture + ", Level " + JsonPlayerBridge.getLevel(player));
            lore.add("");
            lore.addAll(MineshaftPlayerBridge.Attributes.getAbilityScoreStrings(player));

            itemMeta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(NamespacedKey.minecraft("dummy"), 0.0, AttributeModifier.Operation.ADD_NUMBER));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            itemMeta.setDisplayName(ChatColor.WHITE + profile);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            item = UIUtil.setOnclick(item, profile);
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
            levelItemLore.add(ChatColor.GRAY + "Armour Class " + ChatColor.GREEN + (int) PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player));
            levelItemLore.add("");
            levelItemLore.add(ChatColor.GRAY + "Click to open profile menu");
            levelItemMeta.setLore(levelItemLore);
            levelItem.setItemMeta(levelItemMeta);
            NBT.modify(levelItem, nbt -> {
                nbt.setString("onClick", "profile_menu");
            });

            return levelItem;
        }

        // Virtues, I.E. feats. Opened from the main menu
        public static ItemStack getVirtueItem(Player player) {
            ItemStack featItem = new ItemStack(Material.KNOWLEDGE_BOOK);
            ItemMeta featItemMeta = featItem.getItemMeta();
            assert featItemMeta != null;
            featItemMeta.setDisplayName(ChatColor.WHITE + "Feats");
            List<String> lore = List.of(
                    ChatColor.GOLD + String.valueOf(MineshaftPlayerBridge.Feats.getFeatPoints(player)) + ChatColor.GRAY + " virtue points remaining",
                    ChatColor.GRAY + "Click to view virtues"
            );
            featItemMeta.setLore(lore);
            featItem.setItemMeta(featItemMeta);
            UIUtil.setOnclick(featItem,"virtues");
            return featItem;
        }

        public static ItemStack getAbilityItem(Player player) {
            ItemStack abilityItem = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta abilityItemMeta = abilityItem.getItemMeta();
            assert abilityItemMeta != null;
            abilityItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Abilities");
            if (JsonPlayerBridge.getAbilities(player).isEmpty() && JsonPlayerBridge.getPassiveAbilities(player).isEmpty()) {
                abilityItemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "None"));
            } else {
                abilityItemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Left click for active abilities. Right click for passive abilities"));
            }
            abilityItem.setItemMeta(abilityItemMeta);
            NBT.modify(abilityItem, nbt -> {
                nbt.setString("onClick", "abilities");
            });
            return abilityItem;
        }

        public static ItemStack getSpellItem(Player player) {
            ItemStack abilityItem = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta abilityItemMeta = abilityItem.getItemMeta();
            assert abilityItemMeta != null;
            abilityItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Spells");
            if (JsonPlayerBridge.getSpells(player).isEmpty()) {
                abilityItemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "None"));
            } else {
                abilityItemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to view"));
            }
            abilityItem.setItemMeta(abilityItemMeta);
            NBT.modify(abilityItem, nbt -> {
                nbt.setString("onClick", "spells");
            });
            return abilityItem;
        }


        public static ItemStack getPlayerAbilityScoreItem(Player player) {
            // ability scores item
            ItemStack abilityScoreItem = new ItemStack((Material.PLAYER_HEAD));
            SkullMeta abilityScoreItemMeta = (SkullMeta) abilityScoreItem.getItemMeta();
            abilityScoreItemMeta.setDisplayName(ChatColor.AQUA + "Ability Scores:");

            ArrayList<String> abilityScoreItemLore = (MineshaftPlayerBridge.Attributes.getAbilityScoreStrings(player));
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

            for (PlayerSkills skill : PlayerSkills.values()) {
                AbilityScores abilityScore = AbilityScores.valueOf(ChatColor.stripColor(skill.getBaseAbilityScore()).toUpperCase());
                switch (JsonPlayerBridge.getProficiencyLevel(player, skill)) {
                    case 0 -> lore.add(abilityScore.getColour() + skill.getName() + abilityScore.getDarkerColour());
                    case 1 ->
                            lore.add(ChatColor.BOLD + abilityScore.getColour() + skill.getName() + abilityScore.getDarkerColour() + " (Proficient)");
                    case 2 ->
                            lore.add(ChatColor.BOLD + abilityScore.getColour() + skill.getName() + abilityScore.getDarkerColour() + " (Expertise)");
                }
            }

            skillsItemMeta.setLore(lore);
            skillsItemMeta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(NamespacedKey.minecraft("dummy"), 0.0, AttributeModifier.Operation.ADD_NUMBER));
            skillsItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            skillsItemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            skillsItemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            skillsItemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            skillsItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            skillsItemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            skillsItem.setItemMeta(skillsItemMeta);
            return skillsItem;
        }

        public static ItemStack getAbilityScoreItem(Player player, AbilityScores abilityScore) {
            ItemStack abilityScoreItem = new ItemStack(Material.KNOWLEDGE_BOOK);
            ItemMeta abilityScoreItemMeta = abilityScoreItem.getItemMeta();

            assert abilityScoreItemMeta != null;
            abilityScoreItemMeta.setDisplayName(ChatColor.WHITE + abilityScore.getName());
            ArrayList<String> strLore = new ArrayList<>();
            strLore.add(abilityScore.getColour() + JsonPlayerBridge.getAbilityScoreValue(player, abilityScore.name().toLowerCase()) + abilityScore.getDarkerColour() + " (" + JsonPlayerBridge.getAbilityScoreModifier(player, abilityScore.name().toLowerCase(Locale.ROOT)) + ")");

            if (JsonPlayerBridge.getAbilityScoreValue(player, abilityScore.name().toLowerCase()) >= ConfigBridge.getAbilityScoreCap(JsonPlayerBridge.getLevel(player))) {
                strLore.add(ChatColor.GOLD + "Can no longer be increased");
            } else if (JsonPlayerBridge.getSkillPoints(player) > 0) {
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


            skillPointMeta.setDisplayName(ChatColor.RED + String.valueOf(JsonPlayerBridge.getSkillPoints(player)) + ChatColor.DARK_RED + " skill point" + (JsonPlayerBridge.getSkillPoints(player) > 1 ? "s" : "") + " remaining");
            skillPointMeta.setCustomModelData(10);

            skillPointItem.setItemMeta(skillPointMeta);
            return skillPointItem;
        }

        public static ItemStack getQuestItem() {
            ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + "Quest Journal");
            meta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to open the quest journal"));
            item.setItemMeta(meta);
            NBT.modify(item, nbt->{
                nbt.setString("onClick", "quest_list");
            });
            return item;
        }

        @Deprecated
        public static ItemStack getOldQuestItem() {
            ItemStack item = new ItemStack(Material.PAPER);
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
    }

    public static class Quests {
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
    }

    public static class Discoveries {

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
                nbt.setString("OnClick","category_"+category.name().toLowerCase());
            });
            return categoryItem;
        }

        public static ItemStack getLocationRegion(Player player, String regionName) {
            ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.WHITE + regionName.replace("-"," ").replace("_"," "));
            itemMeta.setLore(List.of(
//                ChatColor.GRAY.toString() + -1 + " discovered"
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
}
