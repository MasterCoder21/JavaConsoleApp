package wtf.godlydev.chat.commands;

import java.io.IOException;

import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.utils.EmptyProcess;

public class ChatCommand implements ICommand {

	@Override
	public Process execute(String[] arg0) throws IOException {
		if(arg0.length == 1) {
			System.out.println(this.help());
			return new EmptyProcess();
		} else {
			return new EmptyProcess();
		}
	}

	@Override
	public String help() {
		return "Help for the main chat plugin command";
	}

	@Override
	public String name() {
		return "chat";
	}

	@Override
	public String shortHelp() {
		return "CHAT";
	}

}
