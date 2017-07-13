package com.lekohd.shopsystem.compat.v1_8_R1;

import com.lekohd.shopsystem.compat.NMSCallProvider;
import net.minecraft.server.v1_8_R1.*;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Leon on 31.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class NMSHandler implements NMSCallProvider{

    public String getVersionId()
    {
        return "1_8_R1";
    }

    public void noAI(Entity bukkitEntity) {
        net.minecraft.server.v1_8_R1.Entity nmsEntity = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity) bukkitEntity).getHandle();
        net.minecraft.server.v1_8_R1.NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new net.minecraft.server.v1_8_R1.NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

    public void overwriteLivingEntityAI(LivingEntity entity)
    {
        try
        {
            EntityLiving ev = ((CraftLivingEntity)entity).getHandle();

            Field goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalsField.setAccessible(true);
            PathfinderGoalSelector goals = (PathfinderGoalSelector)goalsField.get(ev);

            Field listField = PathfinderGoalSelector.class.getDeclaredField("b");
            listField.setAccessible(true);
            List list = (List)listField.get(goals);
            list.clear();
            listField = PathfinderGoalSelector.class.getDeclaredField("c");
            listField.setAccessible(true);
            list = (List)listField.get(goals);
            list.clear();

            goals.a(0, new PathfinderGoalFloat((EntityInsentient)ev));
            goals.a(1, new PathfinderGoalLookAtPlayer((EntityInsentient)ev, EntityHuman.class, 12.0F, 1.0F));

            Field targetsField = EntityInsentient.class.getDeclaredField("targetSelector");
            targetsField.setAccessible(true);
            PathfinderGoalSelector targets = (PathfinderGoalSelector)targetsField.get(ev);

            Field listField2 = PathfinderGoalSelector.class.getDeclaredField("b");
            listField2.setAccessible(true);
            List list2 = (List)listField.get(goals);
            list2.clear();
            listField2 = PathfinderGoalSelector.class.getDeclaredField("c");
            listField2.setAccessible(true);
            list2 = (List)listField.get(goals);
            list2.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void overwriteVillagerAI(LivingEntity villager)
    {
        try {
            EntityVillager ev = ((CraftVillager)villager).getHandle();

            Field goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalsField.setAccessible(true);
            PathfinderGoalSelector goals = (PathfinderGoalSelector)goalsField.get(ev);

            Field listField = PathfinderGoalSelector.class.getDeclaredField("b");
            listField.setAccessible(true);
            List list = (List)listField.get(goals);
            list.clear();
            listField = PathfinderGoalSelector.class.getDeclaredField("c");
            listField.setAccessible(true);
            list = (List)listField.get(goals);
            list.clear();

            goals.a(0, new PathfinderGoalFloat(ev));
            goals.a(1, new PathfinderGoalTradeWithPlayer(ev));
            goals.a(1, new PathfinderGoalLookAtTradingPlayer(ev));
            goals.a(2, new PathfinderGoalLookAtPlayer(ev, EntityHuman.class, 12.0F, 1.0F));

            Field targetsField = EntityInsentient.class.getDeclaredField("targetSelector");
            targetsField.setAccessible(true);
            PathfinderGoalSelector targets = (PathfinderGoalSelector)targetsField.get(ev);

            Field listField2 = PathfinderGoalSelector.class.getDeclaredField("b");
            listField2.setAccessible(true);
            List list2 = (List)listField.get(goals);
            list2.clear();
            listField2 = PathfinderGoalSelector.class.getDeclaredField("c");
            listField2.setAccessible(true);
            list2 = (List)listField.get(goals);
            list2.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxVillagerProfession()
    {
        return 4;
    }

    public void setVillagerProfession(Villager villager, int profession)
    {
        ((CraftVillager)villager).getHandle().setProfession(profession);
    }

    public void setEntitySilent(Entity entity, boolean silent)
    {
        net.minecraft.server.v1_8_R1.Entity mcEntity = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity)entity).getHandle();
        mcEntity.b(silent);
    }

}