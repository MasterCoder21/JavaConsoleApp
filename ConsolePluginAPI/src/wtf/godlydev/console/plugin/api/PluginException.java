package wtf.godlydev.console.plugin.api;

public class PluginException extends RuntimeException {

	private static final long serialVersionUID = -8161437637562938254L;

	public PluginException(String whatHappened) {
		super(whatHappened);
	}
	
	public PluginException(String whatHappened, Throwable cause) {
		super(whatHappened, cause);
	}
	
}
