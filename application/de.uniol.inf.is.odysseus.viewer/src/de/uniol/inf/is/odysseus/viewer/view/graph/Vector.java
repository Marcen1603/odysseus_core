package de.uniol.inf.is.odysseus.viewer.view.graph;


public class Vector {

	private int x;
	private int y;
	
	public static final Vector EMPTY_VECTOR = new Vector(0,0);
	
	public Vector( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Vector clone() {
		return new Vector(x,y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Vector add( Vector v ) {
		if( v == null )
			return clone();
		
		return new Vector( v.x + x, v.y + y );
	}
	
	public Vector add( int x, int y ) {
		return new Vector( this.x + x, this.y + y );
	}
	
	public Vector sub( Vector v ) {
		if( v == null )
			return clone();
		
		return new Vector( x - v.x, y - v.y );
	}
	
	public Vector sub( int x, int y ) {
		return new Vector( this.x - x, this.y - y );
	}
	
	public Vector mul( int factor ) {
		return new Vector( x * factor, y * factor );
	}
	
	public Vector mul( double factor ) {
		return new Vector( (int)(x * factor), (int)(y * factor));
	}
	
	public int mul( Vector v ) {
		return v.x * x + v.y * y;
	}
	
	public Vector div( int factor ) {
		return new Vector( x / factor, y / factor );
	}
	
	public Vector div( double factor ) {
		return new Vector( (int)(x / factor), (int)(y / factor));
	}
}
