package com.lekohd.shopsystem.manager;

import com.lekohd.shopsystem.ShopGUI;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.entity.SkeletonClass;
import com.lekohd.shopsystem.entity.VillagerClass;
import com.lekohd.shopsystem.entity.ZombieClass;
import org.bukkit.Location;
import org.bukkit.entity.*;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class EntityManager {

    public static void changeEntity(Player p)
    {
        Location sLoc = ShopSystem.playerInShop.get(p.getUniqueId());

        for(Entity entity : sLoc.getChunk().getEntities())
        {
            Location eLoc = entity.getLocation();
            if(sLoc.getBlockX() == eLoc.getBlockX() && sLoc.getBlockY() == eLoc.getBlockY() && sLoc.getBlockZ() == eLoc.getBlockZ() && sLoc.getWorld() == eLoc.getWorld())
            {
                if(entity instanceof Villager)
                {
                    entity.remove();
                    ShopGUI.setAdminMenu(p, ZombieClass.placeZ(entity.getLocation(), entity.getCustomName()));
                } else
                if(entity instanceof Zombie)
                {
                    entity.remove();
                    ShopGUI.setAdminMenu(p, SkeletonClass.placeS(entity.getLocation(), entity.getCustomName()));
                }
                else
                if(entity instanceof Skeleton)
                {
                    entity.remove();
                    ShopGUI.setAdminMenu(p, VillagerClass.placeV(entity.getLocation(), entity.getCustomName()));
                }
            }
        }
    }

    public static Entity getEntityByPlayer(Player p)
    {
        Location sLoc = ShopSystem.playerInShop.get(p.getUniqueId());

        for(Entity entity : sLoc.getChunk().getEntities()) {
            Location eLoc = entity.getLocation();
            if (sLoc.getBlockX() == eLoc.getBlockX() && sLoc.getBlockY() == eLoc.getBlockY() && sLoc.getBlockZ() == eLoc.getBlockZ() && sLoc.getWorld() == eLoc.getWorld()) {
                if(entity != null && entity.getCustomName() != null)
                    return entity;
            }
        }
        return null;
    }

}
