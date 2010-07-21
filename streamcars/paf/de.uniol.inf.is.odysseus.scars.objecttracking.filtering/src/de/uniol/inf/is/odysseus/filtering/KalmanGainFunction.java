/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

/**
 * @author mase
 *
 */
public class KalmanGainFunction implements IGainFunction {

/*	@Override
	public double[][] computeGain(double[][] oldCovariance, double[][] newCovariance, Object[] matrixes) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	public int functionID = 1;
	
	/*
	 * Wird nicht mehr benötigt, da die Parameter im
	 * FilterPO gesetzt werden.
	 *
	private  double[][] oldCovariance;
	
	double[][] newCovariance;
	
	private double[] outputModel;
	*/
	
	private HashMap<String, Object> parameters;
	
	public KalmanGainFunction() {
		
	}
	public KalmanGainFunction(HashMap<String, Object> parameters) {
		this.parameters = parameters;
		/*this.parameters.put("oldCovariance", oldCovariance);
		this.parameters.put("newCovariance", newCovariance);
		this.parameters.put("outputModel", outputModel); */
	
	}
	
	@Override
	public double[][] computeGain() {
		return null;
	}

	
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the parameters
	 */
	public HashMap<String, Object> getParameters() {
		return this.parameters;
	} 
	
	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(String key, Object value) {
		this.parameters.put(key, value);
	}

	@Override
	public int getFunctionID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
