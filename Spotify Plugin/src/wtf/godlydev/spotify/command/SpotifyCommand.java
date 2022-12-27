package wtf.godlydev.spotify.command;

import java.io.IOException;
import java.util.Scanner;

import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import wtf.godlydev.console.command.ICommand;
import wtf.godlydev.console.utils.EmptyProcess;
import wtf.godlydev.spotify.Spotify;
import wtf.godlydev.spotify.util.RegexUtils;
import wtf.godlydev.spotify.util.SpotifyCommandHelp;
import wtf.godlydev.spotify.util.SpotifyManager;

public class SpotifyCommand implements ICommand {

	@Override
	public Process execute(String[] arg0) throws IOException {
		if(arg0.length == 1) {
			System.out.println(this.help());
		} else {
			final Scanner sc = new Scanner(System.in);
			switch(arg0[1]) {
			case "setup":
				System.out.println("Hello there!  We'll ask you a few quick questions to complete the setup.");
				System.out.println();
				System.out.println("What is your application's client ID?  (If you don't know how to find this, type \"spotify help setup\".)");
				System.out.print(" > ");
				String id = sc.nextLine().strip();
				System.out.println("What is your application's client secret?  (If you don't know how to find this, type \"spotify help setup\".)");
				System.out.print(" > ");
				String secret = sc.nextLine().strip();
				System.out.println("You've completed the required setup!  Verification of your client ID and secret will happen when you attempt to connect your account.");
				Spotify.getInstance().getSpotifyConfig().spotifyClientID = id;
				Spotify.getInstance().getSpotifyConfig().spotifyClientSecret = secret;
				Spotify.getInstance().getSpotifyConfig().save();
				break;
			case "authorize":
			case "auth":
			case "login":
			case "connect":
				SpotifyManager.init();
				SpotifyManager.connectSpotify();
				break;
			case "playback":
				if(arg0.length == 2) {
					System.out.println("Various playback commands.");
				} else {
					if(Spotify.getInstance().getSpotifyConfig().spotifyClientID == null || Spotify.getInstance().getSpotifyConfig().spotifyClientSecret == null || 
							Spotify.getInstance().getSpotifyConfig().spotifyClientID.equals("null") || Spotify.getInstance().getSpotifyConfig().spotifyClientSecret.equals("null")) {
						System.out.println("Spotify API is not setup, please run \"spotify setup\"");
						break;
					}
					if(SpotifyManager.getSpotifyAPI() == null) {
						System.out.println("Spotify API not initialized, please run \"spotify connect\"");
						break;
					}
					if(!Spotify.getInstance().isSpotifyLoggedIn()) {
						System.out.println("Spotify not authorized, please run \"spotify connect\"");
						break;
					}
					switch(arg0[2]) {
					case "get":
					case "update":
						final Track t = SpotifyManager.getCurrentTrack();
						final CurrentlyPlaying cp = SpotifyManager.getCurrentlyPlaying();
						final ArtistSimplified[] artists = t.getArtists();
						String artistString = "";
						if(artists.length > 1) {
							artistString = artists[0].getName() + " + " + (artists.length - 1) + " more";
						} else {
							artistString = artists[0].getName();
						}
						System.out.println("Currently Playing: \"" + t.getName() + "\" by " + artistString);
						long msd = t.getDurationMs();
						long msp = cp.getProgress_ms();
						long md = (msd / 1000) / 60;
						long mp = (msp / 1000) / 60;
						long sd = (msd / 1000) % 60;
						long sp = (msp / 1000) % 60;
						System.out.println("Duration: " + mp + ":" + sp + " / " + md + ":" + sd);
						break;
					case "pause":
						SpotifyManager.pauseCurrentTrack();
						break;
					case "play":
					case "start":
					case "resume":
						SpotifyManager.startResumeCurrentTrack();
						break;
					case "next":
						SpotifyManager.skipToNextTrack();
						break;
					case "previous":
						SpotifyManager.skipToPreviousTrack();
						break;
					case "seek":
						if(arg0.length == 3) {
							System.out.println("There are missing arguments!");
							System.out.println("You can simply provide a time to seek to, or say if you'd like to seek forward or backward.");
							System.out.println("Refer to \"spotify help playback seek\" for correct time formatting.");
							break;
						}
						String a = arg0[3];
						final CurrentlyPlaying cp1 = SpotifyManager.getCurrentlyPlaying();
						switch(a) {
						case "forward":
							if(arg0.length == 4) {
								System.out.println("Please specify a time!");
								System.out.println("Refer to \"spotify help playback seek\" for correct time formatting.");
								break;
							}
							String b = arg0[4];
							int time1 = RegexUtils.timeStringConvert(b);
							SpotifyManager.seekTo(cp1.getProgress_ms() + time1);
							break;
						case "back":
						case "backward":
							if(arg0.length == 4) {
								System.out.println("Please specify a time!");
								System.out.println("Refer to \"spotify help playback seek\" for correct time formatting.");
								break;
							}
							String c = arg0[4];
							int time2 = RegexUtils.timeStringConvert(c);
							SpotifyManager.seekTo(cp1.getProgress_ms() - time2);
							break;
						default:
							int time = RegexUtils.timeStringConvert(a);
							SpotifyManager.seekTo(time);
							break;
						}
						break;
					}
				}
				break;
			case "help":
				if(arg0.length == 2) {
					System.out.println(this.help());
				} else {
					String contents = SpotifyCommandHelp.SUBCMD_CONTENTS.get(String.join(" ", arg0));
					if(contents != null) {
						System.out.println(contents);
					} else {
						System.out.println("There is currently no help for that command.");
					}
				}
				break;
			default:
				System.out.println("That's not a valid subcommand.");
				break;
			}
			// sc.close();
		}
		return new EmptyProcess();
	}

	@Override
	public String help() {
		return SpotifyCommandHelp.CONTENTS;
	}

	@Override
	public String name() {
		return "spotify";
	}

	@Override
	public String shortHelp() {
		return SpotifyCommandHelp.SHORT_HELP;
	}

}
