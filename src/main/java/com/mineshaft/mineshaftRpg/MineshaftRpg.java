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
import com.mineshaft.mineshaftRpg.listener.*;
import com.mineshaft.mineshaftRpg.manager.config.ConfigManager;
import com.mineshaft.mineshaftRpg.manager.player_character_options.*;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class MineshaftRpg extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager();
    private final ArrayList<CustomCultureClass> customCultures = new ArrayList<>();
    private JsonCustomCultures jsonCustomCultures;
    private JsonCustomFeats jsonCustomFeats;

    public HashMap<CustomFeatClass, Boolean> featCache = new HashMap<>();

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

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new UIListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameSaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerActionlistener(), this);
        Bukkit.getPluginManager().registerEvents(new MineshaftListener(), this);
        getDataFolder().mkdirs();
        configManager.setupConfig();

        jsonCustomCultures=new JsonCustomCultures();
        jsonCustomFeats=new JsonCustomFeats();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MineshaftRpg getInstance() {
        return MineshaftRpg.getPlugin(MineshaftRpg.class);
    }

    public ConfigManager getConfigManager() {return configManager;}

    public ArrayList<CustomCultureClass> getCustomCultures() {
        return customCultures;
    }

    public void cacheCustomCulture(CustomCultureClass customCulture) {
        this.customCultures.add(customCulture);
        Logger.logInfo("Cached custom culture with id: " + customCulture.getId());
    }

    public void clearCustomCultureCache() {
        this.customCultures.clear();
    }

    public JsonCustomCultures getJsonCustomCultures() {
        return jsonCustomCultures;
    }

    public JsonCustomFeats getJsonCustomFeats() {
        return jsonCustomFeats;
    }

    public Map<CustomFeatClass, Boolean> getCustomFeats() {
        return featCache;
    }

    public void cacheCustomFeat(CustomFeatClass customFeat, boolean cultureRestricted) {
        this.featCache.put(customFeat, cultureRestricted);
    }
}
