package wtf.godlydev.console.command.commands;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.console.Console;
import wtf.godlydev.console.global.GlobalVariables;
import wtf.godlydev.console.utils.EmptyProcess;
import wtf.godlydev.console.utils.Pair;

public class ConfigCommand implements ICommand {

	@Override
	public String name() {
		return "config";
	}

	@Override
	public String shortHelp() {
		return "CONFIG         Configure the look and feel of the console.";
	}

	@Override
	public String help() {
		return "Configure the look and feel of the console."
				+ "\n\n"
				+ "config ComputerNameColor [r (0-255)] [g (0-255)] [b (0-255)] - Change the color of the computer name text."
				+ "\n"
				+ "config PathColor [r (0-255)] [g (0-255)] [b (0-255)] - Change the color of the path color text."
				+ "\n"
				+ "config StartingPath [path] - Set the default working directory for " + GlobalVariables.APPLICATION_NAME + "."
				+ "\n"
				+ "config DefaultConsoleTitle [defaultTitle] - Set the default title for the " + GlobalVariables.APPLICATION_NAME + " window.  Keep in mind that this changes when the \"cd\" command is run."
				+ "\n"
				+ "config PluginPaths [list|add|remove <-c>] [path] - List, add, or remove paths that " + GlobalVariables.APPLICATION_NAME + " plugins are kept in.  Add -c when adding paths to make them if they don't exist."
				+ "\n";
	}

