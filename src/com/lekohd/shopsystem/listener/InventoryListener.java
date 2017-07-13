package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopGUI;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.entity.SkeletonClass;
import com.lekohd.shopsystem.entity.ZombieClass;
import com.lekohd.shopsystem.exception.ItemBuyException;
import com.lekohd.shopsystem.exception.NotEnoughMoneyException;
import com.lekohd.shopsystem.handler.EconomyHandler;
import com.lekohd.shopsystem.item.ItemClass;
import com.lekohd.shopsystem.manager.DataManager;
import com.lekohd.shopsystem.manager.EntityManager;
import com.lekohd.shopsystem.manager.InventoryManager;
import com.lekohd.shopsystem.manager.MessageManager;
import com.lekohd.shopsystem.manager.MessageManager.MessageType;
import com.lekohd.shopsystem.manager.SettingsManager;
import com.lekohd.shopsystem.util.ItemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lekohd.shopsystem.util.ShopMode;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class InventoryListener
        implements Listener
{
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e)
    {
        if ((e.isLeftClick()) || (e.isRightClick()) || (e.isShiftClick()))
        {
            if (e.getClickedInventory() == null) return;
            if ((e.getAction() == InventoryAction.PLACE_ALL) || (e.getAction() == InventoryAction.PLACE_SOME) || (e.getAction() == InventoryAction.PLACE_ONE))
            {
                if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_NAME))
                {
                    e.setCancelled(true);
                    return;
                }
                if (!e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CREATE)) return;
                ShopSystem.isEditingShop.put(((Player)e.getWhoClicked()).getUniqueId(), Boolean.valueOf(true));
                InventoryManager invMa = (InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId());
                ItemClass item = new ItemClass(e.getCursor(), e.getCursor().getAmount(), 0, e.getSlot());
                item.getItem().setAmount(1);
                ShopSystem.currentItem.put(((Player)e.getWhoClicked()).getUniqueId(), item);
                invMa.addItem(item);
                ShopSystem.currentInvCreation.put(((Player)e.getWhoClicked()).getUniqueId(), invMa);
                ShopSystem.creatingInv.put(((Player)e.getWhoClicked()).getUniqueId(), Boolean.valueOf(true));
                ShopSystem.currentSlot.put(((Player)e.getWhoClicked()).getUniqueId(), Integer.valueOf(e.getSlot()));
                MessageManager.getInstance().msg((Player)e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.ENTER_PRICE);
                e.setCurrentItem(new ItemStack(Material.AIR));
                e.setCancelled(true);
                ((Player)e.getWhoClicked()).closeInventory();
                return;
            }
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CREATE)) {
                if (e.isLeftClick())
                {
                    if ((!e.getCurrentItem().hasItemMeta()) || (!e.getCurrentItem().getItemMeta().hasDisplayName()) || (!e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Locale.ITEM_SHOP_SAVE))) {
                        if (e.getCursor() != null)
                        {
                            if ((e.getCursor().getType() == e.getCurrentItem().getType()) && (e.getCursor().getData().getData() == e.getCurrentItem().getData().getData()) && (sameEnchantments(e.getCursor().getEnchantments(), e.getCurrentItem().getEnchantments())))
                            {
                                InventoryManager invMa = (InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId());
                                ItemClass pItem = (ItemClass)invMa.getItems().get(e.getSlot());
                                pItem.setAmount(pItem.getAmount() + e.getCursor().getAmount());
                                invMa.addItem(pItem);
                                ShopSystem.currentInvCreation.put(((Player)e.getWhoClicked()).getUniqueId(), invMa);

                                ShopSystem.isEditingShop.put(((Player)e.getWhoClicked()).getUniqueId(), Boolean.valueOf(true));
                                ((Player)e.getWhoClicked()).closeInventory();
                                ((InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId())).openInv((Player)e.getWhoClicked());
                                e.setCancelled(true);
                                return;
                            }
                        }

                        InventoryManager invMa = (InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId());
                        ItemClass pItem = (ItemClass)invMa.getItems().get(e.getSlot());
                        ItemClass item = new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, e.getSlot());
                        invMa.addItem(item);
                        ShopSystem.currentInvCreation.put(((Player)e.getWhoClicked()).getUniqueId(), invMa);
                        Inventory in = ((Player)e.getWhoClicked()).getInventory();
                        ItemStack rItem = pItem.getItem();
                        rItem.setAmount(pItem.getAmount());
                        in.addItem(new ItemStack[] { removeLore(rItem) });
                        ((Player)e.getWhoClicked()).updateInventory();

                        ((InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId())).openInv((Player)e.getWhoClicked());
                        e.setCancelled(true);
                        return;
                    }
                }
                else if (e.isRightClick())
                {
                    if ((!e.getCurrentItem().hasItemMeta()) || (!e.getCurrentItem().getItemMeta().hasDisplayName()) || (!e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Locale.ITEM_SHOP_SAVE))) {
                        if (e.getCursor() != null)
                        {
                            if ((e.getCursor().getType() == e.getCurrentItem().getType()) && (e.getCursor().getData().getData() == e.getCurrentItem().getData().getData()) && (sameEnchantments(e.getCursor().getEnchantments(), e.getCurrentItem().getEnchantments())))
                            {
                                InventoryManager invMa = (InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId());
                                ItemClass pItem = (ItemClass)invMa.getItems().get(e.getSlot());
                                pItem.setAmount(pItem.getAmount() + e.getCursor().getAmount());
                                invMa.addItem(pItem);
                                ShopSystem.currentInvCreation.put(((Player)e.getWhoClicked()).getUniqueId(), invMa);

                                ShopSystem.isEditingShop.put(((Player)e.getWhoClicked()).getUniqueId(), Boolean.valueOf(true));
                                ((Player)e.getWhoClicked()).closeInventory();
                                ((InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId())).openInv((Player)e.getWhoClicked());
                                e.setCancelled(true);
                                return;
                            }
                        }
                        ShopSystem.isEditingShop.put(((Player)e.getWhoClicked()).getUniqueId(), Boolean.valueOf(true));
                        InventoryManager invMa = (InventoryManager)ShopSystem.currentInvCreation.get(((Player)e.getWhoClicked()).getUniqueId());
                        ItemClass item = (ItemClass)invMa.getItems().get(e.getSlot());
                        ShopSystem.currentItem.put(((Player)e.getWhoClicked()).getUniqueId(), item);
                        invMa.addItem(item);
                        ShopSystem.currentInvCreation.put(((Player)e.getWhoClicked()).getUniqueId(), invMa);
                        ShopSystem.creatingInv.put(((Player)e.getWhoClicked()).getUniqueId(), Boolean.valueOf(true));
                        ShopSystem.currentSlot.put(((Player)e.getWhoClicked()).getUniqueId(), Integer.valueOf(e.getSlot()));
                        MessageManager.getInstance().msg((Player)e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.ENTER_PRICE);
                        e.setCurrentItem(new ItemStack(Material.AIR));
                        e.setCancelled(true);
                        ((Player)e.getWhoClicked()).closeInventory();
                        return;
                    }
                }
                else
                    e.setCancelled(true);
            }
            if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_NAME))
            {
                Player p = (Player)e.getWhoClicked();
                if (e.isRightClick())
                    try {
                        UUID owner = (UUID)DataManager.shopOwner.get(ShopSystem.playerInShop.get(p.getUniqueId()));
                        InventoryManager invMa;
                        //InventoryManager invMa;
                        if (isOnline(owner))
                            invMa = this.buyItem((InventoryManager) DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot(), p, Bukkit.getPlayer(owner), false);
                        else
                            invMa = buyItem((InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot(), p, owner, false);
                        DataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), invMa);
                        ItemStack itemStack = e.getCurrentItem().clone();
                        itemStack.setAmount(1);
                        p.getInventory().addItem(new ItemStack[] { removeLore(itemStack) });
                    } catch (ItemBuyException exeption) {
                        exeption.printStackTrace();
                    }
                    catch (NotEnoughMoneyException exception) {
                    }
                else if (e.isLeftClick()) {
                    try
                    {
                        UUID owner = (UUID)DataManager.shopOwner.get(ShopSystem.playerInShop.get(p.getUniqueId()));
                        InventoryManager invMa = (InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId()));
                        ItemClass item = (ItemClass)invMa.getItems().get(e.getSlot());
                        invMa = null;
                        if (isOnline(owner))
                            invMa = buyItem((InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot(), p, Bukkit.getPlayer(owner), true);
                        else
                            invMa = buyItem((InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())), e.getSlot(), p, owner, true);
                        DataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), invMa);
                        ItemStack itemStack = item.getItem();
                        itemStack.setAmount(item.getAmount());
                        p.getInventory().addItem(new ItemStack[] { removeLore(itemStack) });
                    } catch (ItemBuyException exeption) {
                        exeption.printStackTrace();
                    }
                    catch (NotEnoughMoneyException exception)
                    {
                    }
                }
                ((InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId()))).openUserInv(p);
                e.setCancelled(true);
            }
            if (!e.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_MENU)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.LEAVE.getName())) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CHANGEARMORMENU.getName()))
                {
                    Player p = (Player)e.getWhoClicked();
                    e.setCancelled(true);
                    ShopGUI.openArmorMenu(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CHANGEENTITY.getName()))
                {
                    Player p = (Player)e.getWhoClicked();
                    e.setCancelled(true);
                    EntityManager.changeEntity(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CREATESHOP.getName())) {
                    Player p = (Player)e.getWhoClicked();
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
                    ShopSystem.deleteShop.put(((Player)e.getWhoClicked()).getUniqueId(), Boolean.valueOf(true));
                    ((Player)e.getWhoClicked()).closeInventory();
                    MessageManager.getInstance().msg((Player)e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.ENTER_DELETE_SHOP);
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CANTCREATESHOP.getName())) {
                    Player p = (Player)e.getWhoClicked();
                    p.closeInventory();
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.EDITSHOP.getName())) {
                    Player p = (Player)e.getWhoClicked();
                    if (DataManager.shopInventory.containsKey(ShopSystem.playerInShop.get(p.getUniqueId()))) {
                        ((InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId()))).openInv(p);
                        ShopSystem.currentInvCreation.put(p.getUniqueId(), DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId())));
                    }
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.SHOWSHOP.getName())) {
                    Player p = (Player)e.getWhoClicked();
                    if (ShopSystem.settingsManager.getConfig().getBoolean("config.UseUUID")) {
                        try {
                            ((InventoryManager)DataManager.shopInventoryUUID.get(ShopSystem.playerInShopUUID.get(p.getUniqueId()))).openUserInv(p);
                        } catch (Exception ex) {
                            ((InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId()))).openUserInv(p);
                        }
                    }
                    else
                    {
                        ((InventoryManager)DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId()))).openUserInv(p);
                    }
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CHANGEMODE.getName()))
                {
                    Player p = (Player)e.getWhoClicked();

                    //TODO: Buy, Sell mode

                    InventoryManager inventoryManager = DataManager.shopInventory.get(ShopSystem.playerInShop.get(p.getUniqueId()));

                    if (inventoryManager.getShopMode().equals(ShopMode.BUY))
                    {
                        inventoryManager.setShopMode(ShopMode.SELL);
                        DataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), inventoryManager);
                        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.SELL_MODE);
                    } else if (inventoryManager.getShopMode().equals(ShopMode.SELL))
                    {
                        inventoryManager.setShopMode(ShopMode.BUY);
                        DataManager.shopInventory.put(ShopSystem.playerInShop.get(p.getUniqueId()), inventoryManager);
                        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.BUY_MODE);
                    }
                    e.setCancelled(true);
                    p.closeInventory();
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CHANGENAME.getName()))
                {
                    Player p = (Player)e.getWhoClicked();
                    ShopSystem.editName.put(p.getUniqueId(), true);
                    MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.ENTER_NAME);
                    e.setCancelled(true);
                    p.closeInventory();
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.CHANGEPROFESSION.getName()))
                {
                    ShopGUI.changeProfession((Player)e.getWhoClicked());
                    e.setCancelled(true);
                }
            }

            if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CHANGE_ARMOR))
            {
                ItemStack item = e.getCurrentItem();
                String name = item.getItemMeta().getDisplayName();
                Player p = (Player)e.getWhoClicked();
                Entity shop = EntityManager.getEntityByPlayer(p);
                if (name.equalsIgnoreCase(ItemType.ADDARMOR.getName()))
                {
                    e.setCancelled(true);
                    if ((shop instanceof Villager))
                    {
                        ShopGUI.setAdminMenu(p, shop);
                    }
                    else if ((shop instanceof Zombie))
                    {
                        ShopSystem.editArmor.put(p.getUniqueId(), Boolean.valueOf(true));
                        p.closeInventory();
                        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.ENTER_CLICK_ARMOR);
                    }
                    else if ((shop instanceof Skeleton))
                    {
                        ShopSystem.editArmor.put(p.getUniqueId(), Boolean.valueOf(true));
                        p.closeInventory();
                        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, Locale.ENTER_CLICK_ARMOR);
                    }
                }

                if (name.equalsIgnoreCase(ItemType.REMOVEHELMET.getName()))
                {
                    e.setCancelled(true);
                    if ((shop instanceof Villager))
                    {
                        ShopGUI.setAdminMenu(p, shop);
                    }
                    else if ((shop instanceof Zombie))
                    {
                        ZombieClass.removeHelmet((Zombie)shop);
                    }
                    else if ((shop instanceof Skeleton))
                    {
                        SkeletonClass.removeHelmet((Skeleton)shop);
                    }
                }
                if (name.equalsIgnoreCase(ItemType.REMOVECHESTPLATE.getName()))
                {
                    e.setCancelled(true);
                    if ((shop instanceof Villager))
                    {
                        ShopGUI.setAdminMenu(p, shop);
                    }
                    else if ((shop instanceof Zombie))
                    {
                        ZombieClass.removeChestPlate((Zombie)shop);
                    }
                    else if ((shop instanceof Skeleton))
                    {
                        SkeletonClass.removeChestPlate((Skeleton)shop);
                    }
                }
                if (name.equalsIgnoreCase(ItemType.REMOVELEGGINGS.getName()))
                {
                    e.setCancelled(true);
                    if ((shop instanceof Villager))
                    {
                        ShopGUI.setAdminMenu(p, shop);
                    }
                    else if ((shop instanceof Zombie))
                    {
                        ZombieClass.removeLeggings((Zombie)shop);
                    }
                    else if ((shop instanceof Skeleton))
                    {
                        SkeletonClass.removeLeggings((Skeleton)shop);
                    }
                }
                if (name.equalsIgnoreCase(ItemType.REMOVEBOOTS.getName()))
                {
                    e.setCancelled(true);
                    if ((shop instanceof Villager))
                    {
                        ShopGUI.setAdminMenu(p, shop);
                    }
                    else if ((shop instanceof Zombie))
                    {
                        ZombieClass.removeBoots((Zombie)shop);
                    }
                    else if ((shop instanceof Skeleton))
                    {
                        SkeletonClass.removeBoots((Skeleton)shop);
                    }
                }
                if (name.equalsIgnoreCase(ItemType.REMOVEALLARMOR.getName()))
                {
                    e.setCancelled(true);
                    if ((shop instanceof Villager))
                    {
                        ShopGUI.setAdminMenu(p, shop);
                    }
                    else if ((shop instanceof Zombie))
                    {
                        ZombieClass.removeHelmet((Zombie)shop);
                        ZombieClass.removeChestPlate((Zombie)shop);
                        ZombieClass.removeLeggings((Zombie)shop);
                        ZombieClass.removeBoots((Zombie)shop);
                    }
                    else if ((shop instanceof Skeleton))
                    {
                        SkeletonClass.removeHelmet((Skeleton)shop);
                        SkeletonClass.removeChestPlate((Skeleton)shop);
                        SkeletonClass.removeLeggings((Skeleton)shop);
                        SkeletonClass.removeBoots((Skeleton)shop);
                    }
                }
                if (name.equalsIgnoreCase(ItemType.BACK.getName()))
                {
                    e.setCancelled(true);
                    ShopGUI.setAdminMenu(p, shop);
                }
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemType.SAVESHOP.getName()))
            {
                if (e.getClickedInventory().getTitle().equalsIgnoreCase(Locale.SHOP_CREATE))
                {
                    UUID uuid = ((Player)e.getWhoClicked()).getUniqueId();
                    DataManager.saveByLocation((Location)ShopSystem.playerInShop.get(uuid), uuid, (InventoryManager)ShopSystem.currentInvCreation.get(uuid), (UUID)ShopSystem.playerInShopUUID.get(uuid), true);
                    MessageManager.getInstance().msg((Player)e.getWhoClicked(), MessageManager.MessageType.INFO, Locale.SHOP_SAVED_SUCCESS);
                    ((Player)e.getWhoClicked()).closeInventory();
                    DataManager.shopInventory.put(ShopSystem.playerInShop.get(uuid), ShopSystem.currentInvCreation.get(uuid));
                    DataManager.shopInventoryUUID.put(ShopSystem.playerInShopUUID.get(uuid), ShopSystem.currentInvCreation.get(uuid));
                    DataManager.shopOwner.put(ShopSystem.playerInShop.get(uuid), uuid);
                    DataManager.shop.put(ShopSystem.playerInShop.get(uuid), ShopSystem.playerInShopUUID.get(uuid));
                    if (ShopSystem.settingsManager.getConfig().getBoolean("config.UseUUID")) {
                        boolean validUUID = false;
                        for (Entity entity : ((Location)ShopSystem.playerInShop.get(uuid)).getWorld().getEntities()) {
                            if ((ShopSystem.playerInShopUUID.containsValue(entity.getUniqueId())) &&
                                    ((entity instanceof Villager || entity instanceof Zombie || entity instanceof Skeleton))) {
                                DataManager.shopOwnerUUID.put(entity.getUniqueId(), uuid);
                                Entity v = (Entity)entity;
                                if (v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT)) {
                                    v.setCustomName(((Player)e.getWhoClicked()).getName() + "'s Shop");
                                    v.setCustomNameVisible(true);

                                    int amount = ((Integer)DataManager.shopAmount.get(uuid)).intValue();
                                    DataManager.shopAmount.put(uuid, Integer.valueOf(amount + 1));
                                    validUUID = true;
                                    break;
                                }
                            }
                        }

                        if (!validUUID) {
                            for (Entity entity : ((Location)ShopSystem.playerInShop.get(((Player)e.getWhoClicked()).getUniqueId())).getChunk().getEntities()) {
                                Location loc = entity.getLocation();
                                Location vLoc = (Location)ShopSystem.playerInShop.get(((Player)e.getWhoClicked()).getUniqueId());
                                if ((vLoc.getBlockX() == loc.getBlockX()) && (vLoc.getBlockY() == loc.getBlockY()) && (vLoc.getBlockZ() == loc.getBlockZ()) && (vLoc.getWorld() == loc.getWorld()) &&
                                        ((entity instanceof Villager || entity instanceof Zombie || entity instanceof Skeleton))) {
                                    Entity v = (Entity)entity;
                                    if (v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT)) {
                                        v.setCustomName(((Player)e.getWhoClicked()).getName() + "'s Shop");
                                        v.setCustomNameVisible(true);
                                        int amount = ((Integer)DataManager.shopAmount.get(uuid)).intValue();
                                        DataManager.shopAmount.put(uuid, Integer.valueOf(amount + 1));

                                        return;
                                    }
                                }
                            }

                        }

                        e.setCancelled(true);
                    }
                    else {
                        for (Entity entity : ((Location)ShopSystem.playerInShop.get(((Player)e.getWhoClicked()).getUniqueId())).getChunk().getEntities()) {
                            Location loc = entity.getLocation();
                            Location vLoc = (Location)ShopSystem.playerInShop.get(((Player)e.getWhoClicked()).getUniqueId());
                            if ((vLoc.getBlockX() == loc.getBlockX()) && (vLoc.getBlockY() == loc.getBlockY()) && (vLoc.getBlockZ() == loc.getBlockZ()) && (vLoc.getWorld() == loc.getWorld()) &&
                                    ((entity instanceof Villager || entity instanceof Zombie || entity instanceof Skeleton))) {
                                Entity v = (Entity)entity;
                                if (v.getCustomName().equalsIgnoreCase(Locale.SHOP_GET_IT)) {
                                    v.setCustomName(((Player)e.getWhoClicked()).getName() + "'s Shop");
                                    v.setCustomNameVisible(true);
                                    int amount = ((Integer)DataManager.shopAmount.get(uuid)).intValue();
                                    DataManager.shopAmount.put(uuid, Integer.valueOf(amount + 1));

                                    return;
                                }
                            }
                        }
                    }

                    e.setCancelled(true);
                }
            }
        }
    }

    public InventoryManager buyItem(InventoryManager inventoryManager, int slot, Player buyer, Player seller, boolean buyAll) throws ItemBuyException, NotEnoughMoneyException
    {
        ArrayList items = inventoryManager.getItems();
        ItemClass item = (ItemClass)items.get(slot);
        if(inventoryManager.getShopMode().equals(ShopMode.BUY)) {
            boolean requiredAmount = EconomyHandler.hasRequiredAmount(buyer, item.getPrice());
            if (buyAll)
                requiredAmount = EconomyHandler.hasRequiredAmount(buyer, item.getPrice() * item.getAmount());
            if (item.getAmount() == -1) {
                return inventoryManager;
            }
            try {
                if (buyAll) {
                    EconomyHandler.transferMoney(buyer, seller, item.getPrice() * item.getAmount());
                } else EconomyHandler.transferMoney(buyer, seller, item.getPrice());
            } catch (NotEnoughMoneyException ex) {
                throw new NotEnoughMoneyException();
            }
            if (buyAll) {
                if (((requiredAmount) && ((!seller.isOp()) || (!seller.hasPermission("shopsystem.admin")))) || (!ShopSystem.settingsManager.getConfig().getBoolean("config.EnableOpShops")))
                    inventoryManager.addItem(new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, slot));
                return inventoryManager;
            }
            if (item.getAmount() == 1) {
                if (((requiredAmount) && ((!seller.isOp()) || (!seller.hasPermission("shopsystem.admin")))) || (!ShopSystem.settingsManager.getConfig().getBoolean("config.EnableOpShops"))) {
                    inventoryManager.addItem(new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, slot));
                }

            } else if (item.getAmount() > 1) {
                if (((requiredAmount) && ((!seller.isOp()) || (!seller.hasPermission("shopsystem.admin")))) || (!ShopSystem.settingsManager.getConfig().getBoolean("config.EnableOpShops"))) {
                    item.setAmount(item.getAmount() - 1);
                    inventoryManager.addItem(item);
                }
            } else {
                throw new ItemBuyException("Invalid item amount");
            }
        } else if (inventoryManager.getShopMode().equals(ShopMode.SELL))
        {
            //TODO: Moving to the Listener, creating method sellItem
        }
        return inventoryManager;
    }

    public InventoryManager buyItem(InventoryManager inventoryManager, int slot, Player buyer, UUID seller, boolean buyAll) throws ItemBuyException, NotEnoughMoneyException
    {
        ArrayList items = inventoryManager.getItems();
        ItemClass item = (ItemClass)items.get(slot);
        boolean requiredAmount = EconomyHandler.hasRequiredAmount(buyer, item.getPrice());
        if (buyAll)
            requiredAmount = EconomyHandler.hasRequiredAmount(buyer, item.getPrice() * item.getAmount());
        if (item.getAmount() == -1)
        {
            return inventoryManager;
        }
        try {
            if (buyAll)
            {
                EconomyHandler.transferMoney(buyer, seller, item.getPrice() * item.getAmount());
            }
            else EconomyHandler.transferMoney(buyer, seller, item.getPrice());
        }
        catch (NotEnoughMoneyException ex)
        {
            throw new NotEnoughMoneyException();
        }
        OfflinePlayer p = Bukkit.getOfflinePlayer(seller);
        if (buyAll)
        {
            if (((requiredAmount) && (!p.isOp())) || (!ShopSystem.settingsManager.getConfig().getBoolean("config.EnableOpShops")))
                inventoryManager.addItem(new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, slot));
            return inventoryManager;
        }
        if (item.getAmount() == 1)
        {
            if (((requiredAmount) && (!p.isOp())) || (!ShopSystem.settingsManager.getConfig().getBoolean("config.EnableOpShops"))) {
                inventoryManager.addItem(new ItemClass(new ItemStack(Material.THIN_GLASS), -1, -1, slot));
            }

        }
        else if (item.getAmount() > 1)
        {
            if (((requiredAmount) && (!p.isOp())) || (!ShopSystem.settingsManager.getConfig().getBoolean("config.EnableOpShops"))) {
                item.setAmount(item.getAmount() - 1);
                inventoryManager.addItem(item);
            }
        }
        else if (item.getAmount() < -1) {
            throw new ItemBuyException("Invalid item amount");
        }

        return inventoryManager;
    }

    public ItemStack removeLore(ItemStack item)
    {
        if (item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore())
            {
                meta.setLore(null);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isOnline(UUID uuid)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getUniqueId().toString().equalsIgnoreCase(uuid.toString()))
                return true;
        }
        return false;
    }

    public boolean sameEnchantments(Map<Enchantment, Integer> one, Map<Enchantment, Integer> two)
    {
        if ((one.isEmpty()) && (two.isEmpty()))
            return true;
        for (Enchantment enchantment : one.keySet())
        {
            if (two.containsKey(enchantment))
            {
                if (two.get(enchantment) != one.get(enchantment))
                {
                    return false;
                }
            } else return false;
        }
        return true;
    }
}