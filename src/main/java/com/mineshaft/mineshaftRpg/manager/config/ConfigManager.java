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

package com.mineshaft.mineshaftRpg.manager.config;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class ConfigManager {
    private static FileConfiguration config;

    public void setupConfig() {
        ConfigManager.config = MineshaftRpg.getInstance().getConfig();
        MineshaftRpg.getInstance().saveDefaultConfig();
    }

    public String getConfigLineToString(String line) {
        if(config.contains(line)) {
            return Objects.requireNonNull(config.get(line)).toString();
        }
        return null;
    }

    public Configuration getConfiguration() { return config; }

    public void ReloadConfigs() {
        config = YamlConfiguration.loadConfiguration(new File("Config.yml"));
    }

}
