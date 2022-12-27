package wtf.godlydev.console.command.commands;

import java.io.IOException;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.utils.ProcessUtil;

public class ClearCommand implements ICommand {

	@Override
	public String name() {
		return "clear";
	}

	@Override
	public String help() {
		return "Clears the screen.\r\n"
				+ "\r\n"
				+ "CLEAR";
	}

	@Override
	public String shortHelp() {
		return "CLEAR          Clears the screen.";
	}

	@Override
	public Process execute(String[] cmd) throws IOException {
		ProcessBuilder pb = ProcessUtil.newEmptyProcess();
		pb.command("cmd", "/c", "cls");
		return pb.start();
	}

}
