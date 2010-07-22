package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;


public class KalmanCorrectStateEstimateFunction implements ICorrectStateEstimateFunction {
		
	private HashMap<Integer, Object> parameters;
	
		public KalmanCorrectStateEstimateFunction() {
	
	}
	
	public KalmanCorrectStateEstimateFunction(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
		/*this.parameters.put("measurementOld", measurementOld);
		this.parameters.put("measurementNew", measurementNew);
		this.parameters.put("gain", gain);*/
	}

	@Override
	public double[] correctStateEstimate() {
		
		double result[];
		// Ansatz:
		// TODO Muss eine neuere Version des Apache Frameworks verwenden,
		
	/*	RealMatrix measurementOld = new RealMatrix((double[]) this.parameters.get(HashConstants.OLD_MEASUREMENT));
		RealMatrix measurementNew = new RealMatrixImpl((double[]) this.parameters.get(HashConstants.NEW_MEASUREMENT));
		RealMatrix gain = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.GAIN));	
		RealMatrix resultRM = new RealMatrixImpl(result);
		return result = measurementOld.add(  gain.multiply  (  measurementNew.subtract(measurementOld)  )  ).getData();
		*/
		return null; 
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
