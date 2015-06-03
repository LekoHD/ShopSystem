package com.lekohd.shopsystem.manager;

import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.item.ItemClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class DataManager {

    public static FileConfiguration data;
    public static HashMap<Location, UUID> shopOwner = new HashMap<Location, UUID>();
    public static HashMap<Location, InventoryManager> shopInventory = new HashMap<Location, InventoryManager>();
    public static HashMap<Location, UUID> shop = new HashMap<Location, UUID>();
    public static HashMap<UUID, InventoryManager> shopInventoryUUID = new HashMap<UUID, InventoryManager>();
    public static HashMap<UUID, UUID> shopOwnerUUID = new HashMap<UUID, UUID>(); //villager, player
    public static HashMap<UUID, Integer> shopAmount = new HashMap<UUID, Integer>();
    public static int maxShops;

    public DataManager ()
    {
        data = ShopSystem.settingsManager.getData();
        maxShops = ShopSystem.settingsManager.getConfig().getInt("config.ShopsPerUser");
    }

    public static void loadShopAmount()
    {
        if(!data.isConfigurationSection("owner")) return;
        for(String name : data.getConfigurationSection("owner").getKeys(false))
        {
            String uuid = name;
            shopAmount.put(UUID.fromString(uuid), data.getInt("owner." + uuid));
        }
    }

    public static void loadAll()
    {
        if(!data.isConfigurationSection("Locations")) return;
        for (String name : data.getConfigurationSection("Locations").getKeys(false)) {
            //String content = locale.getString("Messages." + name).replace("&", "§");
            String x = name;
            for(String nam : data.getConfigurationSection("Locations." + x).getKeys(false))
            {
                String y = nam;
                for(String na : data.getConfigurationSection("Locations." + x + "." + y).getKeys(false))
                {
                    String z = na;
                    for(String n : data.getConfigurationSection("Locations." + x + "." + y + "." + z).getKeys(false))
                    {
                        String world = n;
                        String path = "Locations." + x + "." + y + "." + z + "." + world;
                        Location loc = new Location(Bukkit.getWorld(world), Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
                        shopOwner.put(loc, UUID.fromString(data.getString(path + ".owner")));
                        String uuid = data.getString(path + ".shopUUID");
                        shop.put(loc, UUID.fromString(uuid));
                        shopOwnerUUID.put(UUID.fromString(uuid),UUID.fromString(data.getString(path + ".owner")));
                        InventoryManager inventoryManager = new InventoryManager(45);
                        if(data.isConfigurationSection(path + ".item")) {
                            for (String slot : data.getConfigurationSection(path + ".item").getKeys(false)) {
                                String p = path;
                                p = p + ".item." + slot;
                                Material material = Material.valueOf(data.getString(p + ".material"));
                                int amount = data.getInt(p + ".amount");
                                int sl = Integer.parseInt(slot);
                                int price = data.getInt(p + ".price");
                                byte type = 0;
                                type = (byte)data.getInt(p + ".type");
                                ArrayList<String> ens = (ArrayList)data.getList(p + ".enchantment");
                                HashMap<Enchantment, Integer> enchantmentIntegerMap = new HashMap<Enchantment, Integer>();
                                for(String s : ens)
                                {
                                    String[] ss = s.split(":");
                                    enchantmentIntegerMap.put(Enchantment.getByName(ss[0]), Integer.parseInt(ss[1]));
                                }
                                ItemStack pItem = new ItemStack(material, 1, type);
                                pItem.addEnchantments(enchantmentIntegerMap);
                                ItemClass item = new ItemClass(pItem, amount, price, sl);
                                inventoryManager.addItem(item);
                            }
                        }
                        shopInventory.put(loc, inventoryManager);
                        shopInventoryUUID.put(UUID.fromString(uuid), inventoryManager);



                    }
                }
            }
        }
    }

    public static void saveByLocation(Location loc, UUID owner, InventoryManager inventoryManager, UUID shopUUID, boolean singleSave)
    {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        String world = loc.getWorld().getName();

        //if(data.contains(x + "." + y + "." + z + "." + world))
        String path = "Locations." + x + "." + y + "." + z + "." + world;
        data.set(path + ".owner", owner.toString());
        data.set(path + ".shopUUID", shopUUID.toString());

        ArrayList<ItemClass> items = inventoryManager.getItems();
        data.set(path + ".item", null);
        for(ItemClass item : items)
        {
            if(item.getPrice() > -1) {
                data.set(path + ".item." + item.getSlot() + ".material", item.getItem().getType().toString());
                data.set(path + ".item." + item.getSlot() + ".amount", item.getAmount());
                data.set(path + ".item." + item.getSlot() + ".slot", item.getSlot());
                data.set(path + ".item." + item.getSlot() + ".price", item.getPrice());
                data.set(path + ".item." + item.getSlot() + ".type", item.getItem().getData().getData());
                ArrayList<String> ens = new ArrayList<String>();
                if(item.getItem().getItemMeta().hasEnchants()) {
                    for (Enchantment enchantment : item.getItem().getEnchantments().keySet()) {
                        String s = enchantment.getName();
                        s = s + ":";
                        s = s + item.getItem().getEnchantments().get(enchantment);
                        ens.add(s);
                    }
                }
                data.set(path + ".item." + item.getSlot() + ".enchantment", ens);
            }
        }
        if(singleSave)
            ShopSystem.settingsManager.saveData();
    }

    public static void saveShopAmountByUUID(UUID owner, int amount, boolean singleSave)
    {
        data.set("owner." + owner.toString(), amount);
        if(singleSave)
            ShopSystem.settingsManager.saveData();
    }

    public static void saveAllShopAmounts()
    {
        for(UUID uuid : shopAmount.keySet())
        {
            saveShopAmountByUUID(uuid, shopAmount.get(uuid), false);
        }
        ShopSystem.settingsManager.saveData();
    }

    public static boolean userIsInConfig(UUID owner)
    {
        if(!data.isConfigurationSection("owner")) return false;
        for(String name : data.getConfigurationSection("owner").getKeys(false)) {
            String uuid = name;
            if(uuid.equalsIgnoreCase(owner.toString()))
                return true;
        }
        return false;
    }

    public static void saveAll()
    {
        for (Location loc : shopInventory.keySet())
        {
            saveByLocation(loc, shopOwner.get(loc), shopInventory.get(loc), shop.get(loc), false);
        }
        ShopSystem.settingsManager.saveData();
    }

    public static void removeShop(Location loc)
    {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        String world = loc.getWorld().getName();

        //if(data.contains(x + "." + y + "." + z + "." + world))
        String path = "Locations." + x + "." + y + "." + z + "." + world;
        data.set(path, null);
        ShopSystem.settingsManager.saveData();
    }

}
