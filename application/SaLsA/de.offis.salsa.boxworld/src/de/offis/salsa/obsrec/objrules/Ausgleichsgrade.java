package de.offis.salsa.obsrec.objrules;

import java.util.List;

import de.offis.salsa.lms.model.Sample;

public class Ausgleichsgrade {
	private List<Sample> segment;

	private double steigung = 0;
	private double absolute = 0;
	
	public Ausgleichsgrade(List<Sample> segment) {
		this.segment = segment;
		doCompute();
	}
	
	private void doCompute(){
		
	}
	
	public double getSteigung() {
		return steigung;
	}
	
	public double getAbsolute(){
		return absolute;
	}
}
