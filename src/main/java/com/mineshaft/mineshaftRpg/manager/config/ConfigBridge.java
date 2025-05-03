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

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ConfigBridge {

    public static List<String> getDisabledWorlds() {
        return MineshaftRpg.getInstance().getConfigManager().getConfiguration().getStringList("disabled-worlds");
    }

    public static List<?> getXpPerLevel() {
        return MineshaftRpg.getInstance().getConfigManager().getConfiguration().getList("experience-per-level");
    }

    public static int getDefaultAbilityScore() {
        return 8;
        //return MineshaftRpg.getInstance().getConfigManager().getConfiguration().getInt("default-ability-score-value");
    }

    public static int getDefaultSkillPoints() {
        return MineshaftRpg.getInstance().getConfig().getInt("default-skill-points");
    }

    public static HashMap<Integer, Integer> getMaximumAbilityScoresList() {

        HashMap<Integer, Integer> map = new HashMap<>();
        Set<String> keys = MineshaftRpg.getInstance().getConfig().getConfigurationSection("maximum-ability-score-per-level").getKeys(false);
        for(String key : keys) {
            map.put(Integer.valueOf(key),MineshaftRpg.getInstance().getConfig().getInt("maximum-ability-score-per-level."+key));
        }
        return map;
    }

    public static int getAbilityScoreCap(int level) {
        int maximum = 15;
        HashMap<Integer, Integer> map = getMaximumAbilityScoresList();
        for(int i = 1; i <= level; i++) {
            if(map.containsKey(i)) {
                maximum = map.get(i);
            }
        }
        return maximum;
    }

}
