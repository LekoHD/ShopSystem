package com.lekohd.shopsystem.item;

import com.lekohd.shopsystem.Locale;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public ItemCreation(String name, Material type, String lore, int amount, int professionID) {
        this.name = name;
        this.type = type;
        this.lore = lore;
        this.amount = amount;
        this.professionID = professionID;
    }

    public ItemStack getItem()
    {
        ItemStack i;
        if(professionID > -1)
        {
            if(professionID == 0)
            {
                i = new ItemStack(this.type, this.amount, DyeColor.BROWN.getData());
            }else
            if(professionID == 1)
            {
                i = new ItemStack(this.type, this.amount, DyeColor.WHITE.getData());
            }else
            if(professionID == 2)
            {
                i = new ItemStack(this.type, this.amount, DyeColor.PINK.getData());
            }else
                i = new ItemStack(this.type, this.amount, DyeColor.BROWN.getData());
        } else
            i = new ItemStack(this.type, this.amount);
        ItemMeta m = i.getItemMeta();
        if (this.lore != null) {
            ArrayList l = new ArrayList();
            l.add(Locale.ITEM_PRICE.replace("%PRICE%", this.lore));
            m.setLore(l);
        }
        m.setDisplayName(this.name);
        i.setItemMeta(m);
        return i;
    }

    public Material getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

}
