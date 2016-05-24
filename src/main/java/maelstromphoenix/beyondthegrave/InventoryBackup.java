package maelstromphoenix.beyondthegrave;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;

public class InventoryBackup {

	private List<Inventory> inventoryBackup = new ArrayList<Inventory>();
	public static int MAXINVENTORYBACKUPS = Config.getInteger("MAXINVENTORYBACKUPS");
	
	public InventoryBackup(){
		
	}
	
	public List<Inventory> getInventoryBackup(){
		return inventoryBackup;
	}
	
	public void addInventoryBackup(List<EntityItem> backup){
		Inventory inventory = new Inventory();
		inventory.items = backup;
		inventoryBackup.add(0, inventory);
		if(inventoryBackup.size() > MAXINVENTORYBACKUPS){
			inventoryBackup.subList(MAXINVENTORYBACKUPS, inventoryBackup.size()).clear();;
		}
	}
	
	public void setInventoryBackup(List<Inventory> backup){
		this.inventoryBackup = backup;
	}
	
}
