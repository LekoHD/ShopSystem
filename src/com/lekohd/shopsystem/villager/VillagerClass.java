package com.lekohd.shopsystem.villager;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class VillagerClass {
    public String name;
    public Location loc;

    public VillagerClass(String name, Location loc)  //Villager color
    {
        if(name == null)
        {
            this.name = Locale.SHOP_GET_IT;
        } else
            this.name = name;
        this.loc = loc;
    }

    public void place()
    {
        Villager v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
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
}
