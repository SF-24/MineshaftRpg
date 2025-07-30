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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.AbilityExecutor;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.event.click.ClickType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonSettingsBridge;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerCache {

    HashMap<UUID, Map<String, AbilityScores>> selectedTempAbilityScore = new HashMap<>();

    @Getter
    SpellHotbarManager spellHotbarManager = new SpellHotbarManager();

    @Getter
    HashMap<UUID, Integer> playerEnergy = new HashMap<>();

    HashMap<UUID, Integer> currentlyEditedSpellHotbar = new HashMap<>();
    HashMap<UUID, Integer> jumpCache = new HashMap<>();

    HashMap<UUID, ArrayList<ClickType>> clicks = new HashMap<>();
    private final Cache<UUID, Long> activeTime = CacheBuilder.newBuilder().expireAfterWrite(2500, TimeUnit.MILLISECONDS).build();
    private final Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(100, TimeUnit.MILLISECONDS).build();

    private final HashMap<UUID, ArrayList<ClickType>> settableClicks = new HashMap<>();

    @Getter
    PlayerClicks clickCache = new PlayerClicks();
    @Getter
    SpellCache spellCache = new SpellCache();
    @Getter
    EnergyCache energyCache = new EnergyCache();

    public class PlayerClicks {
        // If the player has clicked
        public boolean hasActiveClicks(Player player) {
            if(clicks.containsKey(player.getUniqueId()) && activeTime.asMap().containsKey(player.getUniqueId())) {
                return true;
            }
            return false;
        }

        public void clearClicks(Player player) {
            activeTime.put(player.getUniqueId(), System.currentTimeMillis());
            player.sendActionBar(Component.text());
            clicks.remove(player.getUniqueId());
        }

        // Cache a combo click
        public void cacheClick(Player player, ClickType clickType) {
            UUID uuid = player.getUniqueId();
            ArrayList<ClickType> clickTypes = new ArrayList<>();
            if(hasActiveClicks(player)) {
                clickTypes = clicks.get(uuid);

                if(clickTypes.size() > 5) {
                    clickTypes = new ArrayList<>();
                }

            }
            activeTime.put(player.getUniqueId(), System.currentTimeMillis() + 2500 );
            clickTypes.add(clickType);
            clicks.put(uuid,clickTypes);

            onUpdate(player);
        }

        // Executed when the player clicks
        public void onUpdate(Player player) {
            ArrayList<ClickType> clickTypes = new ArrayList<>();

            if(clicks.containsKey(player.getUniqueId())) {
                clickTypes = clicks.get(player.getUniqueId());
            }

            // Generate string for actionbar display:
            StringBuilder actionBar = new StringBuilder();
            int elements = 0;

            for(ClickType click: clickTypes) {
                if(elements < 1) {
                    actionBar = new StringBuilder(String.valueOf(click.getAbbreviation()));
                } else {
                    actionBar.append("-").append(click.getAbbreviation());
                }
                elements++;
            }

            // Show the actionbar
            player.sendActionBar(Component.text(actionBar.toString(), NamedTextColor.GREEN));

            // Check if a valid combo has been crated
            testAbilities(player);
        }

        // Get click list. Clean if necessary
        public ArrayList<ClickType> getKeys(Player player) {
            ArrayList<ClickType> clickTypes = new ArrayList<>();

            if(hasActiveClicks(player)) {
                clickTypes = clicks.get(player.getUniqueId());
                if(clickTypes.size() > 5) {
                    clickTypes.clear();
                }
            }
            return clickTypes;
        }

        // Get the keys as a string. Used for display purposes
        public String getKeysAsString(Player player) {
            ArrayList<ClickType> clickTypes = getKeys(player);
            int elements = 0;
            StringBuilder clickList = new StringBuilder();

            for(ClickType click: clickTypes) {
                if(elements < 1) {
                    clickList = new StringBuilder(String.valueOf(click.getAbbreviation()));
                } else {
                    clickList.append(click.getAbbreviation());
                }
                elements++;
            }
            return clickList.toString();
        }


        // Test whether the combo exists
        public void testAbilities(Player player) {
            // Get the combos
            String combo = getKeysAsString(player);

            if(combo != null && JsonSettingsBridge.getAbility(player,combo) != null) {
                CustomAbilityClass ability = MineshaftRpg.getInstance().getCache().getAbility(JsonSettingsBridge.getAbility(player,combo));
                player.sendActionBar(Component.text(ability.getName(),NamedTextColor.WHITE, TextDecoration.BOLD));
                AbilityExecutor.executeAbilityOnSelf(player,ability);
                clicks.clear();
            }
        }

        /**
         * Clicks used for saving a combo in the UI
         * */

        public void addSettingClick(Player player, ClickType clickType) {
            ArrayList<ClickType> clickList = getSettingClicks(player);
            clickList.add(clickType);
            settableClicks.put(player.getUniqueId(),clickList);
        }

        public void resetSettingClicks(Player player) {
            settableClicks.put(player.getUniqueId(),new ArrayList<>());
        }

        public void saveSettingClicks(Player player, String ability) {
            JsonSettingsBridge.addAbility(player,ability,getSettingClicksAsString(player));
            settableClicks.clear();
        }

        // Get the player click array
        public ArrayList<ClickType> getSettingClicks(Player player) {
            if(settableClicks.containsKey(player.getUniqueId())) {
                return settableClicks.get(player.getUniqueId());
            }
            return new ArrayList<>();
        }

        // Get the player click array as a string
        public String getSettingClicksAsString(Player player) {

            StringBuilder value = new StringBuilder();
            if(!getSettingClicks(player).isEmpty()) {
                for(ClickType click : getSettingClicks(player)) {
                    value.append(click.getAbbreviation());
                }
                return value.toString();
            }

            return null;
        }

        // Get the player click array as a string list, for displaying in the ui
        public ArrayList<String> getSettingClicksAsStringList(Player player, String prefix, String suffix) {
            ArrayList<String> clickStrings = new ArrayList<>();

            for(ClickType click : getSettingClicks(player)) {
                clickStrings.add(prefix + " " + click.getName() + " " + suffix);
            }

            return clickStrings;
        }

        public ArrayList<String> getAbilityClicksAsString(Player player, String ability) {
            // Get the clicks for a specific ability as a string
            // Used for UI

            return null;
        }
    }

    public class SpellCache {
        public int getEditedSpellHotbar(Player player) {
            if(!currentlyEditedSpellHotbar.containsKey(player.getUniqueId())) {
                currentlyEditedSpellHotbar.put(player.getUniqueId(),0);
                return 0;
            }
            if(currentlyEditedSpellHotbar.get(player.getUniqueId())<0 || currentlyEditedSpellHotbar.get(player.getUniqueId())>2) return 0;
            return currentlyEditedSpellHotbar.get(player.getUniqueId());
        }

        public void setEditedSpellHotbar(Player player, int hotbar) {
            currentlyEditedSpellHotbar.put(player.getUniqueId(), hotbar);
        }

        public void upEditedSpellHotbar(Player player) {
            int hotbar = getEditedSpellHotbar(player)+1;
            if(hotbar>2) hotbar=0;
            currentlyEditedSpellHotbar.put(player.getUniqueId(), hotbar);
        }

        public void downEditedSpellHotbar(Player player) {
            int hotbar = getEditedSpellHotbar(player) - 1;
            if (hotbar < 0) hotbar = 2;
            currentlyEditedSpellHotbar.put(player.getUniqueId(), hotbar);
        }
    }

    public class EnergyCache {
        BukkitTask runnable;
        @Getter
        int maxEnergy = 20;

        public Integer getEnergy(Player player) {
            return playerEnergy.get(player.getUniqueId());
        }

        public void setEnergy(Player player, int energy) {
            playerEnergy.put(player.getUniqueId(), energy);
        }

        public void takeEnergy(Player player, int energy) {
            playerEnergy.put(player.getUniqueId(), Math.max(0, getEnergy(player)-energy));
        }

        public void addEnergy(Player player, int energy) {
            playerEnergy.put(player.getUniqueId(), Math.min(getEnergy(player)+energy, maxEnergy));
        }
        public boolean hasEnergy(Player player, int energy) {
            return playerEnergy.get(player.getUniqueId()) >= energy;
        }

        public void updateRegistry() {
            if(!Bukkit.getOnlinePlayers().isEmpty() && runnable != null) {
                runnable = Bukkit.getScheduler().runTaskTimerAsynchronously(MineshaftRpg.getInstance(), ()->{
                    for(Player player:Bukkit.getOnlinePlayers()) {
                        MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().addEnergy(player, 1);
                    }
                }, 0, 20);
            } else if(Bukkit.getOnlinePlayers().isEmpty()) {
                runnable.cancel();
            }
        }
    }

}
