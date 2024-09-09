package com.mineshaft.mineshaftRpg;

import com.mineshaft.mineshaftRpg.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineshaftRpg extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
