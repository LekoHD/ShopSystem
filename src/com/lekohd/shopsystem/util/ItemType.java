package com.lekohd.shopsystem.util;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.util.ArrayList;

import com.lekohd.shopsystem.Locale;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ItemType
{
    CREATESHOP(Locale.ITEM_CREATE, Material.ANVIL, null),
    CHANGENAME(Locale.ITEM_CHANGE_SHOP_NAME, Material.PAPER, null),
    SHOWSHOP(Locale.ITEM_SHOW_SHOP, Material.ENDER_PEARL, null),
    EDITSHOP(Locale.ITEM_EDIT_SHOP, Material.ANVIL, null),
    LEAVE(Locale.ITEM_LEAVE, Material.ARROW, Locale.ITEM_LEAVE_LORE),
    CHANGEPROFESSION(Locale.ITEM_CHANGE_PROFESSION, Material.WOOL, null),
    NULL_ITEM("", Material.THIN_GLASS, null),
    SAVESHOP(Locale.ITEM_SHOP_SAVE, Material.MAP, null);

    private String name;
    private Material type;
    private String lore;

    /**
     * Default Shop navigation items
     * @param name of the item
     * @param type of the item
     * @param lore of the item
     */
    private ItemType(String name, Material type, String lore) {
        this.name = name;
        this.type = type;
        this.lore = lore; }

    /**
     * To get the item
     * @return the item initialized
     */
    public ItemStack getItem()
    {
        ItemStack i;
        i = new ItemStack(this.type);
        ItemMeta m = i.getItemMeta();
        if (this.lore != null) {
            ArrayList l = new ArrayList();
            l.add(this.lore);
            m.setLore(l);
        }
        m.setDisplayName(this.name);
        i.setItemMeta(m);
        return i;
    }

    /**
     * To get the Material
     * @return the item Material
     */
    public Material getType() {
        return this.type;
    }

    /**
     * To get the item name.
     * Important for identification which shop navigation item was clicked
     * @return the item name
     */
    public String getName() {
        return this.name;
    }
}