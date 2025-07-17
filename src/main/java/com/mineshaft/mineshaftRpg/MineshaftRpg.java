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

package com.mineshaft.mineshaftRpg;

import com.mineshaft.mineshaftRpg.command.*;
import com.mineshaft.mineshaftRpg.dependencies.Dependencies;
import com.mineshaft.mineshaftRpg.listener.*;
import com.mineshaft.mineshaftRpg.manager.cache.MineshaftCache;
import com.mineshaft.mineshaftRpg.manager.config.ConfigManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.JsonCustomAbilities;
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.JsonCustomBackgrounds;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.JsonCustomCultures;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.JsonCustomFeats;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.JsonLevellingRewards;
import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.util.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public final class MineshaftRpg extends JavaPlugin {

    ArrayList<UUID> openUiPlayers = new ArrayList<>();

    @Getter
    private Dependencies dependencies;

    @Getter
    private final ConfigManager configManager = new ConfigManager();

    public final MineshaftCache cache = new MineshaftCache();

    @Override
    public void onEnable() {

        Logger.log(Level.INFO, "\n" +
                "███╗░░░███╗██╗███╗░░██╗███████╗░██████╗██╗░░██╗░█████╗░███████╗████████╗   ██████╗░██████╗░░██████╗░\n" +
                "████╗░████║██║████╗░██║██╔════╝██╔════╝██║░░██║██╔══██╗██╔════╝╚══██╔══╝   ██╔══██╗██╔══██╗██╔════╝░\n" +
                "██╔████╔██║██║██╔██╗██║█████╗░░╚█████╗░███████║███████║█████╗░░░░░██║░░░   ██████╔╝██████╔╝██║░░██╗░\n" +
                "██║╚██╔╝██║██║██║╚████║██╔══╝░░░╚═══██╗██╔══██║██╔══██║██╔══╝░░░░░██║░░░   ██╔══██╗██╔═══╝░██║░░╚██╗\n" +
                "██║░╚═╝░██║██║██║░╚███║███████╗██████╔╝██║░░██║██║░░██║██║░░░░░░░░██║░░░   ██║░░██║██║░░░░░╚██████╔╝\n" +
                "╚═╝░░░░░╚═╝╚═╝╚═╝░░╚══╝╚══════╝╚═════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░░░░░░░╚═╝░░░   ╚═╝░░╚═╝╚═╝░░░░░░╚═════╝░");

        Logger.log(Level.INFO,"MineshaftRpg enabled. Using logger from MineshaftAPI");

        getCommand("experience").setExecutor(new ExperienceCommand());
        getCommand("menu").setExecutor(new MenuCommand());
        getCommand("character_creation").setExecutor(new CharacterCreationCommand());
        getCommand("character_creation").setTabCompleter(new CharacterCreationTabCompleter());
        getCommand("mineshaft_rpg").setExecutor(new MineshaftRpgCommand());
        getCommand("ability").setExecutor(new AbilityCommand());
        getCommand("ability_admin").setExecutor(new AdminAbilityCommand());
        getCommand("ability").setTabCompleter(new AbilityTabCompleter());
        getCommand("ability_admin").setTabCompleter(new AbilityTabCompleter());

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new UIListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameSaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerActionlistener(), this);
        Bukkit.getPluginManager().registerEvents(new MineshaftListener(), this);

        getDataFolder().mkdirs();
        configManager.setupConfig();

        getCache().reloadData();

        // Load energy cache
        for(Player player : Bukkit.getOnlinePlayers()) {
            MineshaftRpg.getInstance().getCache().getPlayerCache().getEnergyCache().setEnergy(player,20);
        }

        // Load dependencies
        dependencies=new Dependencies();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MineshaftRpg getInstance() {
        return MineshaftRpg.getPlugin(MineshaftRpg.class);
    }

    public JsonCustomCultures getJsonCustomCultures() {
        return cache.jsonCustomCultures;
    }

    public JsonCustomFeats getJsonCustomFeats() {
        return cache.jsonCustomFeats;
    }

    public JsonCustomAbilities getJsonCustomAbilities() {return cache.jsonCustomAbilities;}

    public JsonLevellingRewards getJsonCustomLevellingRewards() {return cache.jsonLevellingRewards;}

    public JsonCustomBackgrounds getJsonCustomBackgrounds() {return cache.jsonCustomBackgrounds;}

    // Get cache static function
    public MineshaftCache getCache() {return MineshaftRpg.getInstance().cache;}

    public static String getPluginPath() {return ProfileManager.getPluginPath();}

    // Note: may change in the future
    public static String getConfigPath() {return getPluginPath();}

    public void addUiBrowsingPlayer(UUID uuid) {openUiPlayers.add(uuid);}
    public void removeUiBrowsingPlayer(UUID uuid) {openUiPlayers.remove(uuid);}
    public ArrayList<UUID> getUiBrowsingPlayers() {return openUiPlayers;}
}
