package maelstromphoenix.beyondthegrave;

import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingDrops(LivingDropsEvent event){
		if(event.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntity();
			
			if(player.hasCapability(InventoryBackupCapability.INSTANCE, null))
				player.getCapability(InventoryBackupCapability.INSTANCE, null).addInventoryBackup(event.getDrops());
				
			EntityZombie zombie = new EntityZombie(player.worldObj);
			EntityLivingBase entityLiving = (EntityLivingBase)zombie;
			
			if(!zombie.hasCapability(InventoryCapability.INSTANCE, null))
				return;
			Inventory inventory = zombie.getCapability(InventoryCapability.INSTANCE, null);

			inventory.items = event.getDrops();
			
			boolean mainhand = false;
			for(int i = 0; i < inventory.items.size(); i++){
				ItemStack stack = inventory.items.get(i).getEntityItem();
				if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.HEAD, zombie)){
					zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, inventory.items.get(i).getEntityItem());
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.CHEST, zombie)){
					zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST, inventory.items.get(i).getEntityItem());
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.LEGS, zombie)){
					zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS, inventory.items.get(i).getEntityItem());
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.FEET, zombie)){
					zombie.setItemStackToSlot(EntityEquipmentSlot.FEET, inventory.items.get(i).getEntityItem());
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.MAINHAND, zombie) && !mainhand){
					zombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, inventory.items.get(i).getEntityItem());
					mainhand = true;
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.OFFHAND, zombie)){
					zombie.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, inventory.items.get(i).getEntityItem());
				}
			}
			
			zombie.setDropChance(EntityEquipmentSlot.HEAD, 0);
			zombie.setDropChance(EntityEquipmentSlot.CHEST, 0);
			zombie.setDropChance(EntityEquipmentSlot.LEGS, 0);
			zombie.setDropChance(EntityEquipmentSlot.FEET, 0);
			zombie.setDropChance(EntityEquipmentSlot.MAINHAND, 0);
			zombie.setDropChance(EntityEquipmentSlot.OFFHAND, 0);
			
			zombie.setCustomNameTag(player.getName() + "'s Corpse");

			zombie.enablePersistence();
			zombie.setPositionAndRotation(player.posX, player.posY, player.posZ, player.cameraYaw, player.cameraPitch);
			player.worldObj.spawnEntityInWorld(zombie);
			
			event.setCanceled(true);
			
		}else if(event.getEntity() instanceof EntityZombie){
			EntityZombie zombie = (EntityZombie)event.getEntity();
			
			if(!(zombie.hasCapability(InventoryCapability.INSTANCE, null)))
					return;
			
			Inventory inventory = zombie.getCapability(InventoryCapability.INSTANCE, null);
			
			for(int i = 0; i < inventory.items.size(); i++){
				EntityItem item = inventory.items.get(i);
				item.setPosition(zombie.posX, zombie.posY, zombie.posZ);
				zombie.worldObj.spawnEntityInWorld(item);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingDeath(LivingDeathEvent event){
		Entity entity = event.getEntity();
		if(!(entity instanceof EntityZombie) || !entity.hasCapability(InventoryCapability.INSTANCE, null))
			return;
		Inventory inventory = entity.getCapability(InventoryCapability.INSTANCE, null);
		EntityZombie zombie = (EntityZombie)entity;
		if(inventory.items.size() == 0)
			return;
		if(event.getSource().getEntity() instanceof EntityPlayer)
			return;
		event.setCanceled(true);
		zombie.setHealth(0.5f);
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event){
		EntityPlayer player = event.getEntityPlayer();
		EntityPlayer original = event.getOriginal();
		if(!event.isWasDeath())
			return;

		if(!original.hasCapability(InventoryBackupCapability.INSTANCE, null) || !player.hasCapability(InventoryBackupCapability.INSTANCE, null))
			return;
		player.getCapability(InventoryBackupCapability.INSTANCE, null).setInventoryBackup(original.getCapability(InventoryBackupCapability.INSTANCE, null).getInventoryBackup());
	}
	
}
