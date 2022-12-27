package wtf.godlydev.console.config;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Config {

	private static final int PROPERTY_COUNT = 4;

	private final File configFile;

	public Color computerNameColor;
	public Color pathColor;

	public File startingPath;
	public List<File> pluginPaths;

	public String defaultConsoleTitle;

	public Config(final File configFile) {
		this.configFile = configFile;
		try {
			this.parse();
		} catch (FileNotFoundException e) {
			this.provideDefaults();
		}
	}

	public void save() {
		synchronized (this) {
			try {
				this.write();
			} catch (IOException e) {
				this.writeDefaults();
			}
		}
	}

	private void parse() throws FileNotFoundException {
		final Scanner sc = new Scanner(this.getConfigFile());

		int count = 0;
		while (sc.hasNextLine()) {
			String line = sc.nextLine().strip();

			if (line.startsWith("#") || line.isEmpty()) {
				continue;
			}

			String[] prop = line.split(":", 2);
			if (prop.length == 0) {
				continue;
			}
			String key = prop[0];
			String value = prop[1];

			switch (key) {
			case "ComputerNameColor":
				try {
					this.computerNameColor = new Color(Integer.parseInt(value));
				} catch (NumberFormatException e) {
					this.computerNameColor = Color.green;
				}
				count++;
				break;
			case "PathColor":
				try {
					this.pathColor = new Color(Integer.parseInt(value));
				} catch (NumberFormatException e) {
					this.pathColor = Color.yellow;
				}
				count++;
				break;
			case "StartingPath":
				this.startingPath = new File(value);
				if (!this.startingPath.exists()) {
					this.startingPath = new File("C:\\Users\\" + System.getProperty("user.name"));
				}
				count++;
				break;
			case "DefaultConsoleTitle":
				this.defaultConsoleTitle = value;
				count++;
				break;
			case "PluginPaths":
				this.pluginPaths = new ArrayList<>();
				value = value.substring(1, value.length() - 1);
				String[] paths = value.split(";");
				for (String s : paths) {
					try {
						File path = new File(s.substring(1, s.length() - 1));
						if (path.exists() && path.isDirectory()) {
							this.pluginPaths.add(path);
						}
					} catch (StringIndexOutOfBoundsException e) {
					}
				}
			}
		}
		if (count != PROPERTY_COUNT) {
			this.provideDefaults();
		}

		sc.close();
	}

	private void write() throws IOException {
		final FileWriter writer = new FileWriter(this.getConfigFile());

		writer.write(
				"# DO NOT MANUALLY EDIT THIS FILE!\n# DOING SO COULD RESULT IN LOSS OF DATA OR APPLICATION MALFUNCTION.\n");
		writer.write("ComputerNameColor:" + this.computerNameColor.getRGB() + "\n");
		writer.write("PathColor:" + this.pathColor.getRGB() + "\n");
		writer.write("StartingPath:" + this.startingPath.getAbsolutePath() + "\n");
		writer.write("DefaultConsoleTitle:" + this.defaultConsoleTitle + "\n");

		StringBuilder pluginPathString = new StringBuilder("PluginPaths:[");
		if (this.pluginPaths == null) {
			this.pluginPaths = Arrays.asList();
		}
		for (File f : this.pluginPaths) {
			pluginPathString.append("\"" + f.getAbsolutePath() + "\";");
		}
		pluginPathString.append("]");
		writer.write(pluginPathString.toString());

		writer.close();
	}

	private void provideDefaults() {
		this.computerNameColor = Color.green;
		this.pathColor = Color.yellow;
		this.startingPath = new File("C:\\Users\\" + System.getProperty("user.name"));
		this.defaultConsoleTitle = "~";
		this.pluginPaths = Arrays.asList(new File("plugins"));
	}

	private void writeDefaults() {
		FileWriter writer;
		try {
			writer = new FileWriter(this.getConfigFile());

			writer.write(
					"# DO NOT MANUALLY EDIT THIS FILE!\n# DOING SO COULD RESULT IN LOSS OF DATA OR APPLICATION MALFUNCTION.\n");
			writer.write("ComputerNameColor:" + Color.green.getRGB() + "\n");
			writer.write("PathColor:" + Color.yellow.getRGB() + "\n");
			writer.write("StartingPath:" + new File("C:\\Users\\" + System.getProperty("user.name")) + "\n");
			writer.write("DefaultConsoleTitle:~\n");
			writer.write("PluginPaths:[\"plugins\";]");

			writer.close();
		} catch (IOException e) {

		}
	}

	public File getConfigFile() {
		return configFile;
	}

}
