package de.uniol.inf.is.odysseus.spatial.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.spatial.exception.InvalidFacetException;

/**
 * A facet is simply a polygon. All the segments
 * in a facet are ordered in clockwise order.
 * That means, that the interior of a facet is
 * always to the right of a segment.
 * 
 * At the moment we assume, that a Facet3D object
 * always represents a strictly convex facet.
 * 
 * @author abolles
 *
 */
public class Facet3D extends AbstractSpatialObject implements IClone, Serializable{

	private ArrayList<Segment3D> segments;
	/**
	 * Indicates whether the facet has
	 * been correctly initialized.
	 * All modifying or testing method
	 * will throw InvalidFacetException
	 * if this field is false;
	 */
	private boolean correct;
	
	/**
	 * this point contains the maximum coordinates
	 * of all segments xmax, ymax, zmax.These coordinates
	 * can come from different segments, so it is not
	 * necessarily a point of the facet.
	 */
	private Point3D pMax;
	
	/**
	 * this point contains the minimum coordinates
	 * of all segments xmin, ymin, zmin. These coordinates
	 * can come from different segments, so it is not
	 * necessarily a point of the facet.
	 */
	private Point3D pMin;
	
	
	public Point3D getPMax() {
		return pMax;
	}

	public void setPMax(Point3D max) {
		pMax = max;
	}

	public Point3D getPMin() {
		return pMin;
	}

	public void setPMin(Point3D min) {
		pMin = min;
	}

	public Facet3D(){
		this.segments = new ArrayList<Segment3D>();
		this.correct = false;
	}
	
	private Facet3D(Facet3D original){
		this.segments = new ArrayList<Segment3D>();
		
		for(Segment3D s: original.segments){
			this.segments.add(s.clone());
		}
		this.normalize();
		this.createBoundingBox();
		
		this.correct = original.correct;
	}
	
	/**
	 * generates a new facet from the overgiven points.
	 * The points will be connected by segments of
	 * the form pi - pi+1, pi+1 - pi+2, ....
	 * @param points
	 * @param check, If true, the correct state of the facet will be checked
	 */
	
	public Facet3D(boolean check, Point3D... points){
		this.segments = new ArrayList<Segment3D>();
		for(int i = 0; i<points.length; i++){
			this.segments.add(new Segment3D(points[i], points[(i+1)%points.length]));
		}
		
		this.normalize();
		if(check){
			this.checkState();
		}
		// if no check is required,
		// we assume that the overgiven
		// array of points is correct
		else{
			this.correct = true;
		}
		this.createBoundingBox();
	}
	
	public Facet3D(ArrayList<Segment3D> segments, boolean check){
		this.segments = segments;
		this.normalize();
		if(check){
			this.checkState();
		}
		// falls kein check gefordert wird, dann wird
		// davon ausgegangen, dass das �bergebene Array
		// einen g�ltigen Zustand f�r ein Facet3D objekt
		// enth�lt.
		else{
			this.correct = true;
		}
		
		this.createBoundingBox();
	}
	
	/**
	 * This method combines to collinear meeting
	 * segments to one segment.
	 * 
	 * That means e.g.
	 * 
	 * (0,0,0);(1,1,0)
	 * and
	 * (1,1,0);(2,2,0)
	 * will be combined to
	 * (0,0,0);(2,2,0)
	 */
	public void normalize(){
		for(int i = 0; i<this.segments.size(); i++){
			Segment3D s1 = this.segments.get(i);
			Segment3D s2 = this.segments.get((i+1)%this.segments.size());
			if(s1.collinear(s2) && s1.meets(s2)){
				Segment3D comb = s1.combine(s2);
				this.segments.remove(i);
				// remember: the element at i has been
				// removed, so the element at i is 
				// now the next element.
				if(i == this.segments.size()){
					this.segments.remove(0);
					this.segments.add(comb);
				}
				else{
					this.segments.remove(i);
					this.segments.add(i, comb);
				}
				
			}
		}
	}
	
