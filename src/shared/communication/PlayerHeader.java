package shared.communication;

import shared.definitions.CatanColor;

public class PlayerHeader {
	private CatanColor color;
	private String name;
	private int id;
	
	public PlayerHeader(CatanColor color, String name, int id){
		this.color = color;
		this.name = name;
		this.id = id;
	}
	/**
	 * @return the color
	 */
	public CatanColor getColor() {
		return color;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
