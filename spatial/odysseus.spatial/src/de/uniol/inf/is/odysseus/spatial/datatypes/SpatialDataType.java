package de.uniol.inf.is.odysseus.spatial.datatypes;

public interface SpatialDataType {

	/**
	 * Lines intersect each other. Lines can
	 * intersect a polygon.
	 * @param other
	 * @return
	 */
	public boolean intersects(SpatialDataType other);
	
	/**
	 * A line touches another line or polygon, if one
	 * of the start/end points is on the other line
	 * or the border of the polygon.
	 * @param other
	 * @return
	 */
	public boolean touches(SpatialDataType other);
	
	/**
	 * A polygon can cover another polygon
	 * @param other
	 * @return
	 */
	public boolean covers(SpatialDataType other);
	
	/**
	 * Two lines meet, if their start and end point
	 * is equal.
	 */
	public boolean meets(SpatialDataType other);
	
	/**
	 * A point can be on a line or the border of
	 * a polygon.
	 * 
	 * @param other
	 * @return
	 */
	public boolean on(SpatialDataType other);
	
	/**
	 * A point can be in a polygon. A segement
	 * (finite line) can be in a polygon.
	 * 
	 * @param other
	 * @return
	 */
	public boolean in(SpatialDataType other);
	
	/**
	 * Polygons can intersect each other.
	 * @param other
	 * @return
	 */
	public boolean overlaps(SpatialDataType other);
	
	/**
	 * Two spatial objects are spatially equal
	 * if the have exactly the same position.
	 * @return
	 */
	public boolean spatialEquals();
	
	/**
	 * Two lines can be parallel
	 */
	public boolean parallel(SpatialDataType other);
	
	/**
	 * returns the string representation of the
	 * datatype
	 */
	public boolean getSymbol();
}
