package wtf.godlydev.console.plugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;

import wtf.godlydev.console.plugin.api.Plugin;

public abstract class PluginLoader {

	public abstract Plugin load(File file);

	public abstract void unload(File file);

	public abstract void reload(File file);
	
	public abstract Collection<Plugin> getLoadedPlugins();
	
	public abstract HashMap<File, Plugin> getPluginMap();

}