	public void createBoundingBox(){
//		Point3D pmax = this.pMax == null ? new Point3D() : this.pMax;
//		Point3D pmin = this.pMin == null ? new Point3D() : this.pMin; 
//		for(Segment3D s : this.segments){
//			// we only need to check the starting point,
//			// because the end point of the last segment
//			// must be the starting point of the first segment.
//			if(s.getStart().getX().compareTo(pmax.getX()) > 0){
//				pmax.setX(s.getStart().getX());
//			}
//			else if(s.getStart().getX().compareTo(pmin.getX()) < 0 ){
//				pmin.setX(s.getStart().getX());
//			}
//		}
//		
//		this.pMax = pmax;
//		this.pMin = pmin;
	}
	
	public Facet3D clone(){
		return new Facet3D(this);
	}
	
	/**
	 * This method checks whether a Facet3D object
	 * is in a valid state or not. A Faced3D object
	 * is in a valid state if and only if
	 * 1. it has more than 2 segments
	 * 2. all segments meet to a cycle
	 * 3. all the segments are coplanar
	 * 
	 * @return True, if the conditions above are true, false otherwise
	 */
	public boolean checkState(){
		// The segments are ordered in cyclic order.
		// That means, that the segment at position i
		// meets the segment at position (i + 1) % m
		// if m is the number of segments in the facet
		
		// 1. more than 2 segments
		if(!this.checkSize()){
			this.correct = false;
			return this.correct;
		}
		
		// 2. meet to a cycle
		if(!this.checkCycle()){
			this.correct = false;
			return this.correct;
		}
		
		if(!this.checkCoplanar()){
			this.correct = false;
			return this.correct;
		}
		
		this.correct = true;
		return this.correct;
		
	}
	
	/**
	 * Check if this Facet3D objects contains at least
	 * 3 segments
	 * @return true, if this.segments.size() >= 3, false otherwise
	 */
	private boolean checkSize(){
		return this.segments.size() >= 3;
	}
	
