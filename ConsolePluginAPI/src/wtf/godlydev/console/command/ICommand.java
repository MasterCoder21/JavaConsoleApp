package wtf.godlydev.console.command;

import java.io.IOException;

public interface ICommand {
	
	String name();
	default String[] aliases() {
		return new String[0];
	}
	String help();
	String shortHelp();
	Process execute(String[] cmd) throws IOException;

}