	@Override
	public Process execute(String[] cmd) throws IOException {
		try {
			switch (cmd[1]) { // thing to configure
			case "ComputerNameColor":
				try {
					Console.getInstance().getConfig().computerNameColor = new Color(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]), Integer.parseInt(cmd[4]));
					System.out.println("Set computer name color to " + Console.getInstance().getConfig().computerNameColor.getRGB() + "!");
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					System.out.println("Failed to set value: invalid input provided!  Please provide a 0-255 value for red, green, and blue.");
					return new EmptyProcess();
				}
				break;
			case "PathColor":
				try {
					Console.getInstance().getConfig().pathColor = new Color(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]), Integer.parseInt(cmd[4]));
					System.out.println("Set path color to " + Console.getInstance().getConfig().pathColor.getRGB() + "!");
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					System.out.println("Failed to set value: invalid input provided!  Please provide a 0-255 value for red, green, and blue.");
					return new EmptyProcess();
				}
				break;
			case "StartingPath":
				try {
					File f = new File(cmd[2]);
					if (!f.exists()) {
						System.out.println("Failed to set value: nonexistend path provided!");
						return new EmptyProcess();
					} else if (!f.isDirectory()) {
						System.out.println("Failed to set value: path provided is not a directory!");
						return new EmptyProcess();
					} else {
						Console.getInstance().getConfig().startingPath = f;
						System.out.println("Set starting path to \"" + f.getAbsolutePath() + "\"!");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Failed to set value: invalid input provided!  Please provide a valid pathname.");
					return new EmptyProcess();
				}
				break;
			case "DefaultConsoleTitle":
				try {
					Console.getInstance().getConfig().defaultConsoleTitle = cmd[2];
					System.out.println("Set default console title to \"" + cmd[2] + "\"!");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Failed to set value: invalid input provided!  Please provide a valid string.");
					return new EmptyProcess();
				}
				break;
			case "PluginPaths":
				for(File f : Console.getInstance().getConfig().pluginPaths) {
					if(!f.exists()) {
						f.mkdirs();
					}
				}
				try {
					String subcmd = cmd[2];
					switch(subcmd) {
					case "list":
						System.out.println(Console.getInstance().getConfig().pluginPaths.toString());
						return new EmptyProcess();
					case "add":
						try {
							String dir;
							boolean create;
							if(cmd[3].equals("-c") || cmd[3].equals("-create") || cmd[3].equals("--c") || cmd[3].equals("--create")) {
								dir = String.join(" ", Arrays.copyOfRange(cmd, 4, cmd.length));
								create = true;
							} else {
								dir = String.join(" ", Arrays.copyOfRange(cmd, 3, cmd.length));
								create = false;
							}
							File f = new File(dir);
							if(create) {
								f.mkdirs();
							}
							if (!f.exists()) {
								System.out.println("Failed to set value: nonexistend path provided!");
								return new EmptyProcess();
							} else if (!f.isDirectory()) {
								System.out.println("Failed to set value: path provided is not a directory!");
								return new EmptyProcess();
							} else {
								for(File f2 : Console.getInstance().getConfig().pluginPaths) {
									if(f2.getAbsolutePath().equals(f.getAbsolutePath())) {
										System.out.println("That path is already added!");
										return new EmptyProcess();
									} else {
										Console.getInstance().getConfig().pluginPaths.add(f);
										System.out.println("Plugin path \"" + f.getAbsolutePath() + "\" added!");
										break;
									}
								}
							}
							break;
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Failed to set value: invalid input provided!  Please provide a valid pathname.");
							return new EmptyProcess();
						}
					case "remove":
						try {
							String dir;
							boolean delete;
							if(cmd[3].equals("-d") || cmd[3].equals("-delete") || cmd[3].equals("--d") || cmd[3].equals("--delete") || cmd[3].equals("-r") || cmd[3].equals("-remove") || cmd[3].equals("--r") || cmd[3].equals("--remove") || cmd[3].equals("-rm") || cmd[3].equals("--rm")) {
								dir = String.join(" ", Arrays.copyOfRange(cmd, 4, cmd.length));
								delete = true;
							} else {
								dir = String.join(" ", Arrays.copyOfRange(cmd, 3, cmd.length));
								delete = false;
							}
							File f = new File(dir);
							if (!f.exists()) {
								System.out.println("Failed to set value: nonexistend path provided!");
								return new EmptyProcess();
							} else if (!f.isDirectory()) {
								System.out.println("Failed to set value: path provided is not a directory!");
								return new EmptyProcess();
							} else {
								boolean found = false;
								for(File f2 : Console.getInstance().getConfig().pluginPaths) {
									if(f2.getAbsolutePath().equals(f.getAbsolutePath())) {
										Console.getInstance().getConfig().pluginPaths.remove(f2);
										found = true;
										break;
									}
								}
								if(found) {
									System.out.println("Plugin path \"" + f.getAbsolutePath() + "\" removed!");
									if(delete) {
										Pair<Integer, Integer> p = this.recursiveDelete(f, 0, 0);
										System.out.println("Delete specified - Deleted " + p.getValue1() + " files and " + p.getValue2() + " directories");
									}
								}
								else
									System.out.println("That path isn't in the path list!");
							}
							break;
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Failed to set value: invalid input provided!  Please provide a valid pathname.");
							return new EmptyProcess();
						}
					case "reset":
						File f = new File("plugins");
						Console.getInstance().getConfig().pluginPaths.clear();
						Console.getInstance().getConfig().pluginPaths.add(f);
						System.out.println("Plugin paths reset!");
						break;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Failed to set value: invalid input provided!  Please provide a valid subcommand.");
					return new EmptyProcess();
				}
				break;
			default:
				System.out.println("Invalid config key provided!  Type \"help config\" to view what you can configure.");
				return new EmptyProcess();
			}
			Console.getInstance().getConfig().save();
			System.out.println("Config saved!");
		} catch (ArrayIndexOutOfBoundsException e) {
			printConfig();
		}

		return new EmptyProcess();
	}
	
	private Pair<Integer, Integer> recursiveDelete(final File f, int files, int dirs) {
		int fs = files;
		int ds = dirs;
		if(f.isDirectory()) {
			for(File f1 : f.listFiles()) {
				if(f1.isDirectory()) {
					this.recursiveDelete(f1, fs, ds);
					ds++;
				} else {
					f1.delete();
					fs++;
				}
			}
			ds++;
		} else {
			fs++;
		}
		f.delete();
		return new Pair<>(fs, ds);
	}
	
	private void printConfig() {
		System.out.println("Console Config");
		System.out.println("\t- ComputerNameColor: " + Console.getInstance().getConfig().computerNameColor.getRGB());
		System.out.println("\t- PathColor: " + Console.getInstance().getConfig().pathColor.getRGB());
		System.out.println("\t- StartingPath: " + Console.getInstance().getConfig().startingPath.getAbsolutePath());
		System.out.println("\t- DefaultConsoleTitle: " + Console.getInstance().getConfig().defaultConsoleTitle);
		System.out.println("\t- PluginPaths: " + Console.getInstance().getConfig().pluginPaths.toString());
	}
}
