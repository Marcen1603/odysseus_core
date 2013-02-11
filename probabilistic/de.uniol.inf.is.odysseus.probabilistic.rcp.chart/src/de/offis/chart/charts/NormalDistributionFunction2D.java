package de.offis.chart.charts;


import org.jfree.data.function.Function2D;

public class NormalDistributionFunction2D implements Function2D {

	private double sigma;
	private double mean;
	
	public NormalDistributionFunction2D(double sigma, double mean) {
		this.sigma = sigma;
		this.mean = mean;

	}
	
	@Override
	public double getValue(double x) {
		double exp = -(Math.pow((x-mean),2)/(2*Math.pow(sigma,2)));		
		return (1/(sigma*Math.sqrt(2*Math.PI)))*Math.pow(Math.E, exp);
	}
}
