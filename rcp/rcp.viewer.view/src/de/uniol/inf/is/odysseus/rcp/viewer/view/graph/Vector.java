package de.uniol.inf.is.odysseus.rcp.viewer.view.graph;


public class Vector {

	private double x;
	private double y;
	
	public static final Vector EMPTY_VECTOR = new Vector(0,0);
	
	public Vector( double x, double y ) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Vector clone() {
		return new Vector(x,y);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Vector add( Vector v ) {
		return new Vector( v.x + x, v.y + y );
	}
	
	public Vector add( double x, double y ) {
		return new Vector( this.x + x, this.y + y );
	}
	
	public Vector sub( Vector v ) {
		return new Vector( x - v.x, y - v.y );
	}
	
	public Vector sub( double x, double y ) {
		return new Vector( this.x - x, this.y - y );
	}
	
	public Vector mul( int factor ) {
		return new Vector( x * factor, y * factor );
	}
	
	public Vector mul( double factor ) {
		return new Vector( x * factor, y * factor);
	}
	
	public double mul( Vector v ) {
		return v.x * x + v.y * y;
	}
	
	public Vector div( int factor ) {
		return new Vector( x / factor, y / factor );
	}
	
	public Vector div( double factor ) {
		return new Vector(x / factor, y / factor);
	}
	
	public float getLength() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(x).append(", ").append(y).append("}");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof Vector)) return false;
		if( obj == this ) return true;
		Vector v = (Vector)obj;
		return v.x == x && v.y == y;
	}
}
