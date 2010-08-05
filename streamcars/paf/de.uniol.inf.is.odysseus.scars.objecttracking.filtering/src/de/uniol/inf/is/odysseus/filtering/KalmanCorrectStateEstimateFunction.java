package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;


public class KalmanCorrectStateEstimateFunction implements IFilterFunction {
		
	private HashMap<Integer, Object> parameters;
	
	public KalmanCorrectStateEstimateFunction() {
		this.parameters = new HashMap<Integer, Object>();
	}
	
	
	
	public KalmanCorrectStateEstimateFunction(KalmanCorrectStateEstimateFunction copy) {

		copy.setParameters(new HashMap<Integer, Object>(this.getParameters()));	
		
	}
	
	public KalmanCorrectStateEstimateFunction(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
		/*this.parameters.put("measurementOld", measurementOld);
		this.parameters.put("measurementNew", measurementNew);
		this.parameters.put("gain", gain);*/
	}

	@Override
	public double[] compute() {
		
		double result[];
		
		RealMatrix measurementOld = new RealMatrixImpl((double[]) this.parameters.get(HashConstants.OLD_MEASUREMENT));
		RealMatrix measurementNew = new RealMatrixImpl((double[]) this.parameters.get(HashConstants.NEW_MEASUREMENT));
		RealMatrix gain = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.GAIN));	
		
		RealMatrix temp = new RealMatrixImpl();
		
		temp =  measurementNew.subtract(measurementOld);
		temp =  gain.multiply(temp);
		temp =  measurementOld.add(temp);
		
		result = temp.getColumn(0);
		
		return result;
	
		
	}

	@Override
	public HashMap<Integer, Object> getParameters() {
		return this.parameters;
	}

	@Override
	public void setParameters(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
	}
	
	/**
	* @param parameters the parameters to set
	*/
	public void addParameter(Integer key, Object value) {
			this.parameters.put(key, value);
		}

	@Override
	public int getFunctionID() {
		// TODO Auto-generated method stub
		return 1;
	}
	
}
