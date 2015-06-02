package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.item.ItemClass;
import com.lekohd.shopsystem.manager.InventoryManager;
import com.lekohd.shopsystem.manager.MessageManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e)
    {
        if(ShopSystem.creatingInv.containsKey(e.getPlayer().getUniqueId())) {
            if (ShopSystem.creatingInv.get(e.getPlayer().getUniqueId())) {
                try {
                    int price = Integer.parseInt(e.getMessage());
                    ItemClass item = ShopSystem.currentItem.get(e.getPlayer().getUniqueId());
                    item.setPrice(price);
                    InventoryManager invMa = ShopSystem.currentInvCreation.get(e.getPlayer().getUniqueId());
                    invMa.addItem(item);
                    ShopSystem.currentInvCreation.put(e.getPlayer().getUniqueId(), invMa);
                    ShopSystem.creatingInv.put(e.getPlayer().getUniqueId(), false);
                    ShopSystem.currentInvCreation.get(e.getPlayer().getUniqueId()).openInv(e.getPlayer());
                    MessageManager.getInstance().msg(e.getPlayer(), MessageManager.MessageType.INFO, Locale.ENTER_PRICE_SUCCESS.replace("%PRICE%", e.getMessage()));
                    e.setCancelled(true);

                } catch (Exception ex) {
                    MessageManager.getInstance().msg(e.getPlayer(), MessageManager.MessageType.INFO, Locale.ENTER_PRICE_FAILED);
                    MessageManager.getInstance().msg(e.getPlayer(), MessageManager.MessageType.INFO, Locale.ENTER_PRICE_FAILED_NOCHARS);
                    e.setCancelled(true);
                }
            }
        }
        if(ShopSystem.editName.containsKey(e.getPlayer().getUniqueId()))
        {
            if(ShopSystem.editName.get(e.getPlayer().getUniqueId()))
            {
                    Player p = e.getPlayer();
                    for(Entity entity : ShopSystem.playerInShop.get(p.getUniqueId()).getChunk().getEntities())
                    {
                        Location loc = entity.getLocation();
                        Location vLoc = ShopSystem.playerInShop.get(p.getUniqueId());
                        if(vLoc.getBlockX() == loc.getBlockX() && vLoc.getBlockY() == loc.getBlockY() && vLoc.getBlockZ() == loc.getBlockZ() && vLoc.getWorld() == loc.getWorld())
                        {
                            if(entity instanceof Villager)
                            {
                                Villager v = (Villager)entity;
                                v.setCustomName(e.getMessage().replace("&","§"));
                                v.setCustomNameVisible(true);
                                //return;
                                MessageManager.getInstance().msg(e.getPlayer(), MessageManager.MessageType.INFO, Locale.ENTER_NAME_SUCCESS);
                            }
                        }
                    }
                    ShopSystem.editName.put(p.getUniqueId(), false);
                    e.setCancelled(true);
            }
        }
        if (ShopSystem.deleteShop.containsKey(e.getPlayer().getUniqueId()))
        {
            if(ShopSystem.deleteShop.get(e.getPlayer().getUniqueId()))
            {
                Player p = e.getPlayer();
                if(e.getMessage().equalsIgnoreCase("y") || e.getMessage().equalsIgnoreCase("yes")|| e.getMessage().equalsIgnoreCase("j") || e.getMessage().equalsIgnoreCase("ja")) {
                    Location loc = ShopSystem.playerInShop.get(p.getUniqueId());
                    if (ShopSystem.dataManager.shopInventory.containsKey(loc)) {
                        ShopSystem.dataManager.shopInventory.remove(loc);
                        ShopSystem.dataManager.removeShop(loc);
                        if (ShopSystem.dataManager.shopOwner.containsKey(loc)) {
                            int shopAmount = ShopSystem.dataManager.shopAmount.get(ShopSystem.dataManager.shopOwner.get(loc));
                            ShopSystem.dataManager.shopAmount.put(ShopSystem.dataManager.shopOwner.get(loc), shopAmount - 1);
                            ShopSystem.dataManager.shopOwner.remove(loc);
                        }
                        for (Entity entity : loc.getChunk().getEntities()) {
                            Location lo = entity.getLocation();
                            Location location = new Location(lo.getWorld(), lo.getBlockX(), lo.getBlockY(), lo.getBlockZ());
                            System.out.println("Entity: " + entity.getCustomName());
                            if (location.getBlockX() == loc.getBlockX() && location.getBlockY() == loc.getBlockY() && location.getBlockZ() == loc.getBlockZ() && location.getWorld() == loc.getWorld()) {
                                if (entity instanceof Villager) {
                                    Villager v = (Villager) entity;
                                    v.setCustomName(Locale.SHOP_GET_IT);
                                    v.setCustomNameVisible(true);
                                    //return;
                                    MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.ENTER_DELETE_SHOP_SUCCESS);
                                }
                            }
                        }
                    }
                }
                else {
                    MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.ENTER_DELETE_SHOP_FAILED);
                }
                e.setCancelled(true);
                ShopSystem.deleteShop.put(p.getUniqueId(), false);
            }
        }

    }

}
