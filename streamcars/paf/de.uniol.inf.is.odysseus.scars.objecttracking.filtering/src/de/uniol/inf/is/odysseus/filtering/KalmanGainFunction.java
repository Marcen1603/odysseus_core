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

	public int functionID = 1;
		
	private HashMap<Integer, Object> parameters;
	
	public KalmanGainFunction() {
		
	}
	public KalmanGainFunction(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
		/*this.parameters.put("oldCovariance", oldCovariance);
		this.parameters.put("newCovariance", newCovariance);
		this.parameters.put("outputModel", outputModel);*/
	}
	
	@Override
	public double[][] computeGain() {
		RealMatrix oldCovariance = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.GAIN_OLD_COVARIANCE));
		RealMatrix newCovariance = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.GAIN_NEW_COVARIANCE));
		return oldCovariance.add(newCovariance).inverse().multiply(oldCovariance).getData();
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the parameters
	 */
	public HashMap<Integer, Object> getParameters() {
		return this.parameters;
	} 
	
	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(Integer key, Object value) {
		this.parameters.put(key, value);
	}

	@Override
	public int getFunctionID() {
		return this.functionID;
	}

}
