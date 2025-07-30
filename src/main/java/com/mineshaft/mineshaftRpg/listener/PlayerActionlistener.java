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

package com.mineshaft.mineshaftRpg.listener;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.spells.SpellCaster;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.ExperienceManager;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.block.BlockManager;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import com.mineshaft.mineshaftapi.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerActionlistener implements Listener {

    @EventHandler
    void onDrop(PlayerDropItemEvent e) {
        if(!e.isCancelled()) {
            Player player = e.getPlayer();
            if (!MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().hasSpellHotbar(player)) {
                if (ItemUtil.isWand(e.getItemDrop().getItemStack())) {
                    MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().deactivateSpellHotbar(player);
                    player.sendMessage("shutting wand ui");
                }
            }
        }
    }

    @EventHandler
    void onPlayerPickupExperience(PlayerExpChangeEvent e) {
        JsonPlayerBridge.addXp(e.getPlayer(), e.getAmount());
        ExperienceManager.updateXpBar(e.getPlayer());
        e.setAmount(0);
    }

    @EventHandler
    void onItemPickup(PlayerAttemptPickupItemEvent e) {
        if(MineshaftRpg.getInstance().getUiBrowsingPlayers().contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    void onHeldItemChange(PlayerItemHeldEvent e) {
        if(MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().hasSpellHotbar(e.getPlayer())) {
            ItemStack slotItem = e.getPlayer().getInventory().getItem(e.getNewSlot());
            if(slotItem!=null && slotItem.getType() != Material.AIR) {
                try {
                    NBT.get(slotItem, nbt->{
                        // try to cast spell when the item is selected
                        if(JsonPlayerBridge.getSpells(e.getPlayer()).containsKey(nbt.getString("Spell"))) {
                            SpellCaster.attemptCastSpell(e.getPlayer(),MineshaftRpg.getInstance().getCache().getAbility(nbt.getString("Spell")),true);
                        }
                    });
                } catch (NullPointerException ignored) {
                    e.getPlayer().sendMessage(Component.text("Null spell detected. Please contact the author of the plugin MineshaftRpg.", NamedTextColor.RED));
                }
            }
            e.setCancelled(true);
            if(e.getNewSlot()!=8) {
                e.getPlayer().getInventory().setHeldItemSlot(8);
            }
        }
    }

    @EventHandler
    void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (ItemUtil.isWand(e.getItem())) {
            if((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (e.getClickedBlock()==null || !BlockManager.isInteractable(e.getClickedBlock().getType()))) {
                MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().toggleSpellHotbar(player);
                e.setCancelled(true);
            } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                // TODO: cast default
            }
        }
    }

    @EventHandler
    void onDamage(EntityDamageEvent e) {
        if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && e instanceof Player player) {
            double newDamage = Math.min(e.getDamage() - MineshaftPlayerBridge.Skills.getSkillBonus(player,PlayerSkills.ACROBATICS), e.getDamage());
            e.setDamage(newDamage);
        }
    }

}
