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

import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.CustomAbilityClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.JsonCustomAbilities;
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.CustomBackgroundClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.backgrounds.JsonCustomBackgrounds;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.CustomCultureClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.cultures.JsonCustomCultures;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.CustomFeatClass;
import com.mineshaft.mineshaftRpg.manager.player_character_options.feats.JsonCustomFeats;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.JsonLevellingRewards;
import com.mineshaft.mineshaftRpg.manager.player_character_options.levelling.LevellingRewardClass;
import com.mineshaft.mineshaftapi.util.Logger;

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
    }

    public void cacheCustomAbility(CustomAbilityClass customAbilityClass) {
        this.abilityCache.add(customAbilityClass);
    }

    public void cacheCustomLevellingReward(LevellingRewardClass levellingRewardClass) {
        this.levellingRewardCache.add(levellingRewardClass);
    }

    public void cacheCustomBackground(CustomBackgroundClass customBackground, boolean isCultureRestricted) {
        this.backgroundCache.put(customBackground, isCultureRestricted);
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

    public ArrayList<LevellingRewardClass> getLevellingRewardCache() {return levellingRewardCache;}

    // Reload the plugin data
    public void reloadData() {
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
}
