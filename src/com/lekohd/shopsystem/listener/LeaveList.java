package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Leon on 01.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class LeaveList implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        ShopSystem.dataManager.saveShopAmountByUUID(p.getUniqueId(), ShopSystem.dataManager.shopAmount.get(p.getUniqueId()), true);
    }

}
