package com.lekohd.shopsystem.listener;

import com.lekohd.economysystem.EconomySystem;
import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.exception.ItemBuyException;
import com.lekohd.shopsystem.exception.NotEnoughMoneyException;
import com.lekohd.shopsystem.handler.EconomyHandler;
import com.lekohd.shopsystem.item.ItemClass;
import com.lekohd.shopsystem.item.ItemCreation;
import com.lekohd.shopsystem.manager.InventoryManager;
import com.lekohd.shopsystem.manager.MessageManager;
import com.lekohd.shopsystem.util.ItemType;
import com.lekohd.shopsystem.villager.VillagerClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.nio.cs.ext.SJIS;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e)
    {
        //System.out.println(ShopSystem.dataManager.shopAmount.get(((Player)e.getWhoClicked()).getUniqueId()));
        if(e.isLeftClick() || e.isRightClick() || e.isShiftClick())
        {
            if(e.getClickedInventory() == null) return;
            if(e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_SOME || e.getAction() == InventoryAction.PLACE_ONE)
            {
                if(!e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CREATE)) return;
                ShopSystem.isEditingShop.put(((Player)e.getWhoClicked()).getUniqueId(), true);
                InventoryManager invMa = ShopSystem.currentInvCreation.get(((Player) e.getWhoClicked()).getUniqueId());
                ItemClass item = new ItemClass(e.getCursor(), e.getCursor().getAmount(), 0, e.getSlot());
                ShopSystem.currentItem.put(((Player)e.getWhoClicked()).getUniqueId(), item);
                invMa.addItem(item);
                ShopSystem.currentInvCreation.put(((Player) e.getWhoClicked()).getUniqueId(), invMa);
                ShopSystem.creatingInv.put(((Player) e.getWhoClicked()).getUniqueId(), true);
                ShopSystem.currentSlot.put(((Player) e.getWhoClicked()).getUniqueId(), e.getSlot());
                MessageManager.getInstance().msg((Player) e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.ENTER_PRICE);
                e.setCurrentItem(new ItemStack(Material.AIR));
                e.setCancelled(true);
                ((Player) e.getWhoClicked()).closeInventory();
                return;
            }
            if(e.getCurrentItem() == null)
                return;
            if(!e.getCurrentItem().hasItemMeta())
                return;
            if(e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CREATE)) {
                if(e.isRightClick())
                {
                    InventoryManager invMa = ShopSystem.currentInvCreation.get(((Player) e.getWhoClicked()).getUniqueId());
                    ItemClass item = new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, e.getSlot());
                    invMa.addItem(item);
                    ShopSystem.currentInvCreation.put(((Player) e.getWhoClicked()).getUniqueId(), invMa);
                    Inventory in = ((Player) e.getWhoClicked()).getInventory();
                    in.addItem(new ItemStack(e.getCurrentItem().getType()));
                    ((Player) e.getWhoClicked()).updateInventory();
                    //System.out.println("Removed");
                    ShopSystem.currentInvCreation.get(((Player) e.getWhoClicked()).getUniqueId()).openInv(((Player) e.getWhoClicked()));
                    e.setCancelled(true);
                } else
                    e.setCancelled(true);
            }
            if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_NAME))
            {
                Player p = (Player) e.getWhoClicked();

                if(ShopSystem.settingsManager.getConfig().getBoolean("config.UseUUID")) {
                    try {
                        InventoryManager invMa;
                        if (ShopSystem.dataManager.shopInventoryUUID.containsKey(ShopSystem.playerInShopUUID.get(p.getUniqueId()))) {
                            invMa = this.buyItem(ShopSystem.dataManager.shopInventoryUUID.get(ShopSystem.playerInShopUUID.get(p.getUniqueId())), e.getSlot(),p,Bukkit.getPlayer(ShopSystem.dataManager.shopOwnerUUID.get(ShopSystem.playerInShopUUID.get(p.getUniqueId()))));
                        } else {
                            invMa = this.buyItem(ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot(),p,Bukkit.getPlayer(ShopSystem.dataManager.shopOwner.get(ShopSystem.playerInShop.get(p.getUniqueId()))));
                        }
                        ShopSystem.dataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), invMa);
                        ItemStack itemStack = e.getCurrentItem().clone();
                        itemStack.setAmount(1);
                        p.getInventory().addItem(removeLore(itemStack));
                    } catch (ItemBuyException exeption) {
                        exeption.printStackTrace();
                    }catch (NotEnoughMoneyException exception)
                    {

                    }

                    ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())).openUserInv(p);
                }
                else
                {
                    try {
                        InventoryManager invMa = this.buyItem(ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot(), p, Bukkit.getPlayer(ShopSystem.dataManager.shopOwner.get(ShopSystem.playerInShop.get(p.getUniqueId()))));
                        ShopSystem.dataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), invMa);
                        ItemStack itemStack = e.getCurrentItem().clone();
                        itemStack.setAmount(1);
                        p.getInventory().addItem(removeLore(itemStack));
                    } catch (ItemBuyException exeption) {
                        exeption.printStackTrace();
                    } catch (NotEnoughMoneyException exception)
                    {

                    }

                    ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())).openUserInv(p);
                }
                e.setCancelled(true);
            }
            if(!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            if(e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_MENU)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.LEAVE.getName())) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CREATESHOP.getName())) {
                    Player p = (Player) e.getWhoClicked();
                    Inventory inv = e.getClickedInventory();
                    if (inv.getTitle().equalsIgnoreCase(Locale.SHOP_MENU)) {
                        p.closeInventory();
                        Inventory in = Bukkit.createInventory(null, 45, Locale.SHOP_CREATE);
                        in.setItem(44, ItemType.SAVESHOP.getItem());
                        ShopSystem.currentInvCreation.put(p.getUniqueId(), new InventoryManager(45));
                        p.openInventory(in);
                        e.setCancelled(true);

                    }
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.DELETESHOP.getName())) {
                    ShopSystem.deleteShop.put(((Player)e.getWhoClicked()).getUniqueId(), true);
                    ((Player)e.getWhoClicked()).closeInventory();
                    MessageManager.getInstance().msg((Player)e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.ENTER_DELETE_SHOP);
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CANTCREATESHOP.getName())) {
                    Player p = (Player) e.getWhoClicked();
                    p.closeInventory();
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.EDITSHOP.getName())) {
                    Player p = (Player) e.getWhoClicked();
                    if(ShopSystem.settingsManager.getConfig().getBoolean("config.UseUUID")) {
                        if (ShopSystem.dataManager.shopInventoryUUID.containsKey(ShopSystem.playerInShopUUID.get(p.getUniqueId()))) {
                            ShopSystem.dataManager.shopInventoryUUID.get(ShopSystem.playerInShopUUID.get(p.getUniqueId())).openInv(p);
                            ShopSystem.currentInvCreation.put(p.getUniqueId(), ShopSystem.dataManager.shopInventoryUUID.get(ShopSystem.playerInShopUUID.get(p.getUniqueId())));
                        } else {
                            if (ShopSystem.dataManager.shopInventory.containsKey(ShopSystem.playerInShop.get(p.getUniqueId()))) {
                                ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())).openInv(p);
                                ShopSystem.currentInvCreation.put(p.getUniqueId(), ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())));
                            }
                        }
                    } else {
                        if (ShopSystem.dataManager.shopInventory.containsKey(ShopSystem.playerInShop.get(p.getUniqueId()))) {
                            ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())).openInv(p);
                            ShopSystem.currentInvCreation.put(p.getUniqueId(), ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())));
                        }
                    }
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.SHOWSHOP.getName())) {
                    Player p = (Player) e.getWhoClicked();
                    if(ShopSystem.settingsManager.getConfig().getBoolean("config.UseUUID")) {
                        try {
                            ShopSystem.dataManager.shopInventoryUUID.get(ShopSystem.playerInShopUUID.get(p.getUniqueId())).openUserInv(p);
                        } catch (Exception ex) {
                            ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())).openUserInv(p);

                        }
                    }
                    else {
                        ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())).openUserInv(p);
                    }
                    e.setCancelled(true);
                }
                if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CHANGENAME.getName()))
                {
                    Player p = (Player) e.getWhoClicked();
                    ShopSystem.editName.put(p.getUniqueId(), true);
                    MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.ENTER_NAME);
                    e.setCancelled(true);
                    p.closeInventory();
                }
                if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CHANGEPROFESSION.getName()))
                {
                    for (Entity entity : ShopSystem.playerInShop.get(((Player) e.getWhoClicked()).getUniqueId()).getChunk().getEntities()) {
                        Location loc = entity.getLocation();
                        Location vLoc = ShopSystem.playerInShop.get(((Player) e.getWhoClicked()).getUniqueId());
                        if (vLoc.getBlockX() == loc.getBlockX() && vLoc.getBlockY() == loc.getBlockY() && vLoc.getBlockZ() == loc.getBlockZ() && vLoc.getWorld() == loc.getWorld()) {
                            if (entity instanceof Villager) {
                                Villager v = (Villager) entity;
                                VillagerClass.switchProfession(v ,((Player)e.getWhoClicked()));
                                Inventory inv = Bukkit.createInventory(null, 9, Locale.SHOP_MENU);
                                inv.setItem(0, ItemType.EDITSHOP.getItem());
                                inv.setItem(1, ItemType.DELETESHOP.getItem());
                                if(((Player)e.getWhoClicked()).hasPermission("shopsystem.shop.changeName"))
                                    inv.setItem(3, ItemType.CHANGENAME.getItem());
                                inv.setItem(4, ItemType.SHOWSHOP.getItem());
                                inv.setItem(6, new ItemCreation(Locale.ITEM_CHANGE_PROFESSION, Material.WOOL, null, 1, VillagerClass.getProfession((Player) e.getWhoClicked())).getItem());
                                inv.setItem(8, ItemType.LEAVE.getItem());
                                ((Player)e.getWhoClicked()).closeInventory();
                                ((Player)e.getWhoClicked()).openInventory(inv);
                            }
                        }
                    }
                   e.setCancelled(true);
                }
            }

            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.SAVESHOP.getName()))
            {
                if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CREATE))
                {
                    UUID uuid = ((Player)e.getWhoClicked()).getUniqueId();
                    ShopSystem.dataManager.saveByLocation(ShopSystem.playerInShop.get(uuid), uuid, ShopSystem.currentInvCreation.get(uuid), ShopSystem.playerInShopUUID.get(uuid), true);
                    MessageManager.getInstance().msg((Player)e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.SHOP_SAVED_SUCCESS);
                    ((Player)e.getWhoClicked()).closeInventory();
                    ShopSystem.dataManager.shopInventory.put(ShopSystem.playerInShop.get(uuid), ShopSystem.currentInvCreation.get(uuid));
                    ShopSystem.dataManager.shopInventoryUUID.put(ShopSystem.playerInShopUUID.get(uuid), ShopSystem.currentInvCreation.get(uuid));
                    ShopSystem.dataManager.shopOwner.put(ShopSystem.playerInShop.get(uuid), uuid);
                    ShopSystem.dataManager.shop.put(ShopSystem.playerInShop.get(uuid), ShopSystem.playerInShopUUID.get(uuid));
                    if(ShopSystem.settingsManager.getConfig().getBoolean("config.UseUUID")) {
                        boolean validUUID = false;
                        for (Entity entity : ShopSystem.playerInShop.get(uuid).getWorld().getEntities()) {
                            if (ShopSystem.playerInShopUUID.containsValue(entity.getUniqueId())) {
                                if (entity instanceof Villager) {
                                    ShopSystem.dataManager.shopOwnerUUID.put(entity.getUniqueId(), uuid);
                                    Villager v = (Villager) entity;
                                    if (v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT)) {
                                        v.setCustomName(((Player) e.getWhoClicked()).getName() + "'s Shop");
                                        v.setCustomNameVisible(true);
                                        //System.out.println("Changed Name");
                                        int amount = ShopSystem.dataManager.shopAmount.get(uuid);
                                        ShopSystem.dataManager.shopAmount.put(uuid, amount + 1);
                                        validUUID = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!validUUID) {
                            for (Entity entity : ShopSystem.playerInShop.get(((Player) e.getWhoClicked()).getUniqueId()).getChunk().getEntities()) {
                                Location loc = entity.getLocation();
                                Location vLoc = ShopSystem.playerInShop.get(((Player) e.getWhoClicked()).getUniqueId());
                                if (vLoc.getBlockX() == loc.getBlockX() && vLoc.getBlockY() == loc.getBlockY() && vLoc.getBlockZ() == loc.getBlockZ() && vLoc.getWorld() == loc.getWorld()) {
                                    if (entity instanceof Villager) {
                                        Villager v = (Villager) entity;
                                        if (v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT)) {
                                            v.setCustomName(((Player) e.getWhoClicked()).getName() + "'s Shop");
                                            v.setCustomNameVisible(true);
                                            int amount = ShopSystem.dataManager.shopAmount.get(uuid);
                                            ShopSystem.dataManager.shopAmount.put(uuid, amount + 1);
                                            //System.out.println("Changed Name");

                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        //System.out.println("Save Shop");
                        e.setCancelled(true);
                    } else
                    {
                        for (Entity entity : ShopSystem.playerInShop.get(((Player) e.getWhoClicked()).getUniqueId()).getChunk().getEntities()) {
                            Location loc = entity.getLocation();
                            Location vLoc = ShopSystem.playerInShop.get(((Player) e.getWhoClicked()).getUniqueId());
                            if (vLoc.getBlockX() == loc.getBlockX() && vLoc.getBlockY() == loc.getBlockY() && vLoc.getBlockZ() == loc.getBlockZ() && vLoc.getWorld() == loc.getWorld()) {
                                if (entity instanceof Villager) {
                                    Villager v = (Villager) entity;
                                    if (v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT)) {
                                        v.setCustomName(((Player) e.getWhoClicked()).getName() + "'s Shop");
                                        v.setCustomNameVisible(true);
                                        int amount = ShopSystem.dataManager.shopAmount.get(uuid);
                                        ShopSystem.dataManager.shopAmount.put(uuid, amount + 1);
                                        //System.out.println("Changed Name");
                                        //ShopSystem.dataManager.saveByLocation();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            } else
            {



            }
        }
    }

    public InventoryManager buyItem(InventoryManager inventoryManager, int slot, Player buyer, Player seller) throws ItemBuyException, NotEnoughMoneyException
    {
        ArrayList<ItemClass> items = inventoryManager.getItems();
        ItemClass item = items.get(slot);
        try {
            EconomyHandler.transferMoney(buyer, seller, item.getPrice());
        } catch (NotEnoughMoneyException ex)
        {
            throw new NotEnoughMoneyException();
        }
        if(item.getAmount() == 1)
        {
            if(EconomyHandler.hasRequiredAmount(buyer, item.getPrice()))
                inventoryManager.addItem(new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, slot));
        }
        else
        {
            if(item.getAmount()>1)
            {
                if(EconomyHandler.hasRequiredAmount(buyer, item.getPrice())) {
                    item.setAmount(item.getAmount() - 1);
                    inventoryManager.addItem(item);
                }
            }
            else
                throw new ItemBuyException("Invalid item amount");
        }


        return inventoryManager;
    }

    public ItemStack removeLore(ItemStack item)
    {
        if(item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            if(meta.hasLore())
            {
                meta.setLore(null);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

}
