package me.Destro168.FC_Rpg.Entities;

import net.minecraft.server.EntityLiving;

import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;

public class MobEquipment
{
	private static org.bukkit.inventory.ItemStack toCraftBukkit(org.bukkit.inventory.ItemStack stack)
	{
		if (!(stack instanceof CraftItemStack))
			return new CraftItemStack(stack);
		else
			return stack;
	}
	
	public static void setWeapon(LivingEntity mob, org.bukkit.inventory.ItemStack item)
	{
		if (item == null)
			return;
		
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) toCraftBukkit(item)).getHandle();
		ent.setEquipment(0, itemStack);
	}
	
	public static void setHelmet(LivingEntity mob, org.bukkit.inventory.ItemStack item)
	{
		if (item == null)
			return;
		
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) toCraftBukkit(item)).getHandle();
		ent.setEquipment(4, itemStack);
	}

	public static void setChestplate(LivingEntity mob, org.bukkit.inventory.ItemStack item)
	{
		if (item == null)
			return;
		
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) toCraftBukkit(item)).getHandle();
		ent.setEquipment(3, itemStack);
	}

	public static void setPants(LivingEntity mob, org.bukkit.inventory.ItemStack item)
	{
		if (item == null)
			return;
		
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) toCraftBukkit(item)).getHandle();
		ent.setEquipment(2, itemStack);
	}

	public static void setBoots(LivingEntity mob, org.bukkit.inventory.ItemStack item)
	{
		if (item == null)
			return;
		
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) toCraftBukkit(item)).getHandle();
		ent.setEquipment(1, itemStack);
	}

	public static org.bukkit.inventory.ItemStack getWeapon(LivingEntity mob)
	{
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		return new CraftItemStack(ent.getEquipment(0));
	}

	public static org.bukkit.inventory.ItemStack getHelmet(LivingEntity mob)
	{
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		return new CraftItemStack(ent.getEquipment(4));
	}

	public static org.bukkit.inventory.ItemStack getChestplate(LivingEntity mob)
	{
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		return new CraftItemStack(ent.getEquipment(3));
	}

	public static org.bukkit.inventory.ItemStack getPants(LivingEntity mob)
	{
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		return new CraftItemStack(ent.getEquipment(2));
	}

	public static org.bukkit.inventory.ItemStack getBoots(LivingEntity mob)
	{
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		return new CraftItemStack(ent.getEquipment(1));
	}
}
