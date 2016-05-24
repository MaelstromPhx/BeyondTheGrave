package maelstromphoenix.beyondthegrave;

import java.util.Map;

import maelstromphoenix.beyondthegrave.CommandHandler.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandHelp extends SubCommand{

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Displays help menu for commands.";
	}
	
	@Override
	public String getUsage() {
		return "/beyondthegrave help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		Map<String, SubCommand> commands = CommandHandler.commands;
		
		if(args.length >= 2 && commands.containsKey(args[1])){
			sender.addChatMessage(new TextComponentString(commands.get(args[1]).getDescription()));
		}else{
			sender.addChatMessage(new TextComponentString("For a description of the command use /beyondthegrave help <command>."));
			for(Map.Entry<String, SubCommand> entry: commands.entrySet()){
				if(entry.getKey() != "help"){
					sender.addChatMessage(new TextComponentString(entry.getValue().getUsage()));
				}
					
			}
		}
		
	}

	

}
