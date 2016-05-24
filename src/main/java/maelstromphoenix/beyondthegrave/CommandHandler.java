package maelstromphoenix.beyondthegrave;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandHandler extends CommandBase{

//	private List<String> aliases = new ArrayList<String>();
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args[0].isEmpty()){
			sender.addChatMessage(new TextComponentString(error));
			return;
		}else if(commands.get(args[0].toLowerCase()) != null){
			commands.get(args[0]).execute(server, sender, args);
		}else{
			sender.addChatMessage(new TextComponentString(error));
		}
	}

	public abstract static class SubCommand{
		public abstract String getName();
		public abstract String getUsage();
		public abstract String getDescription();
		public abstract void execute(MinecraftServer server, ICommandSender sender, String[] args);
	}
	
}
