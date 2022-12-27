package wtf.godlydev.exampleplugin.command;

import java.io.IOException;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.utils.ProcessUtil;

public class ExampleCommand implements ICommand {

	@Override
	public Process execute(String[] arg0) throws IOException {
		ProcessBuilder pb = ProcessUtil.newEmptyProcess();
		pb.command("cmd", "/c", "echo", "Hello World!");
		return pb.start();
	}

	@Override
	public String help() {
		return "A simple hello world command!";
	}

	@Override
	public String shortHelp() {
		return "HELLOWORLD     A simple hello world command!";
	}

	@Override
	public String name() {
		return "helloworld";
	}

}
