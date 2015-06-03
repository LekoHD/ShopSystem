package com.lekohd.shopsystem.item;

import com.lekohd.shopsystem.Locale;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ItemCreation {

    private String name;
    private Material type;
    private String lore;
    private int amount, professionID;
    private boolean admin;
    private MaterialData data;
    ItemStack item;

    public ItemCreation(String name, Material type, String lore, int amount, int professionID, boolean admin, MaterialData data) {
        this.name = name;
        this.type = type;
        this.lore = lore;
        this.amount = amount;
        this.professionID = professionID;
        this.admin = admin;
        this.data = data;
    }

    public ItemCreation(String name, ItemStack item, String lore, int amount, int professionID, boolean admin, MaterialData data) {
        this.name = name;
        this.type = item.getType();
        this.lore = lore;
        this.amount = amount;
        this.professionID = professionID;
        this.admin = admin;
        this.data = data;
        this.item = item;
    }

    public ItemStack getItem()
    {
        ItemStack i;
        if(item != null)
        {
            i = item;
        } else {
            if (professionID > -1) {
                if (professionID == 0) {
                    i = new ItemStack(this.type, this.amount, DyeColor.BROWN.getData());
                } else if (professionID == 1) {
                    i = new ItemStack(this.type, this.amount, DyeColor.WHITE.getData());
                } else if (professionID == 2) {
                    i = new ItemStack(this.type, this.amount, DyeColor.PINK.getData());
                } else
                    i = new ItemStack(this.type, this.amount, DyeColor.BROWN.getData());
            } else
                i = new ItemStack(this.type);
        }
        ItemMeta m = i.getItemMeta();
        if (this.lore != null) {
            ArrayList l = new ArrayList();
            l.add(" ");
            l.add(Locale.ITEM_PRICE.replace("%PRICE%", this.lore));
            l.add(Locale.ITEM_AMOUNT.replace("%AMOUNT%", this.amount + ""));
            l.add(" ");
            if(admin)
            {
                l.add(Locale.ITEM_EDIT_RIGHT_CLICK);
                l.add(Locale.ITEM_EDIT_LEFT_CLICK);
            }
            else {
                l.add(Locale.ITEM_BUY_RIGHT_CLICK);
                l.add(Locale.ITEM_BUY_LEFT_CLICK);
            }
            m.setLore(l);
        }
        if(this.name != null)
            m.setDisplayName(this.name);
        i.setItemMeta(m);
        if(this.data != null)
            i.setData(this.data);
        return i;
    }

    public Material getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

}
