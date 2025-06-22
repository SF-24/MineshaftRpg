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

import com.mineshaft.mineshaftapi.util.Logger;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomAbilityClass {

    String id = "example_ability";
    String name = "Example Ability";
    String description = "Description";
    AbilityType abilityType = AbilityType.ACTIVE_ABILITY;

    // Only work for non-passive abilities
    List<String> customEvents = List.of("event1","event2");
    List<String> hardcodedEvents = List.of("event1","event2");
    Map<String,Map<String, Double>> passiveModifiers = Map.of("passiveEvent1",Map.of("DAMAGE", 1d),"passiveEvent2",Map.of("SPEED",2d));
    List<String> hardcodedPassiveModifiers = List.of("event1","event2");
    int castCost = 10;

    Material materialIcon = Material.IRON_SWORD;
    int customModelData = 5;

    public ItemStack getIcon() {
        ItemStack item = new ItemStack(materialIcon);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setCustomModelData(customModelData);
        ArrayList<String> lore = new ArrayList<>();
        if(abilityType==null) {
            Logger.logError("Found invalid ability type for ability: " + id);
        }
        if(abilityType.equals(AbilityType.ACTIVE_ABILITY)) {
            lore.add(ChatColor.GRAY + "Passive Ability");
            lore.add("");
        }
        lore.add(ChatColor.GRAY + description);
        item.setItemMeta(itemMeta);
        return item;
    }

    public boolean isSpell() {return abilityType.equals(AbilityType.SPELL);}
    public boolean isPassive() {return abilityType.equals(AbilityType.PASSIVE_ABILITY);}

    public CustomAbilityClass self() {
        return this;
    }

}
