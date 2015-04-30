package de.uniol.inf.is.odysseus.condition.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings("rawtypes")
public class DeviationAnomalyDetectionPO<T extends Tuple<?>> extends AbstractPipe<T, Tuple> {

	public static final String VALUE_NAME = "value";
	
	private int n = 0;
	private double mean = 0;
	private double m2 = 0;

	@Override
	protected void process_next(T tuple, int port) {
		
		int valueIndex = getOutputSchema().findAttributeIndex(VALUE_NAME);
		double sensorValue = tuple.getAttribute(valueIndex);
		
		// Calculate new value for standard deviation
		double sigma = calcStandardDeviation(sensorValue);
		
		if (sensorValue < mean - 3 * sigma || sensorValue > mean + 3 * sigma) {
			// Maybe here we have an anomaly
			Tuple newTuple = tuple.append(1.0);
			transfer(newTuple);
			return;
		}
	}

	private double calcStandardDeviation(double newValue) {
		n += 1;
		double delta = newValue - mean;
		mean += (delta / n);
		m2 += delta * (newValue - mean);

		if (n < 2)
			return 0;

		double variance = m2 / (n - 1);
		double standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	

}
