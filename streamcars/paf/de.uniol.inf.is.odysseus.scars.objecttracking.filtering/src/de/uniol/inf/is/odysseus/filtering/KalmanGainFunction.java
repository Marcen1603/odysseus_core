/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

/**
 * @author dtwumasi
 *
 */
public class KalmanGainFunction implements IFilterFunction {

	public int functionID = 1;
		
	private HashMap<Integer, Object> parameters;
	
	public KalmanGainFunction() {
		
	}
	
	public KalmanGainFunction(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
	}
	

	public double[][] compute() {
		
		double[][] result;
		
		RealMatrix oldCovariance = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.OLD_COVARIANCE));
		RealMatrix newCovariance = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.NEW_COVARIANCE));
		
		RealMatrix temp = new RealMatrixImpl();
		
		temp = oldCovariance.add(newCovariance);
		temp = temp.inverse();
		temp = oldCovariance.multiply(temp);
		
		result = temp.getData();
		
		return result;
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
