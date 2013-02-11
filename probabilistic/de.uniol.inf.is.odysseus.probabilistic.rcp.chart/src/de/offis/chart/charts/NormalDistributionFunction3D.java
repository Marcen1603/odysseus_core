package de.offis.chart.charts;


import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;

public class NormalDistributionFunction3D {
	
	private double sigmax, sigmay, centerx, centery;
	
	public NormalDistributionFunction3D(double sigmax, double sigmay, double centerx, double centery) {
		this.sigmax = sigmax;
		this.sigmay = sigmay;
		this.centerx = centerx;
		this.centery = centery;		
	}
	
	public NormalDistributionFunction3D(CovarianceMatrix matrix) {
		// TODO Auto-generated constructor stub
	}
	
	public double getValue(double x, double y){
		float z = (float) (1*Math.exp(
				-(
						(Math.pow(x-centerx, 2))/(2*Math.pow(sigmax, 2))
						+
						(Math.pow(y-centery, 2))/(2*Math.pow(sigmay, 2))
						)));
		
		
		
		
		return z;
	}
}
