package de.uniol.inf.is.odysseus.costmodel.mockup.streamCharacteristic;

import de.uniol.inf.is.odysseus.costmodel.streamCharacteristic.IStreamCharacteristic;

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
