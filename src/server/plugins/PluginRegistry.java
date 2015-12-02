package server.plugins;

import java.util.Map;

import server.Factories.IDAOFactory;

public class PluginRegistry {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static PluginRegistry singleton = null;
	
	public PluginRegistry getSingleton() {
		if (singleton == null) {
			singleton = new PluginRegistry();
		}
		return singleton;
	}
	
	private Map<String, PluginMetadata> plugins;
	
	private PluginRegistry() {
		
	}
	
	public PluginMetadata getMetadata(String pluginName) {
		return plugins.get(pluginName);
	}
	
	public IDAOFactory getDAOFactory(String pluginName) {
		// TODO stub
		return null;
	}
	
	public void registerPlugin(String pluginName, String jarFileLocation, String className) {
		PluginMetadata meta = new PluginMetadata(pluginName, jarFileLocation, className);
	}
	
	private void loadRegistryFile() {
		
	}
	
	private void saveRegistryFile() {
		
	}
}
