package shared.definitions;

public enum MunicipalityType {
	SETTLEMENT, CITY;
	
	private int pointValue;
	
	static {
		SETTLEMENT.pointValue = 1;
		CITY.pointValue = 2;
	}
	
	public int getPointValue() {
		return pointValue;
	}

}
