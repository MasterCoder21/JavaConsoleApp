package wtf.godlydev.console.console;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import wtf.godlydev.console.command.CommandRegistry;
import wtf.godlydev.console.command.commands.ChangeDirectoryCommand;
import wtf.godlydev.console.command.commands.ClearCommand;
import wtf.godlydev.console.command.commands.ConfigCommand;
import wtf.godlydev.console.command.commands.ExitCommand;
import wtf.godlydev.console.command.commands.HelpCommand;
import wtf.godlydev.console.command.commands.PluginsCommand;
import wtf.godlydev.console.command.commands.QuitCommand;
import wtf.godlydev.console.config.Config;
import wtf.godlydev.console.global.GlobalVariables;
import wtf.godlydev.console.plugin.PluginLoader;
import wtf.godlydev.console.utils.CLITools;
import wtf.godlydev.console.utils.EmptyProcess;

public class Console {

	private static Console INSTANCE;

	private final Scanner scanner;
	private final CLITools cliTools;
	private final Config config;
	private final PluginLoader pluginLoader;

	private Console() {
		this.scanner = new Scanner(System.in);
		this.cliTools = new CLITools();

		this.config = new Config(new File("ConsoleConfig.txt"));

		CommandRegistry.register(new HelpCommand(), null);
		CommandRegistry.register(new QuitCommand(), null);
		CommandRegistry.register(new ExitCommand(), null);
		CommandRegistry.register(new ChangeDirectoryCommand(), null);
		CommandRegistry.register(new ConfigCommand(), null);
		CommandRegistry.register(new PluginsCommand(), null);
		CommandRegistry.register(new ClearCommand(), null);
		// CommandRegistry.register(new DuplicateCommand(), null);
		// CommandRegistry.register(new DuplicateCommand2(), null);
		
		this.pluginLoader = new PluginLoader();
		for(File pluginDir : this.config.pluginPaths) {
			if(!pluginDir.exists()) {
				pluginDir.mkdirs();
			}
			for(File f : pluginDir.listFiles()) {
				this.pluginLoader.load(f);
			}
		}
	}

	public static Console getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Console();
		}
		return INSTANCE;
	}

	public void run(String[] args) {
		GlobalVariables.applicationWorkingDirectory = this.config.startingPath;
		this.cliTools.setTitle(this.config.defaultConsoleTitle);
		this.printPrefix();

		while (true) {
			try {
				String cmd = this.scanner.nextLine().strip();

				String[] cmdarr = cmd.split(" ");

				boolean builtin = this.testBuiltinCommands(cmdarr);
				if (builtin) {
					this.printPrefix();
					continue;
				}

				ProcessBuilder pb;
				boolean isCmdSlashC;

				try {
					isCmdSlashC = cmdarr[0].equals("cmd") && cmdarr[1].equals("/c");
				} catch (ArrayIndexOutOfBoundsException e) {
					isCmdSlashC = false;
				}

				if (isCmdSlashC)
					pb = new ProcessBuilder(cmdarr);
				else {
					String[] newcmd = new String[cmdarr.length + 2];
					newcmd[0] = "cmd";
					newcmd[1] = "/c";
					for (int i = 0; i < cmdarr.length; i++) {
						newcmd[i + 2] = cmdarr[i];
					}
					pb = new ProcessBuilder(newcmd);
				}
				pb.directory(GlobalVariables.applicationWorkingDirectory);
				pb.inheritIO();
				try {
					Process p = pb.start();
					try {
						p.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.printPrefix();
					continue;
				} catch (IOException e) {
					/*
					 * synchronized(this) { e.printStackTrace(); }
					 */
					this.printPrefix();
				}
			} catch (NoSuchElementException e) {
				this.config.save();
				for(File pluginDir : this.config.pluginPaths) {
					if(!pluginDir.exists()) {
						pluginDir.mkdirs();
					}
					for(File f : pluginDir.listFiles()) {
						this.pluginLoader.unload(f);
					}
				}
				System.exit(0);
			}
		}
		// this.scanner.close();
	}

	private boolean testBuiltinCommands(String[] cmd) {
		Process p;
		try {
			p = CommandRegistry.execute(cmd);
			return p instanceof EmptyProcess || p != null;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
	}

	public void printPrefix() {
		this.cliTools.setColorRGB(this.config.computerNameColor.getRed(), this.config.computerNameColor.getGreen(),
				this.config.computerNameColor.getBlue());
		try {
			System.out.print(System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostName() + " ");
		} catch (UnknownHostException e1) {
			System.out.print(System.getProperty("user.name") + " ");
		}
		this.cliTools.reset();
		this.cliTools.setColorRGB(this.config.pathColor.getRed(), this.config.pathColor.getGreen(),
				this.config.pathColor.getBlue());
		System.out.print(getPathText());
		this.cliTools.reset();
		System.out.println();
		System.out.print("$ ");
	}
	
	private String getPathText() {
		String current = GlobalVariables.applicationWorkingDirectory.getPath();
		String starting = this.config.startingPath.getPath();
		String currentParent = GlobalVariables.applicationWorkingDirectory.getParent();
		// String startingParent = this.config.startingPath.getParent();
		if(current.equals(starting)) { // Current directory = starting directory
			return "~";
		} else if(current.length() < starting.length()) { // Current directory is above the starting directory
			if(currentParent != null) {
				String s = current.replace(currentParent, "");
				if(s.charAt(0) != '\\') {
					s = '\\' + s;
				}
				return File.separator + s.split(Pattern.quote(File.separator))[1];
			} else {
				return "\\";
			}
		} else { // Current directory is below starting directory
			return "~" + current.replace(starting, "");
		}
	}

	public CLITools getCliTools() {
		return cliTools;
	}

	public Config getConfig() {
		return config;
	}
	
	public PluginLoader getPluginLoader() {
		return pluginLoader;
	}
	
	public Scanner getScanner() {
		return scanner;
	}

}
