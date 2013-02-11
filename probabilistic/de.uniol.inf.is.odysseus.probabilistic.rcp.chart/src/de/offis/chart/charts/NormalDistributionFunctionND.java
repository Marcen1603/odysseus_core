package de.offis.chart.charts;


import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;

public class NormalDistributionFunctionND {
	
	private CovarianceMatrix matrix;
	private double[] means;
	
	public NormalDistributionFunctionND(double[] means, CovarianceMatrix matrix) {
		this.matrix = matrix;
		this.means = means;	
	}
	
	public double getValue(double[] x){
		int k = x.length;
		RealMatrix z = matrix.getMatrix();
		double z_det = new LUDecomposition(z).getDeterminant();
		RealMatrix x_col = MatrixUtils.createColumnRealMatrix(x);
		RealMatrix means_col = MatrixUtils.createColumnRealMatrix(means);
		RealMatrix x_sub_m = x_col.subtract(means_col);
		RealMatrix z_inverse = new LUDecomposition(z).getSolver().getInverse();
		

		
		double first = 1/(Math.pow(2*Math.PI, k/2)*Math.pow(z_det, 1/2));
		RealMatrix factor1 = x_sub_m.transpose();
		RealMatrix factor2 = z_inverse.multiply(x_sub_m);
		RealMatrix factor3 = x_sub_m;
		RealMatrix f4 = factor1.multiply(factor2);
		double f5 = f4.multiply(factor3).getEntry(0, 0);
		double second = -0.5 * f5;
		return first*Math.exp(second);
	}
}
