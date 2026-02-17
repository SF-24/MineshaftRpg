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

package com.mineshaft.mineshaftRpg.manager;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.config.ConfigBridge;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CultureManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.ExperienceManager;
import com.mineshaft.mineshaftRpg.manager.player_data.CharacterCreationManager;
import com.mineshaft.mineshaftRpg.manager.ui.PlayerMenuManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerCharacterManager {

    public static void initialiseCharacter(Player player) {
        if(player==null) return;

        // Opens character creation menu if not created
        if(JsonProfileBridge.getCurrentProfile(player).equals("Default")) {
            // TODO: Create Profile
            if (!ConfigBridge.getDisabledWorlds().contains(player.getWorld().getName())) {
                PlayerMenuManager.Profile.openProfileMenu(player, true);
            }
        }

        // Sets default data, if not set
        CharacterCreationManager.setDefaultData(player);

        // Updates xp bar
        ExperienceManager.updateXpBar(player);

        // Loads data
        JsonPlayerBridge.loadInventory(player);
        JsonPlayerBridge.loadLocation(player);
        JsonPlayerBridge.loadEffects(player);

        // Culture test, finishes character creation
        if(!CultureManager.hasCulture(player)) {
            PlayerMenuManager.Profile.openSpeciesSelector(player);
        } else if(!CultureManager.Items.hasCultureStartingItems(player)) {
            CultureManager.Items.giveCultureStartingItems(player,CultureManager.getCulture(player));
        }
        if(CultureManager.hasCulture(player)) {
            if(!CultureManager.hasSubCulture(player)) {
                if(!CultureManager.getCustomCulture(CultureManager.getCulture(player)).getSubcultureClasses().isEmpty()) {
                    PlayerMenuManager.Profile.openSubspeciesSelector(player,CultureManager.getCustomCulture(CultureManager.getCulture(player)).getSubcultureClasses());
                }
            }

            if((!CultureManager.hasSubCulture(player) || CultureManager.getCustomCulture(CultureManager.getCulture(player)).getSubcultureClasses().isEmpty()) && !MineshaftPlayerBridge.Backgrounds.hasBackground(player)) {
                PlayerMenuManager.Profile.openBackgroundSelector(player);
            }
        }
        if(MineshaftPlayerBridge.Backgrounds.hasBackground(player) && !MineshaftPlayerBridge.Backgrounds.hasBackgroundAbilityScores(player)) {
            PlayerMenuManager.Profile.openBackgroundAsiSelector(player);
        }
    }

    public static void setProfile(Player player, String profile) {
        if(profile.contains("§")) {
            player.closeInventory();
            player.sendMessage("Profile name contains invalid characters");
            return;
        }
        player.closeInventory();

        Bukkit.getScheduler().runTaskLaterAsynchronously(MineshaftRpg.getInstance(), () -> {
            // Save user daya
            MineshaftPlayerBridge.savePlayerData(player);

            // Clear inventory
            player.getInventory().clear();

            // Set Profile
            JsonProfileBridge.setCurrentProfile(player,profile);

            // Initialise user data
            initialiseCharacter(player);

            // Notify player
            player.sendMessage(ChatColor.AQUA + "Profile loaded: " + ChatColor.GOLD + profile);
        },1/40);


    }

}
