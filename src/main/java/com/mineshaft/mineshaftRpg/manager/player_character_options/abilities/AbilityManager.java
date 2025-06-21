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

package com.mineshaft.mineshaftRpg.manager.player_character_options.abilities;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AbilityManager {

    public static ArrayList<CustomAbilityClass> getPlayerAbilities(Player player) {
        ArrayList<CustomAbilityClass> abilities = new ArrayList<>();
        for(String ability : JsonPlayerBridge.getAbilities(player).keySet()) {
            abilities.add(getAbility(ability));
        }
        return abilities;
    }

    public static CustomAbilityClass getAbility(String abilityId) {
        for(CustomAbilityClass abilityClass : MineshaftRpg.getInstance().getCache().getAbilityCache()) {
            if(abilityClass.getId().equals(abilityId)) return abilityClass;
        }
        return null;
    }

}
