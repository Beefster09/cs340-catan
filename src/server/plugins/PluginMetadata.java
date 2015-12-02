package server.plugins;

import java.io.Serializable;
import java.net.URL;

public class PluginMetadata
	implements Serializable {
	private static final long serialVersionUID = 100000L;
	
	private String name;
	private String jarFileLocation;
	private String className;
	
	// Dependencies?
	
	/**
	 * @param name the name to associate with the plugin
	 * @param jarFileLocation the location of the jar file relative to the plugins folder
	 * @param className the class to load as the plugin
	 */
	PluginMetadata(String name, String jarFileLocation, String className) {
		super();
		this.name = name;
		this.jarFileLocation = jarFileLocation;
		this.className = className;
	}

	/**
	 * @return the name
	 */
	String getName() {
		return name;
	}
	
	/**
	 * @return the jarFileLocation
	 */
	String getJarFileLocation() {
		return jarFileLocation;
	}
	
	URL getJarURL() {
		return null;
	}
	
	/**
	 * @return the className
	 */
	String getClassName() {
		return className;
	}
	
}
