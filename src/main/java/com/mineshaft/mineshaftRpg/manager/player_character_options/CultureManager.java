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

package com.mineshaft.mineshaftRpg.manager.player_character_options;

import com.mineshaft.mineshaftRpg.manager.MineshaftPlayerBridge;
import com.mineshaft.mineshaftRpg.manager.player_data.AbilityScores;
import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.entity.Player;

public class CultureManager {

    public static boolean isCustomCulturesInitialised() {
        try {
            Class.forName("CustomCultures");
        } catch (ClassCastException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void setCulture(Player player, String culture, boolean isCustom) {
        JsonPlayerBridge.setCharacterDataValue(player, "culture", culture);
        JsonPlayerBridge.setCharacterDataValue(player, "isCultureCustom", String.valueOf(isCustom).toLowerCase());
    }

    public static Enum<? extends Enum<?>> getCulture(Player player) {
        String culture = JsonPlayerBridge.getCharacterDataValue(player, "culture");
        if(!isCustomCulture(player)) {
            return Cultures.valueOf(culture);
        } else {
            try {
                if (CultureManager.isCustomCulturesInitialised()) {
                    return CustomCultures.CustomCulturesList.valueOf(culture);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static boolean isCustomCulture(Player player) {
        return JsonPlayerBridge.getCharacterDataValue(player,"isCultureCustom").equals("true");
    }

    public void givePlayerCulture(Player player, String cultureName, boolean isCustom) {

        if(isCustom) {
            try {
                CustomCultures.CustomCulturesList culture= CustomCultures.CustomCulturesList.valueOf(cultureName);
                for(AbilityScores element : culture.getAbilityScores().keySet()) {
                    MineshaftPlayerBridge.addAttribute(player,element,culture.getAbilityScores().get(element));
                }
                JsonPlayerBridge.addSkillPoints(player,culture.getAbilityScorePoints());
                CultureManager.setCulture(player, culture.name.toLowerCase(), true);

                JsonPlayerBridge.addWeaponProficiencies(player, culture.getWeaponProficiencies());

                CustomCultures.giveCustomCultureAbilities(player, culture);
            } catch (RuntimeException e) {
                Logger.logError("Custom culture specified is not present");
            }
        } else {
            Cultures culture= Cultures.valueOf(cultureName);
            for(AbilityScores element : culture.getAbilityScores().keySet()) {
                MineshaftPlayerBridge.addAttribute(player,element,culture.getAbilityScores().get(element));
            }
            JsonPlayerBridge.addSkillPoints(player,culture.getAbilityScorePoints());
            CultureManager.setCulture(player, culture.name.toLowerCase(), true);

            JsonPlayerBridge.addWeaponProficiencies(player, culture.getWeaponProficiencies());

            giveCustomCultureAbilities(player, culture);
        }
    }

    public static void giveCustomCultureAbilities(Player player, Cultures culture) {
        //TODO:
    }


}
