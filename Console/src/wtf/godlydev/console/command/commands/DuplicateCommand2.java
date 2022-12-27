package wtf.godlydev.console.command.commands;

import java.io.IOException;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.utils.EmptyProcess;

public class DuplicateCommand2 implements ICommand {

	@Override
	public String name() {
		return "config";
	}

	@Override
	public String help() {
		return "lol";
	}

	@Override
	public String shortHelp() {
		return "lol";
	}

	@Override
	public Process execute(String[] cmd) throws IOException {
		return new EmptyProcess();
	}

}
