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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomAbilityClass {

    String name = "Example Ability";
    String id = "example_ability";
    List<String> customEvents = List.of("event1","event2");
    List<String> hardcodedEvents = List.of("event1","event2");
    List<String> passiveAbilities = List.of("passive_ability_1,passive_ability_2");
    int castCost = 10;
    Material materialIcon = Material.IRON_SWORD;
    int customModelData = 5;

    public String getId() {return id;}
    public String getName() {return name;}
    public int getCastCost() {return castCost;}
    public List<String> getCustomEvents() {return customEvents;}
    public List<String> getHardcodedEvents() {return hardcodedEvents;}

    // Granted on gain
    public List<String> getPassiveAbilities() {return passiveAbilities;}

    public ItemStack getIcon() {
        ItemStack item = new ItemStack(materialIcon);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setCustomModelData(customModelData);
        item.setItemMeta(itemMeta);
        return item;
    }

}
