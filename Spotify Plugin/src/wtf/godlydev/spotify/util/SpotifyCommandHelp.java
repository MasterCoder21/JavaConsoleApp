package wtf.godlydev.spotify.util;

import java.util.HashMap;
import java.util.Map;

import wtf.godlydev.spotify.Spotify;

public class SpotifyCommandHelp {

	public static final String CONTENTS;
	public static final Map<String, String> SUBCMD_CONTENTS;
	public static final String SHORT_HELP;

	static {
		SHORT_HELP = "SPOTIFY        Control Spotify playback within your console.";

		final StringBuilder sb = new StringBuilder();
		sb.append("Spotify Controller 1.0.0");
		sb.append('\n');
		sb.append("Control Spotify playback within your console.");
		sb.append("\n\n");
		sb.append("setup");
		sb.append('\n');
		sb.append(
				"\tConfigure the spotify controller with your application's ID and secret.  The ID and secret are stored locally and never shared across the internet.");
		sb.append(
				"\tRunning this command will make the console prompt you to enter your application's ID and secret.  This command must be run before attempting to make any API requests.");
		sb.append('\n');
		sb.append("authorize|auth|login|connect");
		sb.append('\n');
		sb.append(
				"\tOpens an authorization window in your browser.  Once you log into Spotify, you'll be asked to allow your application to access to your account.  Doing that will then authorize your account with this plugin.");
		sb.append('\n');
		sb.append("playback");
		sb.append('\n');
		sb.append("\tVarious commands to control playback.");
		sb.append('\n');
		sb.append("\tget|update");
		sb.append('\n');
		sb.append("\t\tFetch and display your currently playing track and its progress.");
		sb.append('\n');
		sb.append("\tpause");
		sb.append('\n');
		sb.append("\t\tPause playback.");
		sb.append('\n');
		sb.append("\tplay|start|resume");
		sb.append('\n');
		sb.append("\t\tPlays/starts/resumes playback.");
		sb.append('\n');
		sb.append("\tnext");
		sb.append('\n');
		sb.append("\t\tSkips to the next song.");
		sb.append('\n');
		sb.append("\tprevious");
		sb.append('\n');
		sb.append("\t\tSkips to the previous song.");
		sb.append('\n');
		sb.append("\tseek [amount]");
		sb.append('\n');
		sb.append("\t\tSeek to a point in the currently playing song.");
		sb.append('\n');
		sb.append(
				"\t\tThis command uses time formatting!  To specify an amount of time, type a number (1-999), followed by either \"ms\" (milliseconds), \"s\" (seconds), or \"m\" (minutes).  Don't separate anything by spaces.  Example: 3m24s113ms");
		sb.append('\n');
		sb.append("\t\tforward [amount]");
		sb.append('\n');
		sb.append("\t\t\tSeek forward by the specified amount.");
		sb.append('\n');
		sb.append("\t\tback|backward [amount]");
		sb.append('\n');
		sb.append("\t\t\tSeek backward by the specified amount.");
		sb.append('\n');
		CONTENTS = sb.toString();

		SUBCMD_CONTENTS = new HashMap<>();
		SUBCMD_CONTENTS.put("spotify help setup", "setup - Configure your application ID and secret."
				+ "\n\nHow to create an application:" + "\n\t1. Go to https://developer.spotify.com/dashboard/"
				+ "\n\t2. Click \"Log In\" and login with your Spotify account." + "\n\t3. Click \"Create an App.\""
				+ "\n\t4. Enter in an app name and description, and agree to Spotify's TOS."
				+ "\n\t5. Select that app and click \"Edit Settings.\""
				+ "\n\t6. In the \"Redirect URIs\" section, add the following: \"http://localhost:9103/callback/\""
				+ "\n\t7. Click \"Save.\""
				+ "\n\nYour application is now ready!  When prompted for the application's ID and secret, simply copy and paste those from the developer dashboard into the console.");
		SUBCMD_CONTENTS.put("spotify help authorize",
				"authorize|auth|login|connect - Link your Spotify account with this plugin."
						+ "\n\nRunning this command will open a Spotify login window in your browser.  Once you log in & allow your application to access your account, you can start controlling playback through your console.");
		SUBCMD_CONTENTS.put("spotify help auth",
				"authorize|auth|login|connect - Link your Spotify account with this plugin."
						+ "\n\nRunning this command will open a Spotify login window in your browser.  Once you log in & allow your application to access your account, you can start controlling playback through your console.");
		SUBCMD_CONTENTS.put("spotify help login",
				"authorize|auth|login|connect - Link your Spotify account with this plugin."
						+ "\n\nRunning this command will open a Spotify login window in your browser.  Once you log in & allow your application to access your account, you can start controlling playback through your console.");
		SUBCMD_CONTENTS.put("spotify help connect",
				"authorize|auth|login|connect - Link your Spotify account with this plugin."
						+ "\n\nRunning this command will open a Spotify login window in your browser.  Once you log in & allow your application to access your account, you can start controlling playback through your console.");
		SUBCMD_CONTENTS.put("spotify help playback", "playback - Various commands to control Spotify playback.");
	}

}
