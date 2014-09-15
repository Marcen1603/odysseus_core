package de.uniol.inf.is.odysseus.sports.rest.dto;

import java.io.Serializable;

/**
 * This class represents a data transfer object for the gamefield
 * @author Thomas
 *
 */
public class FieldInfo implements Serializable{

	private static final long serialVersionUID = 8342555855464609365L;
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	
	public FieldInfo(double xmin, double xmax, double ymin, double ymax) {
		
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		
	}

	public double getXmin() {
		return xmin;
	}

	public void setXmin(double xmin) {
		this.xmin = xmin;
	}

	public double getXmax() {
		return xmax;
	}

	public void setXmax(double xmax) {
		this.xmax = xmax;
	}

	public double getYmin() {
		return ymin;
	}

	public void setYmin(double ymin) {
		this.ymin = ymin;
	}

	public double getYmax() {
		return ymax;
	}

	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	
	
}
