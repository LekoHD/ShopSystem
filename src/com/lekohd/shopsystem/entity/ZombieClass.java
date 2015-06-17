package com.lekohd.shopsystem.entity;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Leon on 04.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ZombieClass {

    public static String name;
    public static Location loc;

    /**
     * Placing the shop villager at location
     * @param location is player location
     */
    public static void place(Location location, String eName)
    {
        loc = location;
        name = eName;
        if(eName == null)
            name = Locale.SHOP_GET_IT;
        Zombie z;
        if(ShopSystem.settingsManager.getConfig().getBoolean("config.PlaceVillagerInMiddleOfBlock"))
        {
            Location l = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
            z = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
        } else
            z = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        try {
            //ShopSystem.nmsManager.getProvider().noAI(v);
            //ShopSystem.nmsManager.getProvider().overwriteVillagerAI(v);
            ShopSystem.nmsManager.getProvider().noAI(z);
        } catch (Exception e){
            z.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
        }

        z.setCustomName(name);
        z.setCustomNameVisible(true);
        z.setCanPickupItems(false);
        z.setVillager(false);
    }

    public static Zombie placeZ(Location location, String eName)
    {
        loc = location;
        name = eName;
        if(eName == null)
            name = Locale.SHOP_GET_IT;
        Zombie z;
        if(ShopSystem.settingsManager.getConfig().getBoolean("config.PlaceVillagerInMiddleOfBlock"))
        {
            Location l = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
            z = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
        } else
            z = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        try {
            //ShopSystem.nmsManager.getProvider().noAI(v);
            //ShopSystem.nmsManager.getProvider().overwriteVillagerAI(v);
            ShopSystem.nmsManager.getProvider().noAI(z);
        } catch (Exception e){
            z.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
        }

        z.setCustomName(name);
        z.setCustomNameVisible(true);
        z.setCanPickupItems(false);
        z.setVillager(false);
        return z;
    }

    public static void removeHelmet(Zombie zombie)
    {
        Zombie e = zombie;
        e.getEquipment().setHelmet(new ItemStack(Material.AIR));
    }

    public static void removeChestPlate(Zombie zombie)
    {
        Zombie e = zombie;
        e.getEquipment().setChestplate(new ItemStack(Material.AIR));
    }

    public static void removeLeggings(Zombie zombie)
    {
        Zombie e = zombie;
        e.getEquipment().setLeggings(new ItemStack(Material.AIR));
    }

    public static void removeBoots(Zombie zombie)
    {
        Zombie e = zombie;
        e.getEquipment().setBoots(new ItemStack(Material.AIR));
    }

    /**
     * switches the villager shop profession
     * @param zombie 's armor. If player click on ChangeArmor Item
     * @param p for uuid
     */
    public static void addArmor(Zombie zombie, Player p)
    {
        Zombie z = zombie;
        EntityEquipment equipment = z.getEquipment();
        ItemStack item = p.getItemInHand();
        if(item != null && item.getType() != Material.AIR)
        {
            Material material = item.getType();
            if(material == Material.WOOD_SWORD || material == Material.STONE_SWORD || material == Material.IRON_SWORD || material == Material.GOLD_SWORD || material == Material.DIAMOND_SWORD)
            {
                equipment.setItemInHand(item);
            }
            else
            if(material == Material.CHAINMAIL_BOOTS || material == Material.DIAMOND_BOOTS || material == Material.GOLD_BOOTS || material == Material.IRON_BOOTS || material == Material.LEATHER_BOOTS)
            {
                equipment.setBoots(item);
            }
            else
            if(material == Material.CHAINMAIL_LEGGINGS || material == Material.DIAMOND_LEGGINGS || material == Material.GOLD_LEGGINGS || material == Material.IRON_LEGGINGS || material == Material.LEATHER_LEGGINGS)
            {
                equipment.setLeggings(item);
            }
            else
            if(material == Material.CHAINMAIL_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE || material == Material.GOLD_CHESTPLATE || material == Material.IRON_CHESTPLATE || material == Material.LEATHER_CHESTPLATE)
            {
                equipment.setChestplate(item);
            }
            else
            if(material == Material.CHAINMAIL_HELMET || material == Material.DIAMOND_HELMET || material == Material.GOLD_HELMET || material == Material.IRON_HELMET || material == Material.LEATHER_HELMET)
            {
                equipment.setHelmet(item);
            }
        }
    }

}
