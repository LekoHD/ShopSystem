package com.lekohd.shopsystem.entity;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Leon on 04.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class SkeletonClass {

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
        Skeleton s;
        if(ShopSystem.settingsManager.getConfig().getBoolean("config.PlaceVillagerInMiddleOfBlock"))
        {
            Location l = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
            s = (Skeleton) l.getWorld().spawnEntity(l, EntityType.SKELETON);
        } else
            s = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
        try {
            //ShopSystem.nmsManager.getProvider().noAI(v);
            //ShopSystem.nmsManager.getProvider().overwriteVillagerAI(v);
            ShopSystem.nmsManager.getProvider().noAI(s);
        } catch (Exception e){
            s.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
        }

        s.setCustomName(name);
        s.setCustomNameVisible(true);
        s.setCanPickupItems(false);
    }

    public static Skeleton placeS(Location location, String eName)
    {
        loc = location;
        name = eName;
        if(eName == null)
            name = Locale.SHOP_GET_IT;
        Skeleton s;
        if(ShopSystem.settingsManager.getConfig().getBoolean("config.PlaceVillagerInMiddleOfBlock"))
        {
            Location l = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
            s = (Skeleton) l.getWorld().spawnEntity(l, EntityType.SKELETON);
        } else
            s = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
        try {
            //ShopSystem.nmsManager.getProvider().noAI(v);
            //ShopSystem.nmsManager.getProvider().overwriteVillagerAI(v);
            ShopSystem.nmsManager.getProvider().noAI(s);
        } catch (Exception e){
            s.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
        }

        s.setCustomName(name);
        s.setCustomNameVisible(true);
        s.setCanPickupItems(false);
        return s;
    }

    public static void removeHelmet(Skeleton skeleton)
    {
        Skeleton e = skeleton;
        e.getEquipment().setHelmet(new ItemStack(Material.AIR));
    }

    public static void removeChestPlate(Skeleton skeleton)
    {
        Skeleton e = skeleton;
        e.getEquipment().setChestplate(new ItemStack(Material.AIR));
    }

    public static void removeLeggings(Skeleton skeleton)
    {
        Skeleton e = skeleton;
        e.getEquipment().setLeggings(new ItemStack(Material.AIR));
    }

    public static void removeBoots(Skeleton skeleton)
    {
        Skeleton e = skeleton;
        e.getEquipment().setBoots(new ItemStack(Material.AIR));
    }

    /**
     * switches the villager shop profession
     * @param skeleton 's armor. If player click on ChangeArmor Item
     * @param p for uuid
     */
    public static void addArmor(Skeleton skeleton, Player p)
    {
        Skeleton s = skeleton;
        EntityEquipment equipment = s.getEquipment();
        ItemStack item = p.getItemInHand();
        if(item != null && item.getType() != Material.AIR) {
            Material material = item.getType();
            if (material == Material.WOOD_SWORD || material == Material.STONE_SWORD || material == Material.IRON_SWORD || material == Material.GOLD_SWORD || material == Material.DIAMOND_SWORD) {
                equipment.setItemInHand(item);
            } else if (material == Material.CHAINMAIL_BOOTS || material == Material.DIAMOND_BOOTS || material == Material.GOLD_BOOTS || material == Material.IRON_BOOTS || material == Material.LEATHER_BOOTS) {
                equipment.setBoots(item);
            } else if (material == Material.CHAINMAIL_LEGGINGS || material == Material.DIAMOND_LEGGINGS || material == Material.GOLD_LEGGINGS || material == Material.IRON_LEGGINGS || material == Material.LEATHER_LEGGINGS) {
                equipment.setLeggings(item);
            } else if (material == Material.CHAINMAIL_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE || material == Material.GOLD_CHESTPLATE || material == Material.IRON_CHESTPLATE || material == Material.LEATHER_CHESTPLATE) {
                equipment.setChestplate(item);
            } else if (material == Material.CHAINMAIL_HELMET || material == Material.DIAMOND_HELMET || material == Material.GOLD_HELMET || material == Material.IRON_HELMET || material == Material.LEATHER_HELMET) {
                equipment.setHelmet(item);
            }
        }
    }

}
