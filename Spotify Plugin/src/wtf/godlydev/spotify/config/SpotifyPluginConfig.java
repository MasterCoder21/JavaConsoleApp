package wtf.godlydev.spotify.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SpotifyPluginConfig {
		
	public String spotifyClientID;
	public String spotifyClientSecret;
	public final String spotifyRedirectURI = "http://localhost:9103/callback/";
	
	private final File configFile;
	
	public SpotifyPluginConfig(final File configFile) {
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
				e.printStackTrace();
				this.writeDefaults();
			}
		}
	}
	
	private void parse() throws FileNotFoundException {
		final Scanner sc = new Scanner(this.getConfigFile());

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
			case "CLIENT_ID":
				this.spotifyClientID = value;
				break;
			case "CLIENT_SECRET":
				this.spotifyClientSecret = value;
				break;
			}
		}

		sc.close();
	}

	private void write() throws IOException {
		final FileWriter writer = new FileWriter(this.getConfigFile());

		writer.write(
				"# DO NOT MANUALLY EDIT THIS FILE!\n# DOING SO COULD RESULT IN LOSS OF DATA OR APPLICATION MALFUNCTION.\n");
		writer.write("CLIENT_ID:" + this.spotifyClientID + "\n");
		writer.write("CLIENT_SECRET:" + this.spotifyClientSecret + "\n");

		writer.close();
	}

	private void provideDefaults() {
		this.spotifyClientID = null;
		this.spotifyClientSecret = null;
	}

	private void writeDefaults() {
		FileWriter writer;
		try {
			writer = new FileWriter(this.getConfigFile());

			writer.write(
					"# DO NOT MANUALLY EDIT THIS FILE!\n# DOING SO COULD RESULT IN LOSS OF DATA OR APPLICATION MALFUNCTION.\n");
			writer.write("CLIENT_ID:NONE\n");
			writer.write("PathColor:NONE\n");;

			writer.close();
		} catch (IOException e) {

		}
	}

	public File getConfigFile() {
		return configFile;
	}

}
