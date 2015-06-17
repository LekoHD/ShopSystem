package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopGUI;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.entity.SkeletonClass;
import com.lekohd.shopsystem.entity.ZombieClass;
import com.lekohd.shopsystem.manager.EntityManager;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */

public class VillagerListener implements Listener {

    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent e)
    {

        Player p = e.getPlayer();
        if(e.getRightClicked().getType() == EntityType.VILLAGER || e.getRightClicked().getType() == EntityType.ZOMBIE || e.getRightClicked().getType() == EntityType.SKELETON)
        {
            Entity entity = e.getRightClicked();
            Location l = new Location(entity.getLocation().getWorld(), entity.getLocation().getBlockX(), entity.getLocation().getBlockY(), entity.getLocation().getBlockZ());
            ShopSystem.playerInShop.put(p.getUniqueId(), l);
            ShopSystem.playerInShopUUID.put(p.getUniqueId(), e.getRightClicked().getUniqueId());
            if(entity.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT))
            {
                e.setCancelled(true);
                ShopGUI.setGetItMenu(p);
            }
            else
            {
                Location vLoc = entity.getLocation();
                Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
                if (ShopSystem.dataManager.shopInventory.containsKey(loc)) {
                    if (ShopSystem.dataManager.shopOwner.containsKey(loc)) {
                        if (p.getUniqueId().toString().equalsIgnoreCase(ShopSystem.dataManager.shopOwner.get(loc).toString())) {
                            e.setCancelled(true);
                            ShopGUI.setAdminMenu(p, entity);
                        }else {
                            e.setCancelled(true);
                            ShopSystem.dataManager.shopInventory.get(loc).openUserInv(p);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(ShopSystem.editArmor.containsKey(e.getPlayer().getUniqueId()))
        {
            if(ShopSystem.editArmor.get(e.getPlayer().getUniqueId()))
            {
                Entity entity = EntityManager.getEntityByPlayer(e.getPlayer());
                if(entity instanceof Villager)
                {
                    e.setCancelled(true);
                } else if(entity instanceof Zombie)
                {
                    ZombieClass.addArmor((Zombie)entity, e.getPlayer());
                    ShopGUI.openArmorMenu(e.getPlayer());
                } else if(entity instanceof Skeleton)
                {
                    SkeletonClass.addArmor((Skeleton) entity, e.getPlayer());
                    ShopGUI.openArmorMenu(e.getPlayer());
                }
                ShopSystem.editArmor.put(e.getPlayer().getUniqueId(), false);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e)
    {
        if(e.getEntity() == null || e.getEntity().getCustomName() == null)
            return;
        if(e.getEntity() instanceof Villager || e.getEntity() instanceof Zombie || e.getEntity() instanceof Skeleton)
        {
            Entity entity = e.getEntity();
            Location vLoc = entity.getLocation();
            Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
            if(entity.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT) || ShopSystem.dataManager.shopInventory.containsKey(loc))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() == null || e.getEntity().getCustomName() == null)
            return;
        if(e.getEntity() instanceof Villager || e.getEntity() instanceof Zombie || e.getEntity() instanceof Skeleton)
        {
            Entity entity = e.getEntity();
            Location vLoc = entity.getLocation();
            Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
            if(entity.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT) || ShopSystem.dataManager.shopInventory.containsKey(loc))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e)
    {
        if(e.getEntity() == null || e.getEntity().getCustomName() == null)
            return;
        if(e.getEntity() instanceof Villager || e.getEntity() instanceof Zombie || e.getEntity() instanceof Skeleton)
        {
            Entity entity = e.getEntity();
            Location vLoc = entity.getLocation();
            Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
            if(entity.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT) || ShopSystem.dataManager.shopInventory.containsKey(loc))
            {
                e.setCancelled(true);
            }
        }
    }

}
