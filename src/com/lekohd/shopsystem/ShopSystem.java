package com.lekohd.shopsystem;

import com.google.gson.Gson;
import com.lekohd.shopsystem.commands.Commands;
import com.lekohd.shopsystem.compat.NMSManager;
import com.lekohd.shopsystem.handler.PermissionHandler;
import com.lekohd.shopsystem.item.ItemClass;
import com.lekohd.shopsystem.listener.*;
import com.lekohd.shopsystem.manager.DataManager;
import com.lekohd.shopsystem.manager.InventoryManager;
import com.lekohd.shopsystem.manager.MessageManager;
import com.lekohd.shopsystem.manager.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Leon167, LekoHD
 * @version 0.1
 *
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 *
 * @permissions:
 *   - shopsystem.admin
 *   - shopsystem.shop.changeProfession
 *   - shopsystem.shop.changeName
 *   - shopsystem.shop.changeArmor
 *   - shopsystem.shop.changeEntity
 *   - shopsystem.shop.changeMode
 *   - shopsystem.shop.create.<amount> amount =< config.ShopsPerUser
 */

public class ShopSystem extends JavaPlugin {  //Empty Inventory Bug, Red Dot for idiots


    public static ShopSystem instance;
    public static SettingsManager settingsManager;
    public static DataManager dataManager;
    public static NMSManager nmsManager;
    public static PermissionHandler permHandler;
    Logger logger = Bukkit.getLogger();
    ConsoleCommandSender clogger = this.getServer().getConsoleSender();
    public static HashMap<UUID, Location> playerInShop = new HashMap<UUID, Location>();
    public static HashMap<UUID, UUID> playerInShopUUID = new HashMap<UUID, UUID>();
    public static HashMap<UUID, InventoryManager> currentInvCreation = new HashMap<UUID, InventoryManager>();
    public static HashMap<UUID, ItemClass> currentItem = new HashMap<UUID, ItemClass>();
    public static HashMap<UUID, Boolean> creatingInv = new HashMap<UUID, Boolean>();
    public static HashMap<UUID, Integer> currentSlot = new HashMap<UUID, Integer>();
    public static HashMap<UUID, Boolean> editName = new HashMap<UUID, Boolean>();
    public static HashMap<UUID, Boolean> deleteShop = new HashMap<UUID, Boolean>();
    public static HashMap<UUID, Boolean> isEditingShop = new HashMap<UUID, Boolean>();
    public static HashMap<UUID, Boolean> editArmor = new HashMap<UUID, Boolean>();
    public static ArrayList<ShopData> shopDatas = new ArrayList<ShopData>();
    public Gson gson;

    /**
     * Loading all shops and messages
     */
    public void onEnable(){
        instance = this;
        settingsManager = new SettingsManager();
        settingsManager.setup(instance);
        nmsManager = new NMSManager();
        nmsManager.load(instance);
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new InventoryListener(), this);
        pm.registerEvents(new VillagerListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new LeaveList(), this);
        pm.registerEvents(new DropItemListener(), this);
        this.getCommand("shop").setExecutor(new Commands());
        this.loadLocale();
        clogger.sendMessage(ChatColor.DARK_GRAY + "*******************************");
        clogger.sendMessage(ChatColor.DARK_GRAY + "*       " + ChatColor.AQUA + "InventoryShops+" + ChatColor.DARK_GRAY + "       *");
        clogger.sendMessage(ChatColor.DARK_GRAY + "*  " + ChatColor.GOLD + "Author: " + this.getDescription().getAuthors() + ChatColor.DARK_GRAY + "  *");
        clogger.sendMessage(ChatColor.DARK_GRAY + "*        " + ChatColor.GOLD + "Version: " + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "         *");
        clogger.sendMessage(ChatColor.DARK_GRAY + "*******************************");
        dataManager = new DataManager();
        dataManager.loadAll();
        dataManager.loadShopAmount();
        permHandler = new PermissionHandler(settingsManager.getConfig().getInt("config.ShopsPerUser"));
        this.gson = new Gson();
        /*if(!getFile().exists())
        {
            try {
                getFile().createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else
        {

        }*/
    }


    /**
     * Saving all shops on disable
     */
    public void onDisable(){
        dataManager.saveAll();
        dataManager.saveAllShopAmounts();
    }

    protected File getFile()
    {
        return new File(getDataFolder(), "data.json");
    }

    public void saveData()
    {
        ShopDataCollection shopDataCollection = new ShopDataCollection(shopDatas);
        String json = gson.toJson(shopDataCollection);
        try {
            FileWriter writer = new FileWriter(getFile());
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData()
    {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(getFile());
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder fileContent = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                fileContent.append(line);
            }
            String content = fileContent.toString();
            ShopDataCollection shops = gson.fromJson(content, ShopDataCollection.class);
            for(ShopData sd : shops.getShops())
                shopDatas.add(sd);
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loading all messages
     */
    public void loadLocale(){
        FileConfiguration locale;
        File lfile;

        lfile = new File(this.getDataFolder(), "locale.yml");
        if (!lfile.exists()) {
            try {
                lfile.createNewFile();
                MessageManager.getInstance().log("Locale file successfully created!");
            } catch (IOException e) {
                Bukkit.getServer().getLogger()
                        .severe(ChatColor.RED + "Could not create locale.yml!");
            }
        }

        locale = YamlConfiguration.loadConfiguration(lfile);
        try {
            for(Field field : Locale.class.getDeclaredFields())
            {
                if(field.getType() == String.class)
                {
                    String name = field.getName();
                    String content = (String) field.get(null);
                    if(!locale.contains("Messages." + name))
                        locale.set("Messages." + name, content.replace("§", "&"));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            locale.save(lfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!locale.isConfigurationSection("Messages")) return;
        try {
            for (String name : locale.getConfigurationSection("Messages").getKeys(false)) {
                String content = locale.getString("Messages." + name).replace("&", "§");
                Field field = Locale.class.getDeclaredField(name);
                field.set(null, content);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * @return the Plugin instance
     */
    public static ShopSystem getInstance(){
        return instance;
    }

}
