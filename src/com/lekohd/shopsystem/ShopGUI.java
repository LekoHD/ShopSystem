package com.lekohd.shopsystem;

import com.lekohd.shopsystem.item.ItemCreation;
import com.lekohd.shopsystem.util.ItemType;
import com.lekohd.shopsystem.entity.VillagerClass;
import net.minecraft.server.v1_8_R2.Village;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;

/**
 * Created by Leon on 03.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ShopGUI {

    public static void setAdminMenu(Player p, Entity e) {
        Villager v;
        Inventory inv = Bukkit.createInventory(null, 9, Locale.SHOP_MENU);
        inv.setItem(0, ItemType.EDITSHOP.getItem());
        inv.setItem(1, ItemType.DELETESHOP.getItem());
        if (p.hasPermission("shopsystem.shop.changeName"))
            inv.setItem(3, ItemType.CHANGENAME.getItem());
        inv.setItem(4, ItemType.SHOWSHOP.getItem());
        if (p.hasPermission("shopsystem.shop.changeProfession") && e instanceof Villager) {
                v = (Villager)e;
            if (v.getProfession() == Villager.Profession.FARMER)
                VillagerClass.professionByUUID.put(p.getUniqueId(), 0);
            if (v.getProfession() == Villager.Profession.LIBRARIAN)
                VillagerClass.professionByUUID.put(p.getUniqueId(), 1);
            if (v.getProfession() == Villager.Profession.PRIEST)
                VillagerClass.professionByUUID.put(p.getUniqueId(), 2);
            inv.setItem(6, new ItemCreation(Locale.ITEM_CHANGE_PROFESSION, Material.WOOL, null, 1, VillagerClass.getProfession(p), true, null).getItem());
        }
        if(p.hasPermission("shopsystem.shop.changeArmor"))
        {
            if(e instanceof Zombie)
            {
                inv.setItem(6, ItemType.CHANGEARMORMENU.getItem());
            }
            if(e instanceof Skeleton)
            {
                inv.setItem(6, ItemType.CHANGEARMORMENU.getItem());
            }
        }
        if(p.hasPermission("shopsystem.shop.changeEntity"))
        {
            inv.setItem(7, ItemType.CHANGEENTITY.getItem());
        }
        inv.setItem(8, ItemType.LEAVE.getItem());
        p.openInventory(inv);
    }

    public static void setGetItMenu(Player p)
    {
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

    public static void changeProfession(Player p)
    {
        for (Entity entity : ShopSystem.playerInShop.get(p.getUniqueId()).getChunk().getEntities()) {
            Location loc = entity.getLocation();
            Location vLoc = ShopSystem.playerInShop.get(p.getUniqueId());
            if (vLoc.getBlockX() == loc.getBlockX() && vLoc.getBlockY() == loc.getBlockY() && vLoc.getBlockZ() == loc.getBlockZ() && vLoc.getWorld() == loc.getWorld()) {
                if (entity instanceof Villager) {
                    Villager v = (Villager) entity;
                    VillagerClass.switchProfession(v ,p);
                    Inventory inv = Bukkit.createInventory(null, 9, Locale.SHOP_MENU);
                    inv.setItem(0, ItemType.EDITSHOP.getItem());
                    inv.setItem(1, ItemType.DELETESHOP.getItem());
                    if(p.hasPermission("shopsystem.shop.changeName"))
                        inv.setItem(3, ItemType.CHANGENAME.getItem());
                    inv.setItem(4, ItemType.SHOWSHOP.getItem());
                    inv.setItem(6, new ItemCreation(Locale.ITEM_CHANGE_PROFESSION, Material.WOOL, null, 1, VillagerClass.getProfession(p), true, null).getItem());
                    if(p.hasPermission("shopsystem.shop.changeEntity"))
                    {
                        inv.setItem(7, ItemType.CHANGEENTITY.getItem());
                    }
                    inv.setItem(8, ItemType.LEAVE.getItem());
                    p.closeInventory();
                    p.openInventory(inv);
                }
            }
        }
    }

    public static void openArmorMenu(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 9, Locale.SHOP_CHANGE_ARMOR);
        inv.setItem(0, ItemType.ADDARMOR.getItem());
        inv.setItem(2, ItemType.REMOVEHELMET.getItem());
        inv.setItem(3, ItemType.REMOVECHESTPLATE.getItem());
        inv.setItem(4, ItemType.REMOVELEGGINGS.getItem());
        inv.setItem(5, ItemType.REMOVEBOOTS.getItem());
        inv.setItem(6, ItemType.REMOVEALLARMOR.getItem());
        inv.setItem(8, ItemType.BACK.getItem());
        p.openInventory(inv);
    }


}
