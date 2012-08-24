package de.offis.salsa.obsrec;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.objrules.AbstractObjRule;

public class TrackedObject {
	public enum Type {
		GERADE, RUND, KONKAV, V_FORM, ECKIG;
	}
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private TypeDetails typeDetails;
	
	private Polygon poly;
	
	public TrackedObject(int x, int y, int width, int height, Type type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.typeDetails = new TypeDetails(type, 1.0);
	}
	
	public TrackedObject(List<Sample> samplesObject) {
		Polygon p = new Polygon();
		
		for(Sample s : samplesObject){
			p.addPoint((int)s.getX(), (int)s.getY());
		}
		
		Rectangle b = p.getBounds();
		
		this.x = b.x;
		this.y = b.y;
		this.width = b.width;
		this.height = b.height;
		this.typeDetails = computeType(samplesObject);
		this.poly = computePoly(samplesObject);
	}

//	private TrackedObject generateBBox(List<Sample> samplesObject){
//		Polygon p = new Polygon();
//		
//		for(Sample s : samplesObject){
//			p.addPoint((int)s.getX(), (int)s.getY());
//		}
//		
//		Rectangle b = p.getBounds();
//		
//		return new TrackedObject(b.x, b.y, b.width, b.height, computeType(samplesObject));
//	}
	
	private Polygon computePoly(List<Sample> samplesObject) {
		return AbstractObjRule.getPredictedPolygon(samplesObject, typeDetails.getMaxAffinityType());
	}

	private TypeDetails computeType(List<Sample> samplesObject){
		return AbstractObjRule.getTypeDetails(samplesObject);
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
	
	public Polygon getPoly() {
		return poly;
	}
}
