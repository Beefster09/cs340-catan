package shared.locations;

import org.json.simple.JSONObject;

import shared.exceptions.SchemaMismatchException;

/**
 * Represents the location of a vertex on a hex map
 */
public class VertexLocation
{
	
	private HexLocation hexLoc;
	private VertexDirection dir;
	
	public VertexLocation(JSONObject json) throws SchemaMismatchException {
		hexLoc = new HexLocation(json);
		if (json.containsKey("direction"))
			dir = VertexDirection.getDirectionFromString((String) json.get("direction"));
		//else throw schema exception.
	}
	
	public VertexLocation(HexLocation hexLoc, VertexDirection dir)
	{
		setHexLoc(hexLoc);
		setDir(dir);
	}
	
	public HexLocation getHexLoc()
	{
		return hexLoc;
	}
	
	private void setHexLoc(HexLocation hexLoc)
	{
		if(hexLoc == null)
		{
			throw new IllegalArgumentException("hexLoc cannot be null");
		}
		this.hexLoc = hexLoc;
	}
	
	public VertexDirection getDir()
	{
		return dir;
	}
	
	private void setDir(VertexDirection direction)
	{
		this.dir = direction;
	}
	
	@Override
	public String toString()
	{
		return "VertexLocation [hexLoc=" + hexLoc + ", dir=" + dir + "]";
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		VertexLocation self = getNormalizedLocation();
		int result = 1;
		result = prime * result + ((self.dir == null) ? 0 : self.dir.hashCode());
		result = prime * result + ((self.hexLoc == null) ? 0 : self.hexLoc.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		VertexLocation other = ((VertexLocation) obj).getNormalizedLocation();
		VertexLocation self = getNormalizedLocation();
		if(self.dir != other.dir)
			return false;
		if(self.hexLoc == null)
		{
			if(other.hexLoc != null)
				return false;
		}
		else if(!self.hexLoc.equals(other.hexLoc))
			return false;
		return true;
	}
	
	/**
	 * Returns a canonical (i.e., unique) value for this vertex location. Since
	 * each vertex has three different locations on a map, this method converts
	 * a vertex location to a single canonical form. This is useful for using
	 * vertex locations as map keys.
	 * 
	 * @return Normalized vertex location
	 */
	public VertexLocation getNormalizedLocation()
	{
		
		// Return location that has direction NW or NE
		
		switch (dir)
		{
			case NorthWest:
			case NorthEast:
				return this;
			case West:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.SouthWest),
										  VertexDirection.NorthEast);
			case SouthWest:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.South),
										  VertexDirection.NorthWest);
			case SouthEast:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.South),
										  VertexDirection.NorthEast);
			case East:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.SouthEast),
										  VertexDirection.NorthWest);
			default:
				assert false;
				return null;
		}
	}
}

