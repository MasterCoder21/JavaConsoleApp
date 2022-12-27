package wtf.godlydev.console.utils;

import java.io.File;

public class ProcessUtil {
	
	public static ProcessBuilder newEmptyProcess() {
		ProcessBuilder pb = new ProcessBuilder();
		File path = new File("C:\\Users\\" + System.getProperty("user.name"));
		pb.directory(path);
		pb.inheritIO();
		return pb;
	}

	public static String[] concatenateCommandArgs(String cmd, String[] args) {
		String[] new_ = new String[args.length + 1];
		new_[0] = cmd;
		for (int i = 0; i < args.length; i++) {
			new_[i + 1] = args[i];
		}
		return new_;
	}

}
