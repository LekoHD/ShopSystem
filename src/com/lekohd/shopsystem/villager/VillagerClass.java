package com.lekohd.shopsystem.villager;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class VillagerClass {
    public static String name;
    public static Location loc;
    public static int professionID = 0;

    /**
     * Placing the shop villager at location
     * @param location is player location
     */
    public static void place(Location location)
    {
        loc = location;
        name = Locale.SHOP_GET_IT;
        Villager v;
        if(ShopSystem.settingsManager.getConfig().getBoolean("config.PlaceVillagerInMiddleOfBlock"))
        {
            Location l = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5);
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

    /**
     * switches the villager shop profession
     * @param villager 's profession. If player click on ChangeProfession Item
     * @param i current villager profession
     */
    public static void switchProfession(Villager villager, int i)
    {
        Villager v = villager;
        i++;
        if(i == 3 || i > 3)
        {
            i = 0;
        }
        professionID = i;
        switch (professionID)
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
