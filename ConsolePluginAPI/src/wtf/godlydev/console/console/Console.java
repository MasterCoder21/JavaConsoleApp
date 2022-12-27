package wtf.godlydev.console.console;

import java.util.Scanner;

import wtf.godlydev.console.config.Config;
import wtf.godlydev.console.plugin.PluginLoader;
import wtf.godlydev.console.utils.CLITools;

public abstract class Console {

	private Console() { }

	public static Console getInstance() {
		return null;
	}

	public abstract CLITools getCliTools();

	public abstract Config getConfig();
	
	public abstract PluginLoader getPluginLoader();
	
	public abstract void printPrefix();
	
	public abstract Scanner getScanner();

}
