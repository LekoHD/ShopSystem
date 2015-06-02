package com.lekohd.shopsystem.handler;

import com.lekohd.economysystem.EconomySystem;
import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.exception.NotEnoughMoneyException;
import com.lekohd.shopsystem.manager.MessageManager;
import org.bukkit.entity.Player;

import java.sql.SQLException;

/**
 * Created by Leon on 02.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class EconomyHandler {

    public static boolean hasRequiredAmount(Player p, int amount)
    {
        try {
            int coins = EconomySystem.getCoins(p);
            if(coins < amount)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void transferMoney(Player buyer, Player seller, int amount) throws NotEnoughMoneyException
    {
        if(hasRequiredAmount(buyer, amount))
        {
            EconomySystem.removeCoins(buyer, amount);
            EconomySystem.addCoins(seller, amount);
            MessageManager.getInstance().msg(buyer, MessageManager.MessageType.INFO, Locale.ECONOMY_BUYING_SUCCESS.replace("%PRICE%", amount + ""));
            MessageManager.getInstance().msg(seller, MessageManager.MessageType.INFO, Locale.ECONOMY_SOLD_ITEM.replace("%PRICE%", amount + "").replace("%BUYER%", buyer.getName()));
        }
        else
        {
            MessageManager.getInstance().msg(buyer, MessageManager.MessageType.INFO, Locale.ECONOMY_DONT_HAVE_ENOUGH_MONEY);
            throw new NotEnoughMoneyException();
        }
    }

}