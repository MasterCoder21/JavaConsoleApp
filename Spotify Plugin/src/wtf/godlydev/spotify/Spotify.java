package wtf.godlydev.spotify;

import java.io.File;

import wtf.godlydev.console.plugin.api.Plugin;
import wtf.godlydev.spotify.command.SpotifyCommand;
import wtf.godlydev.spotify.config.SpotifyPluginConfig;

public class Spotify extends Plugin {
	
	private SpotifyPluginConfig config;
	
	private SpotifyCommand spotifyCommand;
	
	private static Spotify INSTANCE;
	
	private boolean spotifyLoggedIn = false;
	
	public Spotify() {
		this.config = new SpotifyPluginConfig(new File("spotify-config.txt"));
	}
	
	@Override
	public void onEnable() {
		this.registerCommand(this.spotifyCommand = new SpotifyCommand());
		
		// super.onEnable();
	}
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void onDisable() {
		this.config.save();
		
		// super.onDisable();
	}

	public static Spotify getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Spotify();
		}
		return INSTANCE;
	}
	
	public SpotifyPluginConfig getSpotifyConfig() {
		return config;
	}
	
	public boolean isSpotifyLoggedIn() {
		return spotifyLoggedIn;
	}
	
	public void setSpotifyLoggedIn(boolean spotifyLoggedIn) {
		this.spotifyLoggedIn = spotifyLoggedIn;
	}

}
