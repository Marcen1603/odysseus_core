package de.uniol.inf.is.odysseus.spatial.datatypes;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.spatial.exception.SpatialException;

public class Point3D extends AbstractSpatialObject implements IClone, Serializable{
	
	private double[] values;
	private double[][] covMatrix;
	
	/**
	 * Creates a new point with coordinates 0, 0, 0
	 * and covariance 0 for every coordinate
	 */
	public Point3D(){
		this.values = new double[3];
		this.values[0] = 0;
		this.values[1] = 0;
		this.values[2] = 0;
		
		this.covMatrix = new double[3][3];
		for(int i = 0; i<this.covMatrix.length; i++){
			for(int u = 0; u<this.covMatrix[i].length; u++){
				this.covMatrix[i][u] = 0;
			}
		}
	}
	
	/**
	 * Creates a new point with coordinates x, y, z
	 * and covariance 0 for every coordinate
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3D(double x, double y, double z){
		this.values = new double[3];
		this.values[0] = x;
		this.values[1] = y;
		this.values[2] = z;
		
		this.covMatrix = new double[3][3];
		
		for(int i = 0; i<this.covMatrix.length; i++){
			for(int u = 0; u<this.covMatrix[i].length; u++){
				this.covMatrix[i][u] = 0;
			}
		}
	}
	
	/**
	 * Creates a new point with coordinate x, y, z and
	 * covariance matric covMatrix
	 * @param original
	 */
	public Point3D(double x, double y, double z, double[][] covMatrix){
		this.values = new double[3];
		this.values[0] = x;
		this.values[1] = y;
		this.values[2] = z;
		
		this.covMatrix = covMatrix;
	}
	
	private Point3D(Point3D original){
		this.values = new double[original.values.length];
		for(int i = 0; i<original.values.length; i++){
			this.values[i] = original.values[i];
		}
		
		this.covMatrix = new double[3][3];
		for(int i = 0; i<this.covMatrix.length; i++){
			for(int u = 0; u<this.covMatrix[i].length; u++){
				this.covMatrix[i][u] = original.covMatrix[i][u];
			}
		}
	}
	
	public Point3D clone(){
		return new Point3D(this);
	}
	
	/**
	 * Two points are equal, if the distance
	 * between the two points is lower than <code>eps</code>
	 * @param eps the maximal distance between two points to consider them as equal
	 */
	public boolean equals(Point3D p, double eps){
		return this.distance(p) <= eps;
	}
	
	@Deprecated
	public boolean equals(Object obj){
		throw new UnsupportedOperationException();
	}
	
	public double getX(){
		return this.values[0];
	}
	
	public void setX(double x){
		this.values[0] = x;
	}
	
	public double getY(){
		return this.values[1];
	}
	
	public void setY(double y){
		this.values[1] = y;
	}
	
	public double getZ(){
		return this.values[2];
	}
	
	public void setZ(double z){
		this.values[2] = z;
	}
	
	public double distance(Point3D p){
		double a = this.getX() - p.getX();
		double b = this.getY() - p.getY();
		double c = this.getZ() - p.getZ();
		return Math.sqrt((a*a) + (b*b) + (c*c));
	}
	
	public boolean on(Segment3D s){
		return false; //SpatialFraction.on(this, s);
	}
	
	public boolean on(Facet3D f){
		return false; //SpatialFraction.on(this, f);
	}
	
	public boolean in(Segment3D s){
		return false; //SpatialFraction.in(this, s);
	}
	
	public boolean in(Facet3D f){
		return false; //SpatialFraction.in(this, f);
	}
	
	public boolean out(Facet3D f){
		return false; //SpatialFraction.out(this, f);
	}
	
	public boolean coplanar(Facet3D f){
		return false; //SpatialFraction.coplanar(this, f);
	}
	
	public String toString(){
		return null;
//		return "Point: x = " + this.values[0].getNumerator() + "/" +this.values[0].getDenominator() + 
//		"; y = " + this.values[1].getNumerator() + "/" + this.values[1].getDenominator() + 
//		"; z = " + this.values[2].getNumerator() + "/" + this.values[2].getDenominator();
	}


	public boolean intersects(Segment3D s) {
		throw new SpatialException("A point cannot intersect with anything.");
	}


	public boolean intersects(Line3D l) {
		throw new SpatialException("A point cannot intersect with anything.");
	}


	public boolean intersects(Facet3D f) {
		throw new SpatialException("A point cannot intersect with anything.");
	}


	public boolean intersects(Solid3D sd) {
		throw new SpatialException("A point cannot intersect with anything.");
	}

}
