package com.lekohd.shopsystem.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;


public class SettingsManager {
	static SettingsManager instance = new SettingsManager();
	Plugin p;
	FileConfiguration config;
	File cfile;
	FileConfiguration data;
	File dfile;
	

	public static SettingsManager getInstance() {
		return instance;
	}

	public void setup(Plugin p) {
		this.cfile = new File(p.getDataFolder(), "config.yml");
		this.config = p.getConfig();

		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

        config.options().copyDefaults(true);
        try {
            config.save(cfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.dfile = new File(p.getDataFolder(), "data.yml");

		if (!this.dfile.exists()) {
			try {
				this.dfile.createNewFile();
				MessageManager.getInstance().log("Data successfully created!");
			} catch (IOException e) {
				Bukkit.getServer().getLogger()
						.severe(ChatColor.RED + "Could not create data.yml!");
			}
		}

		this.data = YamlConfiguration.loadConfiguration(this.dfile);

	}

	public FileConfiguration getData() {
		return this.data;
	}

	public void saveData() {
		try {
			this.data.save(this.dfile);
			MessageManager.getInstance().log("Data successfully saved!");
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save data.yml!");
		}
	}
	

	public void reloadData() {
		this.data = YamlConfiguration.loadConfiguration(this.dfile);
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public void saveConfig() {
		try {
			this.config.save(this.cfile);
			MessageManager.getInstance().log("Config successfully saved!");
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void reloadConfig() {
		this.config = YamlConfiguration.loadConfiguration(this.cfile);
	}

	public PluginDescriptionFile getDesc() {
		return this.p.getDescription();
	}
}