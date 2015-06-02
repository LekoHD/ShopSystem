package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.item.ItemCreation;
import com.lekohd.shopsystem.util.ItemType;
import com.lekohd.shopsystem.villager.VillagerClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

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

                Inventory inv = Bukkit.createInventory(null, 9, Locale.SHOP_MENU);
                if(ShopSystem.permHandler.hasRequiredPermissions(p))
                {
                    inv.setItem(0, ItemType.CREATESHOP.getItem());
                }
                else
                {
                    inv.setItem(0, ItemType.CANTCREATESHOP.getItem());
                }
                //inv.setItem(3, ItemType.CHANGENAME.getItem());
                inv.setItem(8, ItemType.LEAVE.getItem());
                p.openInventory(inv);

            }
            else
            {
                Location vLoc = v.getLocation();
                Location loc = new Location(vLoc.getWorld(), vLoc.getBlockX(), vLoc.getBlockY(), vLoc.getBlockZ());
                //System.out.println(loc);
                if(ShopSystem.settingsManager.getConfig().getBoolean("config.UseUUID")) {
                    for (Entity entity : ShopSystem.playerInShop.get(p.getUniqueId()).getWorld().getEntities()) {
                        if (ShopSystem.dataManager.shopInventoryUUID.containsKey(entity.getUniqueId())) {
                            if (ShopSystem.dataManager.shopOwnerUUID.containsKey(entity.getUniqueId())) {
                                if (p.getUniqueId().toString().equalsIgnoreCase(ShopSystem.dataManager.shopOwnerUUID.get(entity.getUniqueId()).toString())) {
                                    e.setCancelled(true);
                                    Inventory inv = Bukkit.createInventory(null, 9, Locale.SHOP_MENU);
                                    inv.setItem(0, ItemType.EDITSHOP.getItem());
                                    inv.setItem(1, ItemType.DELETESHOP.getItem());
                                    if(p.hasPermission("shopsystem.shop.changeName"))
                                        inv.setItem(3, ItemType.CHANGENAME.getItem());
                                    inv.setItem(4, ItemType.SHOWSHOP.getItem());
                                    if(p.hasPermission("shopsystem.shop.changeProfession")) {
                                        if (v.getProfession() == Villager.Profession.FARMER)
                                            VillagerClass.professionByUUID.put(p.getUniqueId(), 0);
                                        if (v.getProfession() == Villager.Profession.LIBRARIAN)
                                            VillagerClass.professionByUUID.put(p.getUniqueId(), 1);
                                        if (v.getProfession() == Villager.Profession.PRIEST)
                                            VillagerClass.professionByUUID.put(p.getUniqueId(), 2);
                                        inv.setItem(6, new ItemCreation(Locale.ITEM_CHANGE_PROFESSION, Material.WOOL, null, 1, VillagerClass.getProfession(p)).getItem());

                                    }
                                    inv.setItem(8, ItemType.LEAVE.getItem());
                                    p.openInventory(inv);
                                } else {
                                    e.setCancelled(true);
                                    ShopSystem.dataManager.shopInventoryUUID.get(entity.getUniqueId()).openUserInv(p);
                                }
                            }
                        }
                    }
                } else {
                    if (ShopSystem.dataManager.shopInventory.containsKey(loc)) {
                        //System.out.println("Same Loc");
                        if (ShopSystem.dataManager.shopOwner.containsKey(loc)) {
                            //System.out.println("Same Loc");
                            //System.out.println(p.getUniqueId());
                            //System.out.println(ShopSystem.dataManager.shopOwner.get(loc));
                            if (p.getUniqueId().toString().equalsIgnoreCase(ShopSystem.dataManager.shopOwner.get(loc).toString())) {
                                //System.out.println("Same UUID");
                                e.setCancelled(true);
                                Inventory inv = Bukkit.createInventory(null, 9, Locale.SHOP_MENU);
                                inv.setItem(0, ItemType.EDITSHOP.getItem());
                                inv.setItem(1, ItemType.DELETESHOP.getItem());
                                if(p.hasPermission("shopsystem.shop.changeName"))
                                    inv.setItem(3, ItemType.CHANGENAME.getItem());
                                inv.setItem(4, ItemType.SHOWSHOP.getItem());
                                if(p.hasPermission("shopsystem.shop.changeProfession")) {
                                    if (v.getProfession() == Villager.Profession.FARMER)
                                        VillagerClass.professionByUUID.put(p.getUniqueId(), 0);
                                    if (v.getProfession() == Villager.Profession.LIBRARIAN)
                                        VillagerClass.professionByUUID.put(p.getUniqueId(), 1);
                                    if (v.getProfession() == Villager.Profession.PRIEST)
                                        VillagerClass.professionByUUID.put(p.getUniqueId(), 2);
                                    inv.setItem(6, new ItemCreation(Locale.ITEM_CHANGE_PROFESSION, Material.WOOL, null, 1, VillagerClass.getProfession(p)).getItem());
                                }
                                inv.setItem(8, ItemType.LEAVE.getItem());
                                p.openInventory(inv);
                            } else {
                                e.setCancelled(true);
                                ShopSystem.dataManager.shopInventory.get(loc).openUserInv(p);
                            }
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

    /*private static final UUID movementSpeedUID = UUID.fromString("206a89dc-ae78-4c4d-b42c-3b31db3f5a7c");

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawn(CreatureSpawnEvent event){
        LivingEntity entity = event.getEntity();

        if (entity.getType() == EntityType.VILLAGER){
            Villager v = (Villager)entity;
            //if(v.getCustomName().equalsIgnoreCase(""))
            EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
            AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.d);

            AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "<plugin_name> movement speed multiplier", 0d, 0);

            attributes.b(modifier);
            attributes.a(modifier);
        }
    }*/

}
