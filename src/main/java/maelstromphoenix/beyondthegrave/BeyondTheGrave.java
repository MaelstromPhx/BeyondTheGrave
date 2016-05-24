package maelstromphoenix.beyondthegrave;

import maelstromphoenix.beyondthegrave.EventHandler;
import maelstromphoenix.beyondthegrave.InventoryCapability;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = BeyondTheGrave.MODID, name = BeyondTheGrave.MODNAME, version = BeyondTheGrave.MODVERSION)
public class BeyondTheGrave {

	public static final String MODID = "beyondthegrave";
	public static final String MODNAME = "Beyond the Grave";
	public static final String MODVERSION = "1.1";
	
	@Mod.Instance
	public static BeyondTheGrave INSTANCE;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Config.preInit(event);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new InventoryCapability());
		MinecraftForge.EVENT_BUS.register(new InventoryBackupCapability());
		InventoryCapability.register();
		InventoryBackupCapability.register();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
	
	}
	
	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandHandler());
	}
	
}
