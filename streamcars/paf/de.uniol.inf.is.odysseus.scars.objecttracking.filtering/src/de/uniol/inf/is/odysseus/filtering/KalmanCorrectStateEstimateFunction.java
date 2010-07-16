package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class KalmanCorrectStateEstimateFunction implements ICorrectStateEstimateFunction {
	
	// measOld, measNew, Gain, output
	
	private HashMap<String, Object> parameters;
	
	public KalmanCorrectStateEstimateFunction(HashMap<String, Object> parameters) {
		this.parameters = parameters;

	}

	@Override
	public double[] correctStateEstimate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameters(HashMap<String, Object> parameters) {
		// TODO Auto-generated method stub
	}
	
	/**
	* @param parameters the parameters to set
	*/
	public void addParameter(String key, Object value) {
			this.parameters.put(key, value);
		}
	
}
