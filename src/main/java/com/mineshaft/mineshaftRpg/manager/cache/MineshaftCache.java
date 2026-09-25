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

package com.mineshaft.mineshaftRpg.manager.cache;

import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.JsonCustomAbilities;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.passive_events.PassiveAbilities;
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.CustomBackgroundClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.JsonCustomBackgrounds;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.JsonCustomCultures;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.CustomFeatClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.JsonCustomFeats;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.JsonLevellingRewards;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.LevellingRewardClass;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.util.Logger;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MineshaftCache {

    public JsonCustomCultures jsonCustomCultures;
    public JsonCustomFeats jsonCustomFeats;
    public JsonCustomAbilities jsonCustomAbilities;
    public JsonLevellingRewards jsonLevellingRewards;
    public JsonCustomBackgrounds jsonCustomBackgrounds;

    // Getters
    @Getter
    private final HashMap<String, String> cultureCache = new HashMap<>();
    @Getter
    private final HashMap<String,String> abilityCache = new HashMap<>();
    @Getter
    private final HashMap<String, String> backgroundCache = new HashMap<>();

    @Getter
    private final HashMap<List<Integer>, String> spellPatternCache = new HashMap<>();

    // TODO: Convert
    @Getter
    private final ArrayList<CustomFeatClass> featCache = new ArrayList<>();
    @Getter
    private final ArrayList<LevellingRewardClass> levellingRewardCache = new ArrayList<>();

    // Click cache
    @Getter
    PlayerCache playerCache = new PlayerCache();

    public MineshaftCache() {
        jsonCustomCultures=new JsonCustomCultures();
        jsonCustomBackgrounds=new JsonCustomBackgrounds();
        jsonCustomAbilities=new JsonCustomAbilities();
        jsonCustomFeats=new JsonCustomFeats();
        jsonLevellingRewards=new JsonLevellingRewards();
    }

    // Cache functions

    public void cacheCustomFeat(CustomFeatClass customFeat) {
        this.featCache.add(customFeat);
        Logger.logInfo("Cached custom feat with name: " + customFeat.getName());
    }

    public void cacheCustomAbility(String name, String path) {
        this.abilityCache.put(name, path);
//        MineshaftApi.getInstance().cacheAbility(customAbilityClass.getId(),customAbilityClass.getAbilityType());
//        this.abilityCache.add(customAbilityClass);
//
//        // If the spell has a pattern, cache the pattern
//        if(!customAbilityClass.getSpellPattern().isEmpty()) {
//            this.spellPatternCache.put(customAbilityClass.getSpellPattern(),customAbilityClass.getId());
//            Logger.logInfo("Cached custom ability with id: " + customAbilityClass.getId() + " with pattern " + customAbilityClass.getSpellPattern());
//        } else {
//            Logger.logInfo("Cached custom ability with id: " + customAbilityClass.getId());
//        }
    }

    public void cacheHardcodedPassiveAbility(PassiveAbilities passiveAbility) {
        MineshaftApi.getInstance().cacheAbility(passiveAbility.name().toLowerCase(), AbilityType.PASSIVE_ABILITY);
        Logger.logInfo("Cached hardcoded passive ability with id: " + passiveAbility.name());
    }


    public void cacheCustomLevellingReward(LevellingRewardClass levellingRewardClass) {
        this.levellingRewardCache.add(levellingRewardClass);
        Logger.logInfo("Cached custom levelling reward for level: " + levellingRewardClass.getLevel());
    }

    public void cacheCustomBackground(String id, String path) {
        this.backgroundCache.put(id,path);
        Logger.logInfo("Cached custom background with id: " + id);
    }

    public void cacheCustomCulture(String id, String path) {
        this.cultureCache.put(id,path);
        Logger.logInfo("Cached custom culture with id: " + id);
    }

    public ArrayList<String> getAbilityIds() {
        return new ArrayList<>(this.abilityCache.keySet());
    }

    public CustomAbilityClass getAbility(String id) {
        return jsonCustomAbilities.getFile(id,abilityCache.get(id));
    }

    public CustomBackgroundClass getBackground(String id) {
        if(id==null) return null;
        return jsonCustomBackgrounds.getFile(id,backgroundCache.get(id));
    }

    public CustomCultureClass getCulture(String id) {
        return jsonCustomCultures.getFile(id,cultureCache.get(id));
    }

    public CustomFeatClass getFeat(String id) {
        for(CustomFeatClass element : featCache) {
            if(id.equalsIgnoreCase(element.getId())) return element;
        }
        return null;
    }

    public String getAbilityId(List<Integer> pattern) {
        return spellPatternCache.get(pattern);
    }

    public CustomAbilityClass getAbility(List<Integer> pattern) {
        System.out.printf("pattern " + pattern.toString());
        if(pattern==null||pattern.isEmpty()||getAbilityId(pattern)==null||getAbilityId(pattern).isBlank()) {
            return null;
        }
        return getAbility(getAbilityId(pattern));
    }

    public ArrayList<CustomFeatClass> getApplicableFeats(Player player) {
        ArrayList<CustomFeatClass> returnFeats = new ArrayList<>();
        for(CustomFeatClass featClass : featCache) {
            if(featClass.canPickFeat(player)) {
                returnFeats.add(featClass);
            }
        }
        return returnFeats;
    }

    // Reload the plugin data
    public void reloadData() {
        // Clear parent cache
        MineshaftApi.getInstance().clearAbilities();

        for(PassiveAbilities passiveAbility : PassiveAbilities.values()) {
            cacheHardcodedPassiveAbility(passiveAbility);
        }

        // Deal with own cache
        cultureCache.clear();
        backgroundCache.clear();
        abilityCache.clear();
        featCache.clear();
        levellingRewardCache.clear();

        jsonCustomCultures.reloadData();
        jsonCustomBackgrounds.reloadData();
        jsonCustomAbilities.reloadData();
        jsonCustomFeats.reloadData();
        jsonLevellingRewards.reloadData();
    }

    public void makeExamples() {
        MineshaftRpg.getInstance().getJsonCustomCultures().makeExample();
        MineshaftRpg.getInstance().getJsonCustomFeats().makeExample();
        MineshaftRpg.getInstance().getJsonCustomAbilities().makeExample();
        MineshaftRpg.getInstance().getJsonCustomLevellingRewards().makeExample();
        MineshaftRpg.getInstance().getJsonCustomBackgrounds().makeExample();
    }
}
