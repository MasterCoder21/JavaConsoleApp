package wtf.godlydev.console.plugin.api;

public class PluginDescriptionFile {

	private final String main, name, version;

	public PluginDescriptionFile(String name, String main, String version) {
		this.name = name;
		this.main = main;
		this.version = version;
	}
	
	public String getName() {
		return name;
	}
	public String getMain() {
		return main;
	}
	public String getVersion() {
		return version;
	}

}
