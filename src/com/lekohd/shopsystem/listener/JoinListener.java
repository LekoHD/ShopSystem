package com.lekohd.shopsystem.listener;

import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.manager.SettingsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Leon on 01.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(!ShopSystem.dataManager.shopAmount.containsKey(p.getUniqueId()))
        {
            if(ShopSystem.dataManager.userIsInConfig(p.getUniqueId()))
            {
                ShopSystem.dataManager.shopAmount.put(p.getUniqueId(), SettingsManager.getInstance().getData().getInt("owner." + p.getUniqueId().toString()));
            }
            else
            {
                ShopSystem.dataManager.saveShopAmountByUUID(p.getUniqueId(), 0, true);
                ShopSystem.dataManager.shopAmount.put(p.getUniqueId(), 0);
            }
        }
    }

}
