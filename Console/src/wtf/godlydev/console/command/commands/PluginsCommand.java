package wtf.godlydev.console.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.console.Console;
import wtf.godlydev.console.plugin.api.Plugin;
import wtf.godlydev.console.utils.EmptyProcess;

public class PluginsCommand implements ICommand {

	@Override
	public String name() {
		return "plugins";
	}

	@Override
	public String shortHelp() {
		return "PLUGINS        Display the list of currently loaded plugins.";
	}

	@Override
	public String help() {
		return "Display & manage the list of currently loaded plugins.\n\n"
				+ "plugins [list] - List all plugins.\n"
				+ "plugins [reload] [all|<plugin_file_name>] - Reload a single plugin or all existing plugins.\n"
				+ "plugins [load] [plugin_file_name] - Load a plugin.\n"
				+ "plugins [unload] [plugin_file_name] - Unload a plugin.\n";
	}

	@Override
	public Process execute(String[] cmd) throws IOException {
		if (cmd.length == 1) {
			System.out.println("Currently loaded plugins: "
					+ Console.getInstance().getPluginLoader().getLoadedPlugins().size());
			for (Entry<File, Plugin> p : Console.getInstance().getPluginLoader().getPluginMap().entrySet()) {
				System.out.println("----------");
				System.out.println(p.getValue().getDescriptionFile().getName() + " (\"" + p.getKey().getAbsolutePath() + "\")");
				System.out.println(p.getValue().getDescriptionFile().getVersion());
			}
		} else {
			switch (cmd[1]) {
			case "list":
				System.out.println("Currently loaded plugins: "
						+ Console.getInstance().getPluginLoader().getLoadedPlugins().size());
				for (Entry<File, Plugin> p : Console.getInstance().getPluginLoader().getPluginMap().entrySet()) {
					System.out.println("----------");
					System.out.println(p.getValue().getDescriptionFile().getName() + " (\"" + p.getKey().getAbsolutePath() + "\")");
					System.out.println(p.getValue().getDescriptionFile().getVersion());
				}
				break;
			case "reload":
				try {
					String val = cmd[2];
					switch (val) {
					case "-a":
					case "-all":
					case "--a":
					case "--all":
						System.out.println("Reloading...");
						long ms = System.currentTimeMillis();
						for (File pluginDir : Console.getInstance().getConfig().pluginPaths) {
							if (!pluginDir.exists()) {
								pluginDir.mkdirs();
							}
							for (File f : pluginDir.listFiles()) {
								Console.getInstance().getPluginLoader().reload(f);
								if(Console.getInstance().getPluginLoader().getPluginMap().containsKey(f)) {
									Plugin p = Console.getInstance().getPluginLoader().getPluginMap().get(f);
									System.out.println("Reloaded " + p.getDescriptionFile().getName() + ", version "
											+ p.getDescriptionFile().getVersion());
								}
							}
						}
						System.out
								.println("Reloaded " + Console.getInstance().getPluginLoader().getLoadedPlugins().size()
										+ " plugins in " + (System.currentTimeMillis() - ms) + " milliseconds.");
						break;
					default:
						val = String.join(" ", Arrays.copyOfRange(cmd, 2, cmd.length));
						if(val.endsWith(".jar")) {

						} else {
							val += ".jar";
						}
						
						int count = 0;
						for(File f : Console.getInstance().getConfig().pluginPaths) {
							for(File f2 : f.listFiles()) {
								if(f2.getName().equals(val)) {
									Console.getInstance().getPluginLoader().reload(f2);
									if(Console.getInstance().getPluginLoader().getPluginMap().containsKey(f2)) {
										Plugin p = Console.getInstance().getPluginLoader().getPluginMap().get(f2);
										System.out.println("Reloaded " + p.getDescriptionFile().getName() + ", version " + p.getDescriptionFile().getVersion() + " (\"" + f.getAbsolutePath() + "\")");
										count++;
									}
								}
							}
						}
						if(count == 0) {
							System.out.println("No plugin found with the name \"" + val + "\"");
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Missing argument!  Type -a to reload everything or a plugin file name to reload just one.");
				}
				break;
			case "load":
				try {
					String val = cmd[2];
					val = String.join(" ", Arrays.copyOfRange(cmd, 2, cmd.length));
					if(val.endsWith(".jar")) {

					} else {
						val += ".jar";
					}
					
					int count = 0;
					for(File f : Console.getInstance().getConfig().pluginPaths) {
						for(File f2 : f.listFiles()) {
							if(f2.getName().equals(val)) {
								if(Console.getInstance().getPluginLoader().getPluginMap().containsKey(f2)) {
									System.out.println("A plugin was found loaded with that name.  Try reloading that plugin.");
									count++;
								} else {
									Console.getInstance().getPluginLoader().load(f2);
									Plugin p = Console.getInstance().getPluginLoader().getPluginMap().get(f2);
									System.out.println("Loaded " + p.getDescriptionFile().getName() + ", version " + p.getDescriptionFile().getVersion() + " (\"" + f.getAbsolutePath() + "\")");
									count++;
								}
							}
						}
					}
					if(count == 0) {
						System.out.println("No plugin found with the name \"" + val + "\"");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Missing argument!  Type a plugin file name to load.");
				}
				break;
			case "unload":
				try {
					String val = cmd[2];
					val = String.join(" ", Arrays.copyOfRange(cmd, 2, cmd.length));
					if(val.endsWith(".jar")) {

					} else {
						val += ".jar";
					}
					
					int count = 0;
					for(File f : Console.getInstance().getConfig().pluginPaths) {
						for(File f2 : f.listFiles()) {
							if(f2.getName().equals(val)) {
								if(Console.getInstance().getPluginLoader().getPluginMap().containsKey(f2)) {
									Plugin p = Console.getInstance().getPluginLoader().getPluginMap().get(f2);
									Console.getInstance().getPluginLoader().unload(f2);
									System.out.println("Unloaded " + p.getDescriptionFile().getName() + ", version " + p.getDescriptionFile().getVersion() + " (\"" + f.getAbsolutePath() + "\")");
									count++;
								} else {
									
								}
							}
						}
					}
					if(count == 0) {
						System.out.println("No plugin found with the name \"" + val + "\"");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Missing argument!  Type a plugin file name to unload.");
				}
				break;
			}
		}
		return new EmptyProcess();
	}

}
