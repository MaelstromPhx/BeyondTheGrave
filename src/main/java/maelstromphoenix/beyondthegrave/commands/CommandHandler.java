package maelstromphoenix.beyondthegrave.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandHandler extends CommandBase{

	static Map<String, SubCommand> commands = new HashMap<String, SubCommand>();
	public String error = "Invalid Command! See /beyondthegrave help";
	
	public CommandHandler(){
		commands.put("help", new CommandHelp());
		commands.put("check", new CommandCheck());
		commands.put("restore", new CommandRestore());
	}
	
	@Override
	public String getCommandName() {
		return "beyondthegrave";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/beyondthegrave help";
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if(args.length == 1 && !commands.containsKey(args[0].toLowerCase())){
			List<String> commandList = new ArrayList<String>(commands.keySet());
			
			for(int i = 0; i < commandList.size(); i++){
				if(!commandList.get(i).contains(args[0].toLowerCase())){
					commandList.remove(i);
					i--;
				}
			}
			return commandList;
		}
		
		if(!commands.containsKey(args[0].toLowerCase()))
			return new ArrayList<String>();
		
		return commands.get(args[0].toLowerCase()).getTabCompletionOptions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args[0].isEmpty()){
			sender.addChatMessage(new TextComponentString(error));
			return;
		}else if(commands.get(args[0].toLowerCase()) != null){
			commands.get(args[0].toLowerCase()).execute(server, sender, args);
		}else{
			sender.addChatMessage(new TextComponentString(error));
		}
	}

	public abstract static class SubCommand{
		public abstract String getName();
		public abstract String getUsage();
		public abstract String getDescription();
		public abstract List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);
		public abstract void execute(MinecraftServer server, ICommandSender sender, String[] args);
	}
	
}
