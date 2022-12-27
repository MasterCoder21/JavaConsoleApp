package wtf.godlydev.exampleplugin.command;

import java.io.IOException;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.utils.EmptyProcess;
import wtf.godlydev.console.utils.ProcessUtil;

public class AddCommand implements ICommand {

	@Override
	public Process execute(String[] arg0) throws IOException {
		ProcessBuilder pb = ProcessUtil.newEmptyProcess();
		if(arg0.length != 3) {
			System.out.println("Please provide two number arguments!");
			return new EmptyProcess();
		}
		try {
			int thing = Integer.parseInt(arg0[1]) + Integer.parseInt(arg0[2]);
			pb.command("cmd", "/c", "echo", "" + thing);
			return pb.start();
		} catch (NumberFormatException e) {
			System.out.println("Please provide two number arguments!");
			return new EmptyProcess();
		}
	}

	@Override
	public String help() {
		return "Add two numbers together!";
	}

	@Override
	public String shortHelp() {
		return "ADD            Add two numbers together!";
	}

	@Override
	public String name() {
		return "add";
	}

}
