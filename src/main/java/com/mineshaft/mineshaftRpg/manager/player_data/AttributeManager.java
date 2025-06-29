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

package com.mineshaft.mineshaftRpg.manager.player_data;

import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.entity.Player;

public class AttributeManager {


    public static int calculateAttributeModifier(Player player, String attributeName) {
        return PlayerStatManager.calculateAbilityScoreModifier(JsonPlayerBridge.getAbilityScoreValue(player, attributeName));
    }

}