	/**
	 * Checks whether the segments are ordered to a cycle.
	 * For this the segments must be ordered, so that
	 * segment i meets segment (i+1) / m, with m number of
	 * segments in this Facet3D object.
	 * 
	 * @return True, if the segments are correctly ordered and
	 * meet to a cycle, false otherwise.
	 */
	private boolean checkCycle(){
		for(int i = 0; i<this.segments.size(); i++){
			if(!this.segments.get(i).meets(this.segments.get((i+1)%this.segments.size()))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks whether all segments are in one plane. For
	 * this every segments si hat to be compared to every
	 * segment sj, i,j \in {0, ... m}, m = size(). So (m - 1)!
	 * comparances have to be done.
	 *  
	 * @return True, if all segments are in the same plane, false otherwise.
	 */
	private boolean checkCoplanar(){
		for(int i = 0; i<this.segments.size() - 1; i++){
			for(int u = i+1; u<this.segments.size(); u++){
				if(!this.segments.get(i).coplanar(this.segments.get(u))){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks whether the passed segment is coplanar to all segments
	 * that are already inserted.
	 * @param s The segment to be checked against the existing segments
	 * @return True, if the s is coplanar to all segments already inserted,
	 * false otherwise
	 */
	public boolean coplanar(Segment3D s){
		return false; // SpatialFraction.coplanar(s, this);
	}
	
	public boolean coplanar(Facet3D f){
		return false; //SpatialFraction.coplanar(f, this);
	}
	
	
	/**
	 * 
	 * @return True, if the last modification of the Facet3D object
	 * has lead to a valid state, false otherwise.
	 */
	public boolean isValid(){
		return this.correct;
	}
	
	/**
	 * Adds a segment to the end of the list. This means
	 * that the segment should meet the last segment
	 * that is already in the list.
	 * 
	 * @param s The segment to be added
	 * @param check If true, a check for a valid state will be done. If no check
	 * has to be done, the return value for isValid() will not change.
	 * @return True, if a check has been done and the facet is valid, or
	 * if no check has been done, but the facet was valid before, false otherwise
	 */
	public boolean addSegment(Segment3D s, boolean check){
		// checkCoplanar has to be called before inserting the new segment
		boolean checkCoplanar = false;
		if(check){
			checkCoplanar = this.coplanar(s);
		}
		this.segments.add(s);
		if(check){
			this.correct = this.checkSize() && this.checkCycle() && checkCoplanar;
		}
		return correct;
	}
	
	/**
	 * This method inserts a segment a the right position in the list.
	 * So it inserts the segment to the position i, where exists
	 * a position j = (i-1)%size(), so that the segment a j has
	 * an end point that is equal to an end point of s and the
	 * same for the next position. It may occur that there is only one
	 * such position because there are not enough elements in the
	 * list. In this case the segment s will be added to that position.
	 * If there is no element in the list, the segment s will be
	 * added to list. If there are elements in the list, but the segment
	 * s finds no partner an InvalidFacetException will be thrown. This
	 * must be done, because otherwise it cannot be guaranteed, that
	 * the Facet3D object will reach a valid state.
	 * 
	 * @param s The segment to be added
	 * @param check If true, a check for a valid state will be done. If no check
	 * has to be done, the return value for isValid() will not change.
	 * @return True, if a check has been done and the facet is valid, or
	 * if no check has been done, but the facet was valid before, false otherwise
	 * 
	 */
	public boolean insertSegment(Segment3D s, boolean check) throws InvalidFacetException{
		// the coplanar check has to be done before inserting
		// the new segment
		boolean checkCoplanar = false;
		if(check){
			checkCoplanar = this.coplanar(s);
		}
		if(this.segments.size() == 0){
			this.segments.add(s);
		}
		else{
			// find a position where exists at least one neighbour that meets
			boolean inserted = false;
			for(int i = 1; i<this.segments.size(); i++){
				if(this.segments.get(i-1).meets(s) || this.segments.get((i+1)%this.segments.size()).meets(s)){
					this.segments.add(i, s);
					inserted = true;
					break;
				}
			}
			
			if(!inserted){
				throw new InvalidFacetException("No meeting partner found for segment.");
			}
		}
		if(check){
			this.correct = this.checkSize() && this.checkCycle() && checkCoplanar;
		}
		return correct;
	}
	
	/**
	 * Returns the segment at the specified index. If no such
	 * segment exists an ArrayIndexOutBoundsException will be thrown.
	 * 
	 * 
	 * @param index The position from which to take the segment
	 * @param check If true, a segment will only be returned, if this
	 * object is a valid facet.
	 * @return The segment at the position index
	 */
	public Segment3D getSegment(int index, boolean check){
		if(check && !this.correct){
			throw new InvalidFacetException("Die Facet ist ungueltig.");
		}
		return this.segments.get(index);
	}
	
	public ArrayList<Segment3D> getSegments(){
		return this.segments;
	}
	
	public boolean in(Facet3D f2){
		for(int i = 0; i<this.getSegments().size(); i++){
			Segment3D s1 = this.getSegment(i, false);
			if(!s1.getStart().in(f2)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * see Spatial.overlaps(Facet3D, Facet3D)
	 * @return Spatial.overlaps(this, f)
	 */
	public boolean overlaps(Facet3D f){
		return false; //SpatialFraction.overlaps(this, f);
	}
	
	/**
	 * Generates a string representation of this facet.
	 * That means, that all the points string representation
	 * will be added to the string in the ordering
	 * of the facet.
	 */
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(Segment3D s : this.segments){
			buffer.append(s.getStart().toString() + " | ");
		}
		return buffer.toString();
	}

	public boolean intersects(Line3D l) {
		return false; //SpatialFraction.intersect(l, this);
	}

	public boolean intersects(Facet3D f) {
		throw new RuntimeException("Not implemented.");
	}

	public boolean intersects(Solid3D sd) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean intersects(
			Segment3D s) {
		// TODO Auto-generated method stub
		return false;
	}
}
