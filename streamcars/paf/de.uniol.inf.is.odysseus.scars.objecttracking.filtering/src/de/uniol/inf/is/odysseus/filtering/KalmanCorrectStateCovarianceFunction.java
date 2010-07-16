package de.uniol.inf.is.odysseus.filtering;


import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class KalmanCorrectStateCovarianceFunction implements ICorrectStateCovarianceFunction {
	
	private double[][] covarianceOld;
	
	private double[][] gain;
	
	private double[] outputModell;
	
	private HashMap<String, Object> parameters;
	
	public KalmanCorrectStateCovarianceFunction() {
		
	}
	public KalmanCorrectStateCovarianceFunction(HashMap<String, Object> parameters) {
		/*this.parameters.put("covarianceOld", covarianceOld );
		this.parameters.put("gain", gain);
		this.parameters.put("outputModell", outputModell); */
		this.parameters=parameters;
	}
	
	
	@Override
	public double[][] correctStateCovariance() {
		// TODO Auto-generated method stub
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
		
		return parameters;
	} 
	
	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(String key, Object value) {
		this.parameters.put(key, value);
	}


	@Override
	public int getFunctionID() {
		
		return 1;
	}



}
