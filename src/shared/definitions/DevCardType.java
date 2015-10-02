package shared.definitions;

public enum DevCardType
{
	SOLDIER, YEAR_OF_PLENTY, MONOPOLY, ROAD_BUILD, MONUMENT;
	
	private String stringRepr;
	
	static {
		SOLDIER.stringRepr = "soldier";
		YEAR_OF_PLENTY.stringRepr = "yearOfPlenty";
		MONOPOLY.stringRepr = "monopoly";
		ROAD_BUILD.stringRepr = "roadBuilding";
		MONUMENT.stringRepr = "monument";
	}
	
	public String toString() {
		return stringRepr;
	}
}

