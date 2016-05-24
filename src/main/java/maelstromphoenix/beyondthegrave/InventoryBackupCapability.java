package maelstromphoenix.beyondthegrave;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import maelstromphoenix.beyondthegrave.BeyondTheGrave;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventoryBackupCapability {

	private static final ResourceLocation location = new ResourceLocation(BeyondTheGrave.MODID, "InventoryBackup");

	@CapabilityInject(InventoryBackup.class)
    public static Capability<InventoryBackup> INSTANCE;
	
    public static void register(){
        CapabilityManager.INSTANCE.register(InventoryBackup.class, new Capability.IStorage<InventoryBackup>()
        {

			@Override
			public NBTBase writeNBT(Capability<InventoryBackup> capability, InventoryBackup instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<InventoryBackup> capability, InventoryBackup instance, EnumFacing side, NBTBase nbt) {
				
			}
        }, new Callable<InventoryBackup>(){

			@Override
			public InventoryBackup call() throws Exception {
				return null;
			}
        	
        });
        
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent.Entity entity){
    	
        if (!(entity.getEntity() instanceof EntityPlayer))
            return;
        
        final EntityPlayer player = (EntityPlayer) entity.getEntity();
        
        entity.addCapability(location, new ICapabilitySerializable<NBTTagCompound>(){
        	
            InventoryBackup inventoryBackup = new InventoryBackup();
            
            @Override
			public NBTTagCompound serializeNBT() {
            	List<Inventory> backupArray = inventoryBackup.getInventoryBackup();
            	
            	if(backupArray.isEmpty())
            		return new NBTTagCompound();
            	
            	NBTTagCompound tag = new NBTTagCompound();
            	NBTTagList backups = new NBTTagList();
            	
            	for(int i = 0; i < backupArray.size(); i++){
            		NBTTagList itemsList = new NBTTagList();
            		List<EntityItem> items = backupArray.get(i).items;
            		
            		for(int j = 0; j < items.size(); j++){
            			itemsList.appendTag(items.get(j).serializeNBT());
            		}
            		backups.appendTag(itemsList);
            	}
            	tag.setTag("InventoryBackup", backups);
            	return tag;
			}

			@Override
			public void deserializeNBT(NBTTagCompound nbt) {
				NBTTagList backups = nbt.getTagList("InventoryBackup", Constants.NBT.TAG_LIST);
				if(backups.hasNoTags())
					return;
				for(int i = 0; i < backups.tagCount(); i++){
					NBTTagList itemsList = (NBTTagList) backups.get(i);
					List<EntityItem> items = new ArrayList<EntityItem>();
					
					for(int j = itemsList.tagCount() - 1; j >= 0 ; j--){
						NBTTagCompound itemTag = itemsList.getCompoundTagAt(j);
						EntityItem item = new EntityItem(player.worldObj);
						item.deserializeNBT(itemTag);
						items.add(item);
					}
					inventoryBackup.addInventoryBackup(items);
				}
				
			}
            
            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing){
                return capability == INSTANCE;
            }

            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing){
            	if(capability == INSTANCE)
            		return INSTANCE.cast(inventoryBackup);
                return null;
            }
            
        });
    }
	
}
