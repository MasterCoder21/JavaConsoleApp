package wtf.godlydev.console.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.console.Console;
import wtf.godlydev.console.global.GlobalVariables;
import wtf.godlydev.console.utils.EmptyProcess;

public class ChangeDirectoryCommand implements ICommand {

	@Override
	public String name() {
		return "cd";
	}

	@Override
	public String shortHelp() {
		return "CD             Displays the name of or changes the current directory.";
	}

	@Override
	public String help() {
		return "Displays the name of or changes the current directory.\r\n"
				+ "\r\n"
				+ "CHDIR [/D] [drive:][path]\r\n"
				+ "CHDIR [..]\r\n"
				+ "CD [/D] [drive:][path]\r\n"
				+ "CD [..]\r\n"
				+ "\r\n"
				+ "  ..   Specifies that you want to change to the parent directory.\r\n"
				+ "\r\n"
				+ "Type CD drive: to display the current directory in the specified drive.\r\n"
				+ "Type CD without parameters to display the current drive and directory.\r\n"
				+ "\r\n"
				+ "Use the /D switch to change current drive in addition to changing current\r\n"
				+ "directory for a drive.\r\n"
				+ "\r\n"
				+ "If Command Extensions are enabled CHDIR changes as follows:\r\n"
				+ "\r\n"
				+ "The current directory string is converted to use the same case as\r\n"
				+ "the on disk names.  So CD C:\\TEMP would actually set the current\r\n"
				+ "directory to C:\\Temp if that is the case on disk.\r\n"
				+ "\r\n"
				+ "CHDIR command does not treat spaces as delimiters, so it is possible to\r\n"
				+ "CD into a subdirectory name that contains a space without surrounding\r\n"
				+ "the name with quotes.  For example:\r\n"
				+ "\r\n"
				+ "    cd \\winnt\\profiles\\username\\programs\\start menu\r\n"
				+ "\r\n"
				+ "is the same as:\r\n"
				+ "\r\n"
				+ "    cd \"\\winnt\\profiles\\username\\programs\\start menu\"\r\n"
				+ "\r\n"
				+ "which is what you would have to type if extensions were disabled.";
	}

	@Override
	public Process execute(String[] cmd) throws IOException {
		if (cmd.length == 1) {
			System.out.println(GlobalVariables.applicationWorkingDirectory);
		} else {
			// String dir = cmd[1];
			String dir = String.join(" ", Arrays.copyOfRange(cmd, 1, cmd.length));

			if (dir.equals("..")) {
				File f = GlobalVariables.applicationWorkingDirectory;
				String[] p = f.getAbsolutePath().split(Pattern.quote(File.separator));
				if(p.length == 1) {
					return new EmptyProcess();
				}
				String np = "";
				for (int i = 0; i < p.length - 1; i++) {
					np += p[i] + File.separator;
				}
				GlobalVariables.applicationWorkingDirectory = new File(np);
				Console.getInstance().getCliTools()
						.setTitle(GlobalVariables.applicationWorkingDirectory.getAbsolutePath());
			} else if (dir.equals(".")) {

			} else {
				if (dir.contains("\""))
					dir = dir.substring(1, dir.length() - 1);
				else if (dir.contains(":")) {
				
				}
				else {
					if(dir.startsWith(File.separator)) {
						
					} else {
						dir = File.separator + dir;
					}
					dir = GlobalVariables.applicationWorkingDirectory.getAbsolutePath() + dir;
				}
				File f = new File(dir);
				if (!f.exists())
					System.out.println("The system cannot find the path specified.");
				else if (f.isFile())
					System.out.println("The directory name is invalid.");
				else {
					GlobalVariables.applicationWorkingDirectory = new File(dir);
					Console.getInstance().getCliTools()
							.setTitle(GlobalVariables.applicationWorkingDirectory.getAbsolutePath());
				}
			}
		}

		return new EmptyProcess();
	}

}
