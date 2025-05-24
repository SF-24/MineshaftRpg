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

import com.google.gson.Gson;
import com.mineshaft.mineshaftRpg.MineshaftRpg;
import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.util.Logger;

import java.io.*;
import java.util.Objects;

public class JsonCustomCultures {

    static final String path = ProfileManager.getPluginPath() + File.separator + "CustomCultures";
    static final File pathDir = new File(path);

    public JsonCustomCultures() {
        reloadData();
    }

    public void reloadData() {
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
            MineshaftRpg.getInstance().cacheCustomCulture(loadData(file));
        }
    }

    public void makeExample() {
        File file = new File(pathDir, "example_culture.json");
        if(!file.exists()) {
            file.mkdirs();
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

        CustomCultureClass CustomCultureClass = makeEmptyData();
        writeData(CustomCultureClass, file);
    }


    // write data to a file
    public static void writeData(CustomCultureClass settingsData, File file) {
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
    public static CustomCultureClass makeEmptyData() {
        return new CustomCultureClass();
    }

    //loads player json data file
    public CustomCultureClass loadData(File file) {
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

        CustomCultureClass pdc = gson.fromJson(reader, CustomCultureClass.class);
        if(pdc==null) {
            Logger.logError("ERROR! CustomCultureClass is null");
        }

        assert pdc != null;
        return pdc;
    }
}
