package wtf.godlydev.console.plugin.api;

import wtf.godlydev.console.command.CommandRegistry;
import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.config.Config;
import wtf.godlydev.console.console.Console;
import wtf.godlydev.console.plugin.PluginLoader;
import wtf.godlydev.console.utils.CLITools;

public abstract class Plugin {

	private PluginDescriptionFile descriptionFile;
	
	public void onEnable() {}
	public void onDisable() {}

	public final void registerCommand(final ICommand command) {
		/*
		try {
			Class<?> clazz = Class.forName("wtf.godlydev.console.command.CommandRegistry");
			Class<?>[] cArg = new Class[] {ICommand.class};
			try {
				clazz.getDeclaredMethod("register", cArg).invoke(null, command);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {

			}
		} catch (ClassNotFoundException e) {

		}
		*/
		CommandRegistry.register(command, this);
	}
	public final void unregisterCommand(final ICommand command) {
		CommandRegistry.unregister(command);
	}

	public final Console getConsole() {
		return Console.getInstance();
	}

	public final Config getConfig() {
		return this.getConsole().getConfig();
	}

	public final CLITools getCLITools() {
		return this.getConsole().getCliTools();
	}

	public final PluginLoader getPluginLoader() {
		return this.getConsole().getPluginLoader();
	}
	
	public final void setDescriptionFile(PluginDescriptionFile descriptionFile) {
		if(this.descriptionFile != null) {
			throw new PluginException("Can't set the description file. Its already set!");
		}
		this.descriptionFile = descriptionFile;
	}
	
	public final PluginDescriptionFile getDescriptionFile() {
		return descriptionFile;
	}
	
}
