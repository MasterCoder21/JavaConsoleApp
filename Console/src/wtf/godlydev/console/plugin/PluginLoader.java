package wtf.godlydev.console.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import wtf.godlydev.console.plugin.api.Plugin;
import wtf.godlydev.console.plugin.api.PluginDescriptionFile;
import wtf.godlydev.console.plugin.api.PluginException;

public class PluginLoader {

	private static final Gson GSON = new GsonBuilder().serializeNulls().create();

	private HashMap<File, Plugin> map = new HashMap<File, Plugin>();

	public Plugin load(File file) {
		try {
			if(map.containsKey(file)) {
				throw new PluginException("Plugin already loaded.");
			}

			PluginDescriptionFile pluginDescriptionFile = getPluginDescriptionFile(file);
			if(pluginDescriptionFile == null) {
				return null;
			}

			ClassLoader loader = URLClassLoader.newInstance( new URL[] { file.toURI().toURL() }, getClass().getClassLoader() );
			
			Class<?> clazz = Class.forName(pluginDescriptionFile.getMain(), true, loader);
			
			Class<? extends Plugin> instanceClass = clazz.asSubclass(Plugin.class);

			Constructor<? extends Plugin> instanceClassConstructor = (Constructor<? extends Plugin>) instanceClass.getConstructor();
			
			Plugin plugin = instanceClassConstructor.newInstance();
			
			plugin.setDescriptionFile(pluginDescriptionFile);

			map.put(file, plugin);
			
			// System.out.println("Loaded '" + plugin.getDescriptionFile().getName() + " v" + plugin.getDescriptionFile().getVersion() + "'");

			plugin.onEnable();
			
			return plugin;
		}
		catch(MalformedURLException e) {
			// throw new PluginException("Failed to convert the file path to a URL.", e);
			return null;
		}
		catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// throw new PluginException("Failed to create a new instance of the plugin.", e);
			return null;
		}
	}

	public void unload(File file) {

		if(!map.containsKey(file)) {
			throw new PluginException("Can't unload a Plugin that wasn't loaded in the first place.");
		}

		Plugin plugin = map.get(file);
		
		plugin.onDisable();
		
		map.remove(file);
		
		// System.out.println("Unloaded '" + plugin.getDescriptionFile().getName() + " v" + plugin.getDescriptionFile().getVersion() + "'");
	}

	public void reload(File file) {
		unload(file);
		load(file);
	}

	private PluginDescriptionFile getPluginDescriptionFile(File file) {

		try {
			
			ZipFile zipFile = new ZipFile(file);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			PluginDescriptionFile pluginJson = null;

			while(entries.hasMoreElements() && pluginJson == null){
				ZipEntry entry = entries.nextElement();

				if(!entry.isDirectory() && entry.getName().equals("plugin.json")) {
					InputStream stream = zipFile.getInputStream(entry);
					try {
						pluginJson = GSON.fromJson(new InputStreamReader(stream), PluginDescriptionFile.class);
					}
					catch(JsonParseException jsonParseException) {
						// throw new PluginException("Failed to parse JSON:", jsonParseException);
					}
				}
			}

			if(pluginJson == null) {
				zipFile.close();
				// throw new PluginException("Failed to find plugin.json in the root of the jar.");
			}

			zipFile.close();
			
			return pluginJson;
		}
		catch(IOException e) {
			// throw new PluginException("Failed to open the jar as a zip:", e);
			return null;
		}
	}
	
	public Collection<Plugin> getLoadedPlugins() {
		return this.map.values();
	}
	
	public HashMap<File, Plugin> getPluginMap() {
		return map;
	}

}
