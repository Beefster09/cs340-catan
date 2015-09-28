package shared.locations;

public enum VertexDirection
{
	West, NorthWest, NorthEast, East, SouthEast, SouthWest;
	
	private VertexDirection opposite;
	
	static
	{
		West.opposite = East;
		NorthWest.opposite = SouthEast;
		NorthEast.opposite = SouthWest;
		East.opposite = West;
		SouthEast.opposite = NorthWest;
		SouthWest.opposite = NorthEast;
	}
	
	public VertexDirection getOppositeDirection()
	{
		return opposite;
	}
	
	public static VertexDirection getDirectionFromString(String input) {
		String lowerInput = input.toLowerCase();
		if      (lowerInput.equals("nw") || lowerInput.equals("northwest")) return NorthWest;
		else if (lowerInput.equals("w")  || lowerInput.equals("north"))     return West;
		else if (lowerInput.equals("ne") || lowerInput.equals("northeast")) return NorthEast;
		else if (lowerInput.equals("se") || lowerInput.equals("southwest")) return SouthEast;
		else if (lowerInput.equals("e")  || lowerInput.equals("south"))     return East;
		else if (lowerInput.equals("sw") || lowerInput.equals("southeast")) return SouthEast;
		else throw new IllegalArgumentException();
	}
}

