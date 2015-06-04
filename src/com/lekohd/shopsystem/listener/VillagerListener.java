package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopGUI;
import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
        if(e.getRightClicked().getType() == EntityType.VILLAGER)
        {
            Villager v = (Villager) e.getRightClicked();
            Location l = new Location(v.getLocation().getWorld(), v.getLocation().getBlockX(), v.getLocation().getBlockY(), v.getLocation().getBlockZ());
            ShopSystem.playerInShop.put(p.getUniqueId(), l);
            ShopSystem.playerInShopUUID.put(p.getUniqueId(), e.getRightClicked().getUniqueId());
            if(v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT))
            {
                e.setCancelled(true);
                ShopGUI.setGetItMenu(p);
            }
            else
            {
                Location vLoc = v.getLocation();
                Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
                if (ShopSystem.dataManager.shopInventory.containsKey(loc)) {
                    if (ShopSystem.dataManager.shopOwner.containsKey(loc)) {
                        if (p.getUniqueId().toString().equalsIgnoreCase(ShopSystem.dataManager.shopOwner.get(loc).toString())) {
                            e.setCancelled(true);
                            ShopGUI.setAdminMenu(p, v);
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
    public void onEntityDamage(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Villager)
        {
            Villager v = (Villager)e.getEntity();
            Location vLoc = v.getLocation();
            Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
            if(v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT) || ShopSystem.dataManager.shopInventory.containsKey(loc))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof Villager)
        {
            Villager v = (Villager)e.getEntity();
            Location vLoc = v.getLocation();
            Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
            if(v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT) || ShopSystem.dataManager.shopInventory.containsKey(loc))
            {
                e.setCancelled(true);
            }
        }
    }

}
