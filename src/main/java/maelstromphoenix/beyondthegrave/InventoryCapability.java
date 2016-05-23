package maelstromphoenix.beyondthegrave;

import java.util.List;
import java.util.concurrent.Callable;

import maelstromphoenix.beyondthegrave.BeyondTheGrave;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
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

public class InventoryCapability {

	private static final ResourceLocation location = new ResourceLocation(BeyondTheGrave.MODID, "Inventory");

	@CapabilityInject(Inventory.class)
    public static Capability<Inventory> INSTANCE;
	
    public static void register(){
        CapabilityManager.INSTANCE.register(Inventory.class, new Capability.IStorage<Inventory>()
        {

			@Override
			public NBTBase writeNBT(Capability<Inventory> capability, Inventory instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<Inventory> capability, Inventory instance, EnumFacing side, NBTBase nbt) {
				
			}
        }, new Callable<Inventory>(){

			@Override
			public Inventory call() throws Exception {
				return null;
			}
        	
        });
        
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent.Entity entity){
    	
        if (!(entity.getEntity() instanceof EntityZombie)){
            return;
    	}

        final EntityZombie zombie = (EntityZombie)entity.getEntity();
        
        entity.addCapability(location, new ICapabilitySerializable<NBTTagCompound>(){
        	
            Inventory inventory = new Inventory();

            @Override
			public NBTTagCompound serializeNBT() {
            	if(inventory.items.isEmpty())
            		return new NBTTagCompound();
            	NBTTagCompound tag = new NBTTagCompound();
            	NBTTagList items = new NBTTagList();
            	
            	for(int i = 0; i < inventory.items.size(); i++){
            		items.appendTag(inventory.items.get(i).serializeNBT());
            	}
            	tag.setTag("Inventory", items);
            	return tag;
			}

			@Override
			public void deserializeNBT(NBTTagCompound nbt) {
				NBTTagList items = nbt.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
				if(items.hasNoTags())
					return;
				for(int i = 0; i < items.tagCount(); i++){
					NBTTagCompound itemTag = items.getCompoundTagAt(i);
					EntityItem item = new EntityItem(zombie.worldObj);
					item.deserializeNBT(itemTag);
					inventory.items.add(item);
				}
			}
            
            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing){
                return capability == INSTANCE;
            }

            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing){
            	if(capability == INSTANCE)
            		return INSTANCE.cast(inventory);
                return null;
            }
            
        });
    }
	
}
