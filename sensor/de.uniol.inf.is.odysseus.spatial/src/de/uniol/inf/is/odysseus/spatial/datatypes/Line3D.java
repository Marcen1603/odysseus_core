package de.uniol.inf.is.odysseus.spatial.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.spatial.exception.InvalidLineException;
import de.uniol.inf.is.odysseus.spatial.exception.NoIntersectionException;

/**
 * A line object can be used for modelling a linear
 * approximated polyline/curve. It consists of
 * a number of meeting segments.
 * 
 * @author abolles
 *
 */
public class Line3D extends AbstractSpatialObject implements IClone, Serializable{

	private ArrayList<Segment3D> segments;
	
	public ArrayList<Segment3D> getSegments() {
		return segments;
	}

	public void setSegments(ArrayList<Segment3D> segments) {
		this.segments = segments;
	}

	public boolean isCorrect() {
		return correct;
	}

	private boolean correct;
	
	public Line3D(boolean check, Segment3D... segs){
		this.segments = new ArrayList<Segment3D>();
		for(int i = 0; i<segs.length; i++){
			this.segments.add(segs[i]);
		}
		
		if(check){
			this.checkState();
		}
		// if no check is required,
		// we assume, that the user
		// knows what he is doing,
		// this line will be correct.
		else{
			this.correct = true;
		}
	}
	
	public Line3D(Point3D... pts){
		this.segments = new ArrayList<Segment3D>();
		
		if(pts.length < 2){
			throw new InvalidLineException("A line must have at least 2 points.");
		}
		for(int i = 0; i<pts.length-1; i++){
			this.segments.add(new Segment3D(pts[i], pts[i+1]));
		}
		
	}
	
	/**
	 * Generates a copy of the passed object.
	 * All contained segments will also be copied.
	 */
	private Line3D(Line3D original){
		this.segments = new ArrayList<Segment3D>();
		for(Segment3D s: original.segments){
			this.segments.add(s.clone());
		}
		this.correct = original.correct;
	}
	
	public Line3D clone(){
		return new Line3D(this);
	}
	
	/**
	 * Checks, whether the first segment
	 * meets the second one and
	 * the second meets the third..
	 * the last
	 * @return
	 */
	public boolean checkState(){
		for(int i = 0; i<this.segments.size() - 1; i++){
			if(!this.segments.get(i).getEnd().equals(this.segments.get(i+1).getStart())){
				this.correct = false;
				return false;
			}
		}
		this.correct = true;
		return true;
	}
	
	/**
	 * A line intersects with another line,
	 * if at least one segment of the one line,
	 * intersects with at least one segment
	 * of the other line
	 * @param l
	 * @return
	 */
	public boolean intersects(Line3D l){
		for(Segment3D s1: this.segments)	{
			for(Segment3D s2: l.segments){
				if(s1.intersects(s2)){
					s1.intersects(s2);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the points of intersection. Two
	 * lines can intersect more than once.
	 */
	public ArrayList<Point3D> intersection(Line3D l){
		ArrayList<Point3D> points = new ArrayList<Point3D>();
		for(Segment3D s1: this.segments){
			for(Segment3D s2: l.segments){
				try{
					Point3D p = s1.intersection(s2);
					points.add(p);
				}catch(NoIntersectionException e){
					// there is no intersection
					// so ignore the exception
				}
			}
		}
		return points;
	}
	
	/**
	 * Two lines overlap if at least one their
	 * segments overlap
	 */
	public boolean overlap(Line3D l){
		for(Segment3D s1: this.segments){
			for(Segment3D s2: l.segments){
				if(s1.overlaps(s2)){
					return true;
				}
			}
		}	
		return false;
	}
	
	/**
	 * Two lines touch if at least one of their
	 * segments touch
	 */
	public boolean touch(Line3D l){
		for(Segment3D s1: this.segments){
			for(Segment3D s2: l.segments){
				if(s1.touches(s2)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Two lines meet if the end point
	 * of the last segment of one line
	 * meets with the start point of the first
	 * segment of the other line.
	 */
	public boolean meet(Line3D l){
		if(this.segments.get(0).getStart().equals(l.getSegments().get(l.segments.size()-1).getEnd()) ||
				l.segments.get(0).getStart().equals(this.segments.get(this.segments.size()-1).getEnd())){
			return true;
		}
		return false;
	}

	/**
	 * A line intersects with a segment
	 * if at least one of the line's
	 * segments intersects with the segment
	 */
	public boolean intersects(Segment3D s) {
		return this.intersects(new Line3D(false, s));
	}

	/**
	 * A line intersects with a segment
	 * if at least one of the line's
	 * segments intersects with the facet
	 */
	public boolean intersects(Facet3D f) {
		for(Segment3D s: this.segments){
			if(s.intersects(f)){
				return true;
			}
		}
		return false;
	}

	/**
	 * A line intersects with a solid
	 * if their exist at least 2 intersection
	 * points between a segment of the line
	 * and a facet of the solid
	 * 
	 * TODO Schnitt mit Segmenten betrachten
	 */
	public boolean intersects(Solid3D sd) {
		for(Segment3D s: this.segments){
			for(Facet3D f: sd.getFacets()){
				if(s.intersects(f)){
					return true;
				}
			}
		}
		return false;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(Segment3D s : this.segments){
			buffer.append(s.getStart().toString() + "; ");
		}
		
		buffer.append(this.getSegments().get(this.segments.size() - 1).getEnd().toString());
		return buffer.toString();
	}
}
