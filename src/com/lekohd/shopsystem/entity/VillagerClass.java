package com.lekohd.shopsystem.entity;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */

public class VillagerClass {
    public static String name;
    public static Location loc;
    public static HashMap<UUID, Integer> professionByUUID = new HashMap<UUID, Integer>();

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
        Villager v;
        if(ShopSystem.settingsManager.getConfig().getBoolean("config.PlaceVillagerInMiddleOfBlock"))
        {
            Location l = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
            v = (Villager) l.getWorld().spawnEntity(l, EntityType.VILLAGER);
        } else
            v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        try {
            //ShopSystem.nmsManager.getProvider().noAI(v);
            //ShopSystem.nmsManager.getProvider().overwriteVillagerAI(v);
            ShopSystem.nmsManager.getProvider().noAI(v);
        } catch (Exception e){
            v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
        }

        v.setCustomName(name);
        v.setCustomNameVisible(true);
        v.setCanPickupItems(false);
        v.setProfession(Villager.Profession.FARMER);
        //v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
    }

    public static Villager placeV(Location location, String eName)
    {
        loc = location;
        name = eName;
        if(eName == null)
            name = Locale.SHOP_GET_IT;
        Villager v;
        if(ShopSystem.settingsManager.getConfig().getBoolean("config.PlaceVillagerInMiddleOfBlock"))
        {
            Location l = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
            v = (Villager) l.getWorld().spawnEntity(l, EntityType.VILLAGER);
        } else
            v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        try {
            //ShopSystem.nmsManager.getProvider().noAI(v);
            //ShopSystem.nmsManager.getProvider().overwriteVillagerAI(v);
            ShopSystem.nmsManager.getProvider().noAI(v);
        } catch (Exception e){
            v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
        }

        v.setCustomName(name);
        v.setCustomNameVisible(true);
        v.setCanPickupItems(false);
        v.setProfession(Villager.Profession.FARMER);
        //v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
        return v;
    }

    public static int getProfession(Player p)
    {
        if(!professionByUUID.containsKey(p.getUniqueId()))
        {
            professionByUUID.put(p.getUniqueId(), 0);
            return 0;
        }
        return professionByUUID.get(p.getUniqueId());
    }

    /**
     * switches the villager shop profession
     * @param villager 's profession. If player click on ChangeProfession Item
     * @param p for uuid
     */
    public static void switchProfession(Villager villager, Player p)
    {
        Villager v = villager;
        UUID uuid = p.getUniqueId();
        if(!professionByUUID.containsKey(uuid))
            professionByUUID.put(uuid, 0);
        int i = professionByUUID.get(uuid);
        i++;
        if(i == 3 || i > 3)
        {
            i = 0;
        }
        professionByUUID.put(uuid, i);
        switch (i)
        {
            case 0: v.setProfession(Villager.Profession.FARMER);  //Brown
                break;
            case 1: v.setProfession(Villager.Profession.LIBRARIAN);  //White
                break;
            case 2: v.setProfession(Villager.Profession.PRIEST); //Pink
                break;
            default: v.setProfession(Villager.Profession.FARMER);
                break;
        }
    }

}
