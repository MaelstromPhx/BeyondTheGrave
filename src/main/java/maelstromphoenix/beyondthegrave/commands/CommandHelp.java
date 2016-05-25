package maelstromphoenix.beyondthegrave.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maelstromphoenix.beyondthegrave.commands.CommandHandler.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
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
		return "/beyondthegrave help or /beyondthegrave help <command>";
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if(args.length == 2){
			List<String> commandList = new ArrayList(CommandHandler.commands.keySet());
			
			for(int i = 0; i < commandList.size(); i++){
				if(!commandList.get(i).contains(args[1].toLowerCase())){
					commandList.remove(i);
					i--;
				}
			}
			return commandList;
		}
		
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		Map<String, SubCommand> commands = CommandHandler.commands;
		
		if(args.length >= 2 && commands.containsKey(args[1].toLowerCase())){
			sender.addChatMessage(new TextComponentString(commands.get(args[1].toLowerCase()).getDescription()));
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
