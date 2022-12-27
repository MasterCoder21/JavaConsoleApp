package wtf.godlydev.console.command.commands;

import java.io.File;
import java.io.IOException;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.console.Console;

public class ExitCommand implements ICommand {

	@Override
	public String name() {
		return "exit";
	}

	@Override
	public String shortHelp() {
		return "EXIT           Quits the application.";
	}

	@Override
	public String help() {
		return "Quits the application.";
	}

	@Override
	public Process execute(String[] cmd) throws IOException {
		Console.getInstance().getConfig().save();
		for(File pluginDir : Console.getInstance().getConfig().pluginPaths) {
			if(!pluginDir.exists()) {
				pluginDir.mkdirs();
			}
			for(File f : pluginDir.listFiles()) {
				Console.getInstance().getPluginLoader().unload(f);
			}
		}
		System.exit(0);
		return null;
	}

}
