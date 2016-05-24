package maelstromphoenix.beyondthegrave;

import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {

	static Configuration configuration;
	
	public static HashMap<String, Boolean> configBoolean = new HashMap<String, Boolean>();
	public static HashMap<String, Integer> configInteger = new HashMap<String, Integer>();
	public static HashMap<String, Double> configDouble = new HashMap<String, Double>();
	public static HashMap<String, String> configString = new HashMap<String, String>();
	
	public static void preInit(FMLPreInitializationEvent event){
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		configuration.load();
		
		setInteger("MAXINVENTORYBACKUPS", configuration.getInt("MaxInventoryBackups", Configuration.CATEGORY_GENERAL, 5, 0, 999, "Sets the amount of inventory backups that each player will have."));
		
		configuration.save();
	}
	
	public static Boolean getBoolean(String string){
		return configBoolean.get(string);
	}
	
	public static Integer getInteger(String string){
		return configInteger.get(string);
	}
	
	public static Double getDouble(String string){
		return configDouble.get(string);
	}
	
	public static String getString(String string){
		return configString.get(string);
	}
	
	public static Boolean setBoolean(String string, Boolean value){
		return configBoolean.put(string, value);
	}
	
	public static Integer setInteger(String string, Integer value){
		return configInteger.put(string, value);
	}
	
	public static Double setDouble(String string, Double value){
		return configDouble.put(string, value);
	}
	
	public static String setString(String string, String value){
		return configString.put(string, value);
	}
	
}
