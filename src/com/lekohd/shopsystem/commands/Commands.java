package com.lekohd.shopsystem.commands;

import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.manager.MessageManager;
import com.lekohd.shopsystem.villager.VillagerClass;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by thorstenkoth on 26.05.15.
 */
public class Commands implements CommandExecutor { //Command: /shopsystem ...

    public void sendHelp(Player p)
    {
        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "Commands:");
        p.sendMessage("/shopsystem place");
        p.sendMessage("/shopsystem remove");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player)
        {
            Player sender = (Player) commandSender;
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
                            ShopSystem.dataManager.shopOwner.remove(loc);
                        }
                        for(Entity entity : loc.getChunk().getEntities())
                        {
                            Location lo = entity.getLocation();
                            Location location = new Location (lo.getWorld(), lo.getBlockX(), lo.getBlockY(), lo.getBlockZ());
                            if(location == loc)
                            {
                                entity.remove();
                            }
                        }
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("place"));
                {
                    //Place Villager
                    VillagerClass v = new VillagerClass(null, sender.getLocation());
                    v.place();
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
