package de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.streamCharacteristic;

import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.IStreamCharacteristic;

public class TimeIntervalLength implements IStreamCharacteristic {
	private final double timeIntevalLength;

	public TimeIntervalLength(double timeIntervalLength) {
		this.timeIntevalLength = timeIntervalLength;
	}

	public double getTimeIntervalLength() {
		return timeIntevalLength;
	}

	@Override
	public String toString() {
		return "TimeIntervalLength: " + timeIntevalLength;
	}
}
