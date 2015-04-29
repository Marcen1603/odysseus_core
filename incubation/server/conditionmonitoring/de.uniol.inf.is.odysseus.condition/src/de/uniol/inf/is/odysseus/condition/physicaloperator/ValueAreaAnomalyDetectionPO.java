package de.uniol.inf.is.odysseus.condition.physicaloperator;

import de.uniol.inf.is.odysseus.condition.MessageHelper;
import de.uniol.inf.is.odysseus.condition.logicaloperator.ValueAreaAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.condition.messages.WarningLevel;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * An operator that finds anomalies when the values are not in the declared
 * area. Expects the machine id on port 0, the sensor id on port 1 and the value
 * on port 2
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class ValueAreaAnomalyDetectionPO<T extends Tuple<?>> extends AbstractPipe<T, Tuple> {

	private double minValue;
	private double maxValue;
	private double difference;
	private boolean toLow;
	private boolean toHigh;

	public ValueAreaAnomalyDetectionPO(ValueAreaAnomalyDetectionAO ao) {
		this.minValue = ao.getMinValue();
		this.maxValue = ao.getMaxValue();
		toLow = false;
		toHigh = false;
	}
	
	public ValueAreaAnomalyDetectionPO(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		toLow = false;
		toHigh = false;
	}

	@Override
	protected void process_next(T object, int port) {
		
		int machineId = object.getAttribute(0);
		int sensorId = object.getAttribute(1);
		double sensorValue = object.getAttribute(2);
		
		if (sensorValue > maxValue && !toHigh) {
			toHigh = true;
			toLow = false;
			difference = sensorValue - maxValue;
			String description = "Value too high at " + sensorValue;
			Tuple out = MessageHelper.createWarningTuple(machineId, sensorId, WarningLevel.MEDIUM, description);
			transfer(out);
		} else if (sensorValue < minValue && !toLow) {
			toHigh = false;
			toLow = true;
			difference = minValue - sensorValue;
			String description = "Value too low at " + sensorValue;
			Tuple out = MessageHelper.createWarningTuple(machineId, sensorId, WarningLevel.MEDIUM, description);
			transfer(out);
		} else if (sensorValue <= maxValue && sensorValue >= minValue) {
			// Correct value
			toHigh = false;
			toLow = false;
			difference = 0;
			// TODO Negative tuple to tell user that everything is ok again
		}
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
