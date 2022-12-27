package wtf.godlydev.console.plugin.api;

import java.util.Scanner;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.config.Config;
import wtf.godlydev.console.console.Console;
import wtf.godlydev.console.plugin.PluginLoader;
import wtf.godlydev.console.utils.CLITools;

public abstract class Plugin {

	public void onEnable() {
	}

	public void onDisable() {
	}

	public final void registerCommand(final ICommand command) {
	}

	public final void unregisterCommand(final ICommand command) {
	}

	public final Console getConsole() {
		return Console.getInstance();
	}

	public final Config getConfig() {
		return Console.getInstance().getConfig();
	}

	public final CLITools getCLITools() {
		return Console.getInstance().getCliTools();
	}

	public final Scanner getScanner() {
		return Console.getInstance().getScanner();
	}

	public final PluginLoader getPluginLoader() {
		return Console.getInstance().getPluginLoader();
	}

}
