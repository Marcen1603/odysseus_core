package de.offis.salsa.obsrec;


public class TrackedObject {
	public enum Type {
		GERADE, RUND, KONKAV, V_FORM, ECKIG;
	}
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private TypeDetails typeDetails;
	
	private PolygonContainer polys;
	
	public TrackedObject(int x, int y, int width, int height, TypeDetails typeDetails, PolygonContainer polys) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.typeDetails = typeDetails;
		this.polys = polys;
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

	public TypeDetails getTypeDetails() {
		return typeDetails;
	}
	
	public PolygonContainer getPolygonContainer() {
		return polys;
	}
}
