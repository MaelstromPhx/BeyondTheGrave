package maelstromphoenix.beyondthegrave;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import maelstromphoenix.beyondthegrave.CommandHandler.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandCheck extends SubCommand {

	@Override
	public String getName() {
		return "check";
	}

	@Override
	public String getDescription() {
		return "Displays the <player>'s inventory backup at the given <index>. 0 is the most recent. Holds up to " + InventoryBackup.MAXINVENTORYBACKUPS + " backups.";
	}
	
	@Override
	public String getUsage() {
		return "/beyondthegrave check <player> <index>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if(args.length < 3){
			sender.addChatMessage(new TextComponentString("Incorrect number of paramaters!"));
		}else if(!(server.getPlayerList().getPlayerByUsername(args[1]) instanceof EntityPlayerMP)){
			sender.addChatMessage(new TextComponentString("Invalid player!"));
			return;
		}else if(!NumberUtils.isNumber(args[2])){
			sender.addChatMessage(new TextComponentString("Given index is not a number!"));
			return;
		}
		
		EntityPlayer player = (EntityPlayer)server.getPlayerList().getPlayerByUsername(args[1]);
		if(!player.hasCapability(InventoryBackupCapability.INSTANCE, null)){
			sender.addChatMessage(new TextComponentString("No inventory backups found!"));
			return;
		}
		
		List<Inventory> backup = player.getCapability(InventoryBackupCapability.INSTANCE, null).getInventoryBackup();
		int index = Integer.parseInt(args[2]);
		
		if(index > backup.size() - 1){
			sender.addChatMessage(new TextComponentString("No backup at that index!"));
			return;
		}
		List<EntityItem> items = backup.get(index).items;
		
		for(int i = 0; i < items.size(); i++){
			ItemStack stack = items.get(i).getEntityItem();
			sender.addChatMessage(new TextComponentString(stack.stackSize + "x " + stack.getDisplayName()));
		}
		
	}

}
