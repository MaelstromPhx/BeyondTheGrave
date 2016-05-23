package maelstromphoenix.beyondthegrave;

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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingDrops(LivingDropsEvent event){
		if(event.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntity();
			
			EntityZombie zombie = new EntityZombie(player.worldObj);
			EntityLivingBase entityLiving = (EntityLivingBase)zombie;
			
			if(!zombie.hasCapability(InventoryCapability.INSTANCE, null))
				return;
			Inventory inventory = zombie.getCapability(InventoryCapability.INSTANCE, null);

			inventory.items = event.getDrops();
			
			boolean head, chest, legs, feet, mainhand, offhand;
			head = chest = legs = feet = mainhand = offhand = false;
			for(int i = 0; i < inventory.items.size(); i++){
				ItemStack stack = inventory.items.get(i).getEntityItem();
				if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.HEAD, zombie) && !head){
					zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, inventory.items.get(i).getEntityItem());
					head = true;
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.CHEST, zombie) && !chest){
					zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST, inventory.items.get(i).getEntityItem());
					chest = true;
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.LEGS, zombie) && !legs){
					zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS, inventory.items.get(i).getEntityItem());
					legs = true;
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.FEET, zombie) && !feet){
					zombie.setItemStackToSlot(EntityEquipmentSlot.FEET, inventory.items.get(i).getEntityItem());
					feet = true;
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.MAINHAND, zombie) && !mainhand){
					zombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, inventory.items.get(i).getEntityItem());
					mainhand = true;
				}else if(stack.getItem().isValidArmor(stack, EntityEquipmentSlot.OFFHAND, zombie) && !offhand){
					zombie.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, inventory.items.get(i).getEntityItem());
					offhand = true;
				}
			}
			
			zombie.setDropChance(EntityEquipmentSlot.HEAD, 0);
			zombie.setDropChance(EntityEquipmentSlot.CHEST, 0);
			zombie.setDropChance(EntityEquipmentSlot.LEGS, 0);
			zombie.setDropChance(EntityEquipmentSlot.FEET, 0);
			zombie.setDropChance(EntityEquipmentSlot.MAINHAND, 0);
			zombie.setDropChance(EntityEquipmentSlot.OFFHAND, 0);

			zombie.setCustomNameTag(player.getName() + "'s Corpse");
			zombie.replaceItemInInventory(1, inventory.items.get(0).getEntityItem());
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
		zombie.setHealth(1);
	}
	
}
