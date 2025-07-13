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
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.AbilityExecutor;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.ExperienceManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.nbtapi.NBT;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerActionlistener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if(!e.isCancelled()) {
            Player player = e.getPlayer();
            if (!MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().hasSpellHotbar(player)) {
                final UUID[] uuid = new UUID[1];
                try {
                    NBT.get(e.getItemDrop(), nbt -> {
                        String id = nbt.getOrDefault("uuid", "null");
                        if (id.equalsIgnoreCase("null")) return;
                        uuid[0] = UUID.fromString(id);
                    });
                } catch (Exception ignored) {
                }
                UUID uniqueId = uuid[0];

                if (ItemManager.getInteractEventsFromItem(ItemManager.getItemName(uniqueId), ActionType.RIGHT_CLICK).contains("wand")) {
                    MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().deactivateSpellHotbar(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupExperience(PlayerExpChangeEvent e) {
        JsonPlayerBridge.addXp(e.getPlayer(), e.getAmount());
        ExperienceManager.updateXpBar(e.getPlayer());
        e.setAmount(0);
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent e) {
        if(MineshaftRpg.getInstance().getUiBrowsingPlayers().contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHotbarChange(PlayerInventorySlotChangeEvent e) {
        if(MineshaftRpg.getInstance().getCache().getPlayerCache().getSpellHotbarManager().hasSpellHotbar(e.getPlayer())) {
            ItemStack slotItem = e.getPlayer().getInventory().getItem(e.getSlot());
            if(slotItem!=null && slotItem.getType()!= Material.AIR) {
                try {
                    NBT.get(slotItem, nbt->{
                        if(JsonPlayerBridge.getSpells(e.getPlayer()).containsKey(nbt.getString("Spell"))) {
                            AbilityExecutor.executeAbilityOnSelf(e.getPlayer(),MineshaftRpg.getInstance().getCache().getAbility(nbt.getString("Spell")));
                        }
                    });
                } catch (NullPointerException ignored) {
                    e.getPlayer().sendMessage(Component.text("Null spell detected. Please contact the author of the plugin MineshaftRpg.", NamedTextColor.RED));
                }
            }
        }
    }

}
