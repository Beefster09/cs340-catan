package server.interpreter;

public class CommandInfo {

	private String[] arguments;
	private String shortDescription;
	private String longDescription;

	/**
	 * @param arguments
	 * @param shortDescription
	 * @param longDescription
	 */
	public CommandInfo(String[] arguments, String shortDescription,
			String longDescription) {
		this.arguments = arguments;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	public CommandInfo(String shortDescription, String longDescription) {
		this(new String[0], shortDescription, longDescription);
	}

	/**
	 * @param arguments
	 * @param description
	 */
	public CommandInfo(String[] arguments, String description) {
		this(arguments, description, description);
	}

	/**
	 * @param description
	 */
	public CommandInfo(String description) {
		this(new String[0], description, description);
	}

	/**
	 * @return the arguments
	 */
	public String[] getArguments() {
		return arguments;
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}

}
