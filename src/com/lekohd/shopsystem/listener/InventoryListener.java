package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.exception.ItemBuyException;
import com.lekohd.shopsystem.item.ItemClass;
import com.lekohd.shopsystem.manager.InventoryManager;
import com.lekohd.shopsystem.manager.MessageManager;
import com.lekohd.shopsystem.util.ItemType;
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
import sun.nio.cs.ext.SJIS;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by thorstenkoth on 26.05.15.
 */
public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e)
    {
        if(e.isLeftClick() || e.isRightClick() || e.isShiftClick())
        {
            if(e.getClickedInventory() == null) return;
            if(e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_SOME || e.getAction() == InventoryAction.PLACE_ONE)
            {
                if(!e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CREATE)) return;
                InventoryManager invMa = ShopSystem.currentInvCreation.get(((Player) e.getWhoClicked()).getUniqueId());
                ItemClass item = new ItemClass(e.getCursor(), e.getCursor().getAmount(), 0, e.getSlot());
                ShopSystem.currentItem.put(((Player)e.getWhoClicked()).getUniqueId(), item);
                invMa.addItem(item);
                ShopSystem.currentInvCreation.put(((Player) e.getWhoClicked()).getUniqueId(), invMa);
                ((Player) e.getWhoClicked()).closeInventory();
                ShopSystem.creatingInv.put(((Player) e.getWhoClicked()).getUniqueId(), true);
                ShopSystem.currentSlot.put(((Player) e.getWhoClicked()).getUniqueId(), e.getSlot());
                MessageManager.getInstance().msg((Player) e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.ENTER_PRICE);
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
                    System.out.println("Removed");
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
                            invMa = this.buyItem(ShopSystem.dataManager.shopInventoryUUID.get(ShopSystem.playerInShopUUID.get(p.getUniqueId())), e.getSlot());
                        } else {
                            invMa = this.buyItem(ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot());
                        }
                        ShopSystem.dataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), invMa);
                    } catch (ItemBuyException exeption) {
                        exeption.printStackTrace();
                    }
                    p.getInventory().addItem(e.getCurrentItem());
                    ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())).openUserInv(p);
                }
                else
                {
                    try {
                    InventoryManager invMa = this.buyItem(ShopSystem.dataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot());
                    ShopSystem.dataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), invMa);
                    } catch (ItemBuyException exeption) {
                        exeption.printStackTrace();
                    }
                    p.getInventory().addItem(e.getCurrentItem());
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

    public InventoryManager buyItem(InventoryManager inventoryManager, int slot) throws ItemBuyException
    {
        ArrayList<ItemClass> items = inventoryManager.getItems();
        ItemClass item = items.get(slot);
        if(item.getAmount() == 1)
        {
            inventoryManager.addItem(new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, slot));
        }
        else
        {
            if(item.getAmount()>1)
            {
                item.setAmount(item.getAmount()-1);
                inventoryManager.addItem(item);
            }
            else
                throw new ItemBuyException("Invalid item amount");
        }
        return inventoryManager;
    }

}
