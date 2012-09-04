package de.offis.salsa.obsrec.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;

public class LinearRegression2D {
	private DescriptiveStatistics statsX = new DescriptiveStatistics();
	private DescriptiveStatistics statsY = new DescriptiveStatistics();
	
	private List<Coordinate> coords = new ArrayList<Coordinate>();
	
	private boolean computed = false;
	
	private double m = 0;
	private double b = 0;
	
	public LinearRegression2D(List<Coordinate> coords) {
		for(Coordinate c : coords){
			addPoint(c.x, c.y);
		}
	}
	
	public LinearRegression2D(Coordinate[] coords) {
		for(Coordinate c : coords){
			addPoint(c.x, c.y);
		}
	}
	
	public LinearRegression2D() {
	}
	
	public void addPoint(double x, double y){
		statsX.addValue(x);
		statsY.addValue(y);
		
		coords.add(new Coordinate(x, y));
	}
	
	public void work(){		
		double meanX = statsX.getMean();
		double meanY = statsY.getMean();
		
		double sum1 = 0;
		double sum2 = 0;
		for(Coordinate c : coords){
			sum1 += (c.x - meanX)*(c.y - meanY);
			sum2 += Math.pow(c.x - meanX, 2);
		}
		
		m = sum1/sum2;
		b = meanY - m * meanX;
		
		computed = true;
	}
	
	public double getM(){
		if(!computed){
			work();
		}
		
		return m;
	}
	
	public double getB(){
		if(!computed){
			work();
		}
		
		return b;
	}
	
	public double value(double x){
		return m*x+b;
	}
}
