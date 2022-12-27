package wtf.godlydev.console.command.commands;

import java.io.IOException;
import java.util.Map.Entry;

import wtf.godlydev.console.command.CommandRegistry;
import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.console.Console;
import wtf.godlydev.console.global.GlobalVariables;
import wtf.godlydev.console.plugin.api.Plugin;
import wtf.godlydev.console.utils.EmptyProcess;
import wtf.godlydev.console.utils.ProcessUtil;

public class HelpCommand implements ICommand {

	@Override
	public String name() {
		return "help";
	}

	@Override
	public String shortHelp() {
		return "HELP           Provides help information for Windows commands.";
	}

	@Override
	public String help() {
		return "Provides help information for Windows commands.\r\n"
				+ "\r\n"
				+ "HELP [command]\r\n"
				+ "\r\n"
				+ "    command - displays help information on that command.";
	}

	@Override
	public Process execute(String[] cmd) throws IOException {
		if(cmd.length == 1) {
			ProcessBuilder pb = ProcessUtil.newEmptyProcess();
			pb.command("help");
			Process p = pb.start();
			try {
				p.waitFor();
			} catch (InterruptedException e) { }
			System.out.println("----------\n" + GlobalVariables.APPLICATION_NAME + " Commands");
			for(Entry<ICommand, Plugin> command : CommandRegistry.getCommandRegistry().entrySet()) {
				if(command.getValue() != null) {
					System.out.print(command.getKey().shortHelp());
					Console.getInstance().getCliTools().setColorRGB(0, 255, 0);
					System.out.print(" >> ");
					Console.getInstance().getCliTools().setColorRGB(255, 255, 0);
					System.out.print(command.getValue().getDescriptionFile().getName() + " " + command.getValue().getDescriptionFile().getVersion());
					Console.getInstance().getCliTools().reset();
					System.out.println();
				}
				else
					System.out.println(command.getKey().shortHelp());
			}
			return p;
		} else {
			String req = cmd[1];
			if(CommandRegistry.commandExists(req)) {
				System.out.println(CommandRegistry.getCommandByName(req).help());
				return new EmptyProcess();
			} else {
				ProcessBuilder pb = ProcessUtil.newEmptyProcess();
				pb.command("help", req);
				return pb.start();
			}
		}
	}

}
