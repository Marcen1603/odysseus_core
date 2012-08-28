package de.offis.salsa.obsrec;

import java.awt.Polygon;
import java.util.HashMap;
import java.util.Iterator;

import de.offis.salsa.obsrec.TrackedObject.Type;

public class PolygonContainer implements Iterable<Polygon> {
	private HashMap<Type, Polygon> polygons = new HashMap<TrackedObject.Type, Polygon>();
	
	public void addPolygon(Type type, Polygon poly){
		this.polygons.put(type, poly);
	}
	
	public Polygon getPolygon(Type type){
		return this.polygons.get(type);
	}

	@Override
	public Iterator<Polygon> iterator() {		
		return polygons.values().iterator();
	}
}
