package wtf.godlydev.spotify.util;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.hc.core5.http.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import wtf.godlydev.console.console.Console;
import wtf.godlydev.spotify.Spotify;

public class SpotifyManager {

	private static SpotifyApi spotifyAPI;
	private static boolean logged = false;
	private static HttpServer server;
	
	public static User currentUser;

	public static void init() {
		spotifyAPI = new SpotifyApi.Builder().setClientId(Spotify.getInstance().getSpotifyConfig().spotifyClientID)
				.setClientSecret(Spotify.getInstance().getSpotifyConfig().spotifyClientSecret)
				.setRedirectUri(SpotifyHttpManager.makeUri(Spotify.getInstance().getSpotifyConfig().spotifyRedirectURI)).build();
	}

	public static void connectSpotify() {
		attemptToOpenAuthURL();
	}
	
	private static void attemptToOpenAuthURL() {
		try {
			initializeHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final Desktop desktop = Desktop.getDesktop();
		System.out.println(Spotify.getInstance().getSpotifyConfig().spotifyRedirectURI);
		final String URL = "https://accounts.spotify.com/authorize?client_id="
				+ Spotify.getInstance().getSpotifyConfig().spotifyClientID
				+ "&response_type=code&redirect_uri=" + Spotify.getInstance().getSpotifyConfig().spotifyRedirectURI + "&scope=user-read-playback-state%20user-read-currently-playing%20user-modify-playback-state&state=34fFs29kd09";
		
		try {
			desktop.browse(new URI(URL));
		} catch (URISyntaxException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}
	
	private static void initializeHandler() throws IOException {
		try {
			SpotifyManager.server = HttpServer.create(new InetSocketAddress(9103), 0);
		} catch (IOException e) {
			if(e instanceof BindException) {
				SpotifyManager.server.stop(0);
			}
			throw e;
		}
		SpotifyManager.server.createContext("/callback", new SpotifyCallbackHandler());
		SpotifyManager.server.setExecutor(null);
		SpotifyManager.server.start();
	}

	private static void handleRequest(final String code) {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		final Runnable logout = new Runnable() {
			@Override
			public void run() {
				if(SpotifyManager.getCurrentlyPlaying() != null && SpotifyManager.getCurrentlyPlaying().getIs_playing()) {
					SpotifyManager.pauseCurrentTrack();
				}
				SpotifyManager.logged = false;
				SpotifyManager.currentUser = null;
				Spotify.getInstance().setSpotifyLoggedIn(false);
				
				System.out.println("Your session expired!  Please run \"spotify connect\" to refresh your session.");
				Console.getInstance().printPrefix();
				return;
			}
		};
		final ScheduledFuture<?> logoutHandler = scheduler.scheduleAtFixedRate(logout, 1L, 1L, TimeUnit.HOURS);
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				logoutHandler.cancel(true);
			}
		}, 1L, TimeUnit.HOURS);
		try {
			final SpotifyApi spotifyAPI = new SpotifyApi.Builder()
					.setClientId(Spotify.getInstance().getSpotifyConfig().spotifyClientID)
					.setClientSecret(Spotify.getInstance().getSpotifyConfig().spotifyClientSecret)
					.setRedirectUri(SpotifyHttpManager.makeUri(Spotify.getInstance().getSpotifyConfig().spotifyRedirectURI)).build();

			final AuthorizationCodeRequest authorizationCodeRequest = spotifyAPI.authorizationCode(code).build();

			final CompletableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = authorizationCodeRequest
					.executeAsync();

			// Thread free to do other tasks...

			// Example Only. Never block in production code.
			final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.join();

			// Set access and refresh token for further "spotifyApi" object usage
			SpotifyManager.spotifyAPI.setAccessToken(authorizationCodeCredentials.getAccessToken());
			SpotifyManager.spotifyAPI.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
			if (SpotifyManager.spotifyAPI.getRefreshToken() != null) {
				SpotifyManager.logged = true;
				SpotifyManager.currentUser = SpotifyManager.getCurrentUser();
				Spotify.getInstance().setSpotifyLoggedIn(true);
				// SpotifyManager.spotifyAPI.getUsersCurrentlyPlayingTrack().build().execute();
			}
			
			System.out.println("Connected account: " + SpotifyManager.currentUser.getDisplayName());
			System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
			Console.getInstance().printPrefix();
			
			/*
			final CurrentlyPlaying cp = SpotifyManager.getCurrentlyPlaying();
			if(SpotifyManager.getCurrentlyPlaying() != null && !SpotifyManager.getCurrentlyPlaying().getIs_playing()) {
				SpotifyManager.startResumeCurrentTrack();
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SpotifyApi getSpotifyAPI() {
		return spotifyAPI;
	}
	
	public static boolean isLoggedIn() {
		return getCurrentUser() != null && SpotifyManager.logged;
	}
	
	public static void setLogged(boolean logged) {
		SpotifyManager.logged = logged;
	}
	
	public static User getCurrentUser() {
		try {
			return SpotifyManager.spotifyAPI.getCurrentUsersProfile().build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// e.printStackTrace();
			return null;
		}
	}
	
	public static CurrentlyPlaying getCurrentlyPlaying() {
		try {
			return SpotifyManager.spotifyAPI.getUsersCurrentlyPlayingTrack().build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static AlbumSimplified getCurrentlyPlayingAlbum() {
		try {
			Track t = SpotifyManager.spotifyAPI.getTrack(getCurrentlyPlaying().getItem().getId()).build().execute();
			if(t != null) {
				if(t.getAlbum() != null) return t.getAlbum();
				else return null;
			} else return null;
			// return (as != null ? as : null);
		} catch (ParseException | SpotifyWebApiException | IOException | NullPointerException e) {
			if(!(e instanceof NullPointerException)) e.printStackTrace();
			return null;
		}
	}
	
	public static Track getCurrentTrack() {
		try {
			return SpotifyManager.spotifyAPI.getTrack(getCurrentlyPlaying().getItem().getId()).build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static CurrentlyPlayingContext getCurrentTrackInfo() {
		try {
			return SpotifyManager.spotifyAPI.getInformationAboutUsersCurrentPlayback().build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void pauseCurrentTrack() {
		try {
			SpotifyManager.spotifyAPI.pauseUsersPlayback().build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void startResumeCurrentTrack() {
		try {
			SpotifyManager.spotifyAPI.startResumeUsersPlayback().build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void skipToPreviousTrack() {
		try {
			SpotifyManager.spotifyAPI.skipUsersPlaybackToPreviousTrack().build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void seekTo(int ms) {
		try {
			SpotifyManager.spotifyAPI.seekToPositionInCurrentlyPlayingTrack(ms).build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void skipToNextTrack() {
		try {
			SpotifyManager.spotifyAPI.skipUsersPlaybackToNextTrack().build().execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Playlist getPlaylist(String id) {
		try {
			return SpotifyManager.spotifyAPI.getPlaylist(id).build().execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void addItemToPlaybackQueue(String id) {
		try {
			SpotifyManager.spotifyAPI.addItemToUsersPlaybackQueue(id).build().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class SpotifyCallbackHandler implements HttpHandler {
		@Override
		public void handle(final HttpExchange t) throws IOException {
			Multithreading.runAsync(() -> handleRequest(t.getRequestURI().toString().replace("/callback/?code=", "")
					.substring(0, t.getRequestURI().toString().replace("/callback/?code=", "").length() - 18)));
			final String response = "Success! You may close this window.";
			t.sendResponseHeaders(200, response.length());
			final OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			SpotifyManager.server.stop(0);
		}
	}

}
