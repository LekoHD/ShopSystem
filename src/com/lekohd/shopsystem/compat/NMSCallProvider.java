package com.lekohd.shopsystem.compat;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

/**
 * Created by Leon on 31.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public abstract interface NMSCallProvider {

    public abstract void noAI(Entity bukkitEntity);

    public abstract void overwriteLivingEntityAI(LivingEntity paramLivingEntity);

    public abstract void overwriteVillagerAI(LivingEntity paramLivingEntity);

    public abstract int getMaxVillagerProfession();

    public abstract void setVillagerProfession(Villager paramVillager, int paramInt);

    public abstract void setEntitySilent(Entity paramEntity, boolean paramBoolean);
}
