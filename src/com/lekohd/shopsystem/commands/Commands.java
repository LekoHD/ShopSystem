package com.lekohd.shopsystem.commands;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.manager.MessageManager;
import com.lekohd.shopsystem.entity.VillagerClass;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */

public class Commands implements CommandExecutor { //Command: /shop...

    public void sendHelp(Player p)
    {
        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "Commands:");
        p.sendMessage("/shop place");
        p.sendMessage("/shop remove");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player)
        {
            Player sender = (Player) commandSender;
            if (!sender.hasPermission("shopsystem.admin"))
            {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, Locale.NO_PERMISSIONS.replace("%PERM%", "shopsystem.admin"));
                return true;
            }
            if(args.length == 0)
            {
                this.sendHelp(sender);
                return true;
            }
            else if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("remove")) //Remove by location
                {
                    Location l = sender.getLocation();
                    Location loc = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
                    if(ShopSystem.dataManager.shopInventory.containsKey(loc))
                    {
                        ShopSystem.dataManager.shopInventory.remove(loc);
                        ShopSystem.dataManager.removeShop(loc);
                        if(ShopSystem.dataManager.shopOwner.containsKey(loc))
                        {
                            int shopAmount = ShopSystem.dataManager.shopAmount.get(ShopSystem.dataManager.shopOwner.get(loc));
                            ShopSystem.dataManager.shopAmount.put(ShopSystem.dataManager.shopOwner.get(loc), shopAmount - 1);
                            ShopSystem.dataManager.shopOwner.remove(loc);
                        }
                        for(Entity entity : loc.getChunk().getEntities())
                        {
                            Location lo = entity.getLocation();
                            Location location = new Location (lo.getWorld(), lo.getBlockX(), lo.getBlockY(), lo.getBlockZ());
                            if(location.getBlockX() == loc.getBlockX() && location.getBlockY() == loc.getBlockY() && location.getBlockZ() == loc.getBlockZ() && location.getWorld() == loc.getWorld())
                            {
                                if(entity instanceof Villager)
                                    entity.remove();
                            }
                        }
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("place"));
                {
                    //Place Villager
                    VillagerClass.place(sender.getLocation());
                    return true;
                }
            }
            else if(args.length == 2)
            {

            }
        } else
        {
            //ERROR: You have to be a player to execute this command
            MessageManager.getInstance().log(MessageManager.MessageType.ERROR + "You have to be a player to execute this command.");
        }

        return false;
    }
}
