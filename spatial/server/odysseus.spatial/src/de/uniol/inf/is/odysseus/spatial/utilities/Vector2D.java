/**
 * 
 */
package de.uniol.inf.is.odysseus.spatial.utilities;

/**
 * @author msalous
 *
 */
public class Vector2D {
	
	/** X Dimension*/
	private double x;
	
	/** Y Dimension*/
	private double y;
	
	/** Constructs a Vector2D from given coordinates*/
	public Vector2D(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/** Length*/
	public double len () {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	/** Dot*/
	public double dot(Vector2D vector){
		return this.x * vector.x + this.y * vector.y;
	}
	
	/** Subtract*/
	public Vector2D subtract (Vector2D vector) {
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}
	
	/** Add*/
	public Vector2D add (Vector2D vector) {
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}
	
	/** Distance between two points*/
	public static double distance (double x1, double y1, double x2, double y2) {
		final double xDif = x2 - x1;
		final double yDif = y2 - y1;
		return Math.sqrt(xDif * xDif + yDif * yDif);
	}
	
	/** Distance between this vector and another one*/
	public double distance (Vector2D vector) {
		final double xDif = this.x - vector.x;
		final double yDif = this.y - vector.y;
		return Math.sqrt(xDif * xDif + yDif * yDif);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
