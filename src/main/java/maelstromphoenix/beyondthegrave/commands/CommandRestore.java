package maelstromphoenix.beyondthegrave.commands;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import maelstromphoenix.beyondthegrave.InventoryBackupCapability;
import maelstromphoenix.beyondthegrave.InventoryCapability.Inventory;
import maelstromphoenix.beyondthegrave.commands.CommandHandler.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandRestore extends SubCommand{

	@Override
	public String getName() {
		return "restore";
	}

	@Override
	public String getDescription() {
		return "Restores the <player>'s inventory backup at given <index> to <targetplayer>'s inventory. 0 is the most recent.";
	}
	
	@Override
	public String getUsage() {
		return "/beyondthegrave restore <player> <index> <targetplayer>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if(args.length < 4){
			sender.addChatMessage(new TextComponentString("Incorrect number of paramaters!"));
		}else if(!(server.getPlayerList().getPlayerByUsername(args[1]) instanceof EntityPlayerMP)){
			sender.addChatMessage(new TextComponentString("Invalid player!"));
			return;
		}else if(!NumberUtils.isNumber(args[2])){
			sender.addChatMessage(new TextComponentString("Given index is not a number!"));
			return;
		}else if(!(server.getPlayerList().getPlayerByUsername(args[3]) instanceof EntityPlayerMP)){
			sender.addChatMessage(new TextComponentString("Invalid target player!"));
			return;
		}
		
		EntityPlayer player = (EntityPlayer)server.getPlayerList().getPlayerByUsername(args[1]);
		EntityPlayer targetPlayer = (EntityPlayer)server.getPlayerList().getPlayerByUsername(args[3]);
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
			ItemStack copy = stack.copy();
			if(!targetPlayer.inventory.addItemStackToInventory(stack.copy())){
				targetPlayer.dropItem(copy.getItem(), copy.stackSize);
			}
		}
//		sender.addChatMessage(new TextComponentString(sender.get));
	}

}
