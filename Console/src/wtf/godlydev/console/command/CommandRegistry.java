package wtf.godlydev.console.command;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import wtf.godlydev.console.plugin.api.Plugin;

public class CommandRegistry {

	private static final Map<ICommand, Plugin> COMMAND_REGISTRY;
	private static int dupeCount = 0;

	public static void register(final ICommand command, final Plugin plugin) {
		try {
			for (ICommand c : COMMAND_REGISTRY.keySet()) {
				if (command.name().equals(c.name())) {
					dupeCount++;
					register(new ICommand() {
						@Override
						public String name() {
							if (command.name().endsWith("-" + (dupeCount - 1))) {
								String[] split = command.name().split("-",
										(int) (command.name().chars().filter(ch -> ch == '-').count() - 1));
								// System.out.println(String.join("", split));
								return String.join("", split) + "-" + dupeCount;
							} else {
								return command.name() + "-" + dupeCount;
							}
						}

						@Override
						public String help() {
							return command.help();
						}

						@Override
						public String shortHelp() {
							return command.shortHelp();
						}

						@Override
						public Process execute(String[] cmd) throws IOException {
							return command.execute(cmd);
						}

					}, plugin);
					return;
				}
			}
			dupeCount = 0;
			// System.out.println(command.name());
			CommandRegistry.COMMAND_REGISTRY.put(command, plugin);
			return;
		} catch (ConcurrentModificationException e) {

		}
	}

	public static void unregister(final ICommand command) {

		if (CommandRegistry.COMMAND_REGISTRY.containsKey(command)) {
			CommandRegistry.COMMAND_REGISTRY.remove(command);
		}
	}

	public static Process execute(String[] cmd) throws IOException {
		for (ICommand command : CommandRegistry.COMMAND_REGISTRY.keySet()) {
			if (command.name().equals(cmd[0])) {
				Process p = command.execute(cmd);
				try {
					p.waitFor();
				} catch (InterruptedException e) {

				} catch (NullPointerException e) {

				}
				return p;
			}
		}

		return null;
	}

	public static boolean commandExists(String cmd) {
		for (ICommand command : COMMAND_REGISTRY.keySet()) {
			if (command.name().equals(cmd)) {
				return true;
			}
		}
		return false;
	}

	public static ICommand getCommandByName(String cmd) {
		for (ICommand command : COMMAND_REGISTRY.keySet()) {
			if (command.name().equals(cmd)) {
				return command;
			}
		}
		return null;
	}

	public static Map<ICommand, Plugin> getCommandRegistry() {
		return COMMAND_REGISTRY;
	}

	static {
		COMMAND_REGISTRY = new HashMap<>();
	}

}