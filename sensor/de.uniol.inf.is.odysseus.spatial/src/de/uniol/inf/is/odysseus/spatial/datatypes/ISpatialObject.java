package de.uniol.inf.is.odysseus.spatial.datatypes;

/**
 * This is the interface for all spatial objects,
 * like points, segments, facets, solids
 * 
 * At the moment, there are no common properties. This
 * is interface is for detecting if spatial
 * operations can be used in tuples, to get
 * higher information.
 * 
 * @author Andre Bolles
 *
 */
public interface ISpatialObject {

	public boolean intersects(ISpatialObject i);
	public boolean intersects(Segment3D s);
	public boolean intersects(Line3D l);
	public boolean intersects(Facet3D f);
	public boolean intersects(Solid3D sd);
	
	
//	public boolean meets(ISpatialObject i);
//	public boolean meets(Segment3D s);
//	public boolean meets(Line3D l);
//	public boolean meets(Facet3D f);
//	public boolean meets(Solid3D sd);
}
