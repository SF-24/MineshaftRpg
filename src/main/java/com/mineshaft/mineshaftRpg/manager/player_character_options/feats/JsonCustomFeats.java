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

package com.mineshaft.mineshaftRpg.manager.player_character_options.feats;

import com.google.gson.Gson;
import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.util.Logger;

import java.io.*;
import java.util.Objects;

public class JsonCustomFeats {


    static final String path = MineshaftRpg.getConfigPath() + File.separator + "CustomVirtues";
    static final File pathDir = new File(path);

    public JsonCustomFeats() {
        reloadData();
    }

    public void reloadData() {

        if(!pathDir.exists()) {
            pathDir.mkdirs();
        }
        try {
            for(File file : Objects.requireNonNull(pathDir.listFiles())) {
                initiateFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFile(File file) throws Exception {
        if(!file.exists()) {
            makeNewFile(file);
        }
        CustomFeatClass data = loadData(file);
        MineshaftRpg.getInstance().getCache().cacheCustomFeat(data);
    }

    public void makeExample() {
        File file = new File(path, "example_feat.json");
        if(!file.exists()) {
            makeNewFile(file);
        }
    }

    public static void makeNewFile(File file) {
        try {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        CustomFeatClass c = makeEmptyData();
        writeData(c, file);
    }

    public void saveFile(CustomFeatClass data, File file) {
        writeData(data, file);
    }


    // write data to a file
    public static void writeData(CustomFeatClass settingsData, File file) {
        Writer writer = null;
        Gson gson = new Gson();

        try {
            writer = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(writer==null) {
            Logger.logError("ERROR! Attempted writing to file \"" + file.getName() + "\" | Writer == null");
            return;
        }

        //IF WRITER IS NOT NULL
        gson.toJson(settingsData, writer);
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Logger.logError("Error in MineshaftRpg - Could not flush or close writer");
        }

        Logger.logInfo("Written json data correctly");
    }

    // make empty data file
    public static CustomFeatClass makeEmptyData() {
        return new CustomFeatClass();
    }

    //loads player json data file
    public CustomFeatClass loadData(File file) {
        Gson gson = new Gson();
        Reader reader = null;

        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(reader==null) {
            Logger.logError("Error reading file \"" + file.getName() + "\"");
            return null;
        }

        System.out.println("Reading file: " + file.getName());
        CustomFeatClass pdc = gson.fromJson(reader, CustomFeatClass.class);
        if(pdc==null) {
            Logger.logError("ERROR! CustomFeatClass is null");
        }

        assert pdc != null;
        return pdc;
    }

}
