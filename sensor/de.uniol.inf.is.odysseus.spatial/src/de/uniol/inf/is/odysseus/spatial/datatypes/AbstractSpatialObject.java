package de.uniol.inf.is.odysseus.spatial.datatypes;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.spatial.exception.SpatialException;

/**
 * This class provides an implementation for the general methods
 * for intersects, meets, touch ...
 * 
 * @author Andre Bolles
 *
 */
public abstract class AbstractSpatialObject implements ISpatialObject, Serializable{

	public boolean intersects(ISpatialObject i){
		if(i instanceof Segment3D){
			return this.intersects((Segment3D)i);
		}
		else if(i instanceof Line3D){
			return this.intersects((Line3D)i);
		}
		else if(i instanceof Facet3D){
			return this.intersects((Facet3D)i);
		}
		else if(i instanceof Solid3D){
			return this.intersects((Solid3D)i);
		}
		throw new SpatialException("Cannot check intersection types " + this.getClass() + " and " + i.getClass());
	}
}
