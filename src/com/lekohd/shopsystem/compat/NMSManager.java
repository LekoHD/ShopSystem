package com.lekohd.shopsystem.compat;

import org.bukkit.plugin.Plugin;

/**
 * Created by Leon on 31.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class NMSManager {

    private static NMSCallProvider provider;

    public static NMSCallProvider getProvider()
    {
        return provider;
    }

    public static void load(Plugin plugin) {
        String packageName = plugin.getServer().getClass().getPackage().getName();
        String cbversion = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            Class clazz = Class.forName("com.lekohd.shopsystem.compat." + cbversion + ".NMSHandler");
            if (NMSCallProvider.class.isAssignableFrom(clazz))
                provider = (NMSCallProvider)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
            else
                throw new Exception("Nope");
        }
        catch (Exception e) {
            plugin.getLogger().severe("Potentially incompatible server version: " + cbversion);
            plugin.getLogger().severe("It is trying to run in 'compatibility mode'.");
        }
    }

}
