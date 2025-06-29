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

package com.mineshaft.mineshaftRpg.manager;

import com.mineshaft.mineshaftRpg.ClickCache;
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
import com.mineshaft.mineshaftapi.util.Logger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MineshaftCache {

    public JsonCustomCultures jsonCustomCultures;

    public JsonCustomFeats jsonCustomFeats;
    public JsonCustomAbilities jsonCustomAbilities;
    public JsonLevellingRewards jsonLevellingRewards;
    public JsonCustomBackgrounds jsonCustomBackgrounds;

    private final ArrayList<CustomCultureClass> customCultureCache = new ArrayList<>();
    private final HashMap<CustomFeatClass, Boolean> featCache = new HashMap<>();
    private final ArrayList<CustomAbilityClass> abilityCache = new ArrayList<>();
    private final ArrayList<LevellingRewardClass> levellingRewardCache = new ArrayList<>();
    private final HashMap<CustomBackgroundClass, Boolean> backgroundCache = new HashMap<>();

    // Click cache
    @Getter
    ClickCache clickCache = new ClickCache();

    public MineshaftCache() {
        jsonCustomCultures=new JsonCustomCultures();
        jsonCustomBackgrounds=new JsonCustomBackgrounds();
        jsonCustomAbilities=new JsonCustomAbilities();
        jsonCustomFeats=new JsonCustomFeats();
        jsonLevellingRewards=new JsonLevellingRewards();
    }

    // Cache functions

    public void cacheCustomFeat(CustomFeatClass customFeat, boolean cultureRestricted) {
        this.featCache.put(customFeat, cultureRestricted);
        Logger.logInfo("Cached custom feat with name: " + customFeat.getName());
    }

    public void cacheCustomAbility(CustomAbilityClass customAbilityClass) {
        MineshaftApi.getInstance().cacheAbility(customAbilityClass.getId());
        this.abilityCache.add(customAbilityClass);
        Logger.logInfo("Cached custom ability with id: " + customAbilityClass.getId());
    }

    public void cacheHardcodedPassiveAbility(PassiveAbilities passiveAbility) {
        MineshaftApi.getInstance().cacheAbility(passiveAbility.name().toLowerCase());
        Logger.logInfo("Cached hardcoded passive ability with id: " + passiveAbility.name());
    }


    public void cacheCustomLevellingReward(LevellingRewardClass levellingRewardClass) {
        this.levellingRewardCache.add(levellingRewardClass);
        Logger.logInfo("Cached custom levelling reward for level: " + levellingRewardClass.getLevel());
    }

    public void cacheCustomBackground(CustomBackgroundClass customBackground, boolean isCultureRestricted) {
        this.backgroundCache.put(customBackground, isCultureRestricted);
        Logger.logInfo("Cached custom background with id: " + customBackground.getId());
    }

    public void cacheCustomCulture(CustomCultureClass customCulture) {
        this.customCultureCache.add(customCulture);
        Logger.logInfo("Cached custom culture with id: " + customCulture.getId());
    }

    // Getters
    public ArrayList<CustomCultureClass> getCultureCache() {
        return customCultureCache;
    }

    public HashMap<CustomBackgroundClass, Boolean> getBackgroundCache() {return backgroundCache;}

    public Map<CustomFeatClass, Boolean> getCustomFeatCache() {
        return featCache;
    }

    public ArrayList<CustomAbilityClass> getAbilityCache() {return abilityCache;}

    public ArrayList<String> getAbilityIds() {
        ArrayList<String> abilityStrings = new ArrayList<>();
        abilityCache.forEach(customAbilityClass -> {
            abilityStrings.add(customAbilityClass.getId());
        });
        return abilityStrings;
    }

    public CustomAbilityClass getAbility(String id) {
        for(CustomAbilityClass customAbilityClass : abilityCache) {
            if(id.equalsIgnoreCase(customAbilityClass.getId())) return customAbilityClass;
        }
        return null;
    }

    public ArrayList<LevellingRewardClass> getLevellingRewardCache() {return levellingRewardCache;}

    // Reload the plugin data
    public void reloadData() {
        // Clear parent cache
        MineshaftApi.getInstance().clearAbilities();

        for(PassiveAbilities passiveAbility : PassiveAbilities.values()) {
            cacheHardcodedPassiveAbility(passiveAbility);
        }

        // Deal with own cache
        customCultureCache.clear();
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
