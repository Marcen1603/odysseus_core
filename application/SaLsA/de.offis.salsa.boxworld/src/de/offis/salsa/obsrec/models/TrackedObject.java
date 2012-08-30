package de.offis.salsa.obsrec.models;

import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Polygon;


public class TrackedObject {
	private int x;
	private int y;
	private int width;
	private int height;
	
	private Map<String, Double> affinitys = new HashMap<String, Double>();
	private Map<String, Polygon> polygons = new HashMap<String, Polygon>();
		
	public TrackedObject(int x, int y, int width, int height, 
			Map<String, Double> affinitys, Map<String, Polygon> polygons) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.affinitys = new HashMap<String, Double>(affinitys);
		this.polygons = new HashMap<String, Polygon>(polygons);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

	public Map<String, Double> getTypeAffinitys() {
		return affinitys;
	}
	
	public Map<String, Polygon> getPolygons() {
		return polygons;
	}
}
