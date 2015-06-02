package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Leon on 02.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class DropItemListener implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e)
    {
        Player p = e.getPlayer();
        if(ShopSystem.isEditingShop.containsKey(p.getUniqueId()))
        {
            if(ShopSystem.isEditingShop.get(p.getUniqueId()))
            {
                ShopSystem.isEditingShop.put(p.getUniqueId(), false);
                e.getItemDrop().remove();
            }
        }
    }

}
