package de.uniol.inf.is.odysseus.costmodel.mockup.streamCharacteristic;

import de.uniol.inf.is.odysseus.costmodel.streamCharacteristic.IStreamCharacteristic;

public class TimeDistance implements IStreamCharacteristic {
	private final double timeDistance;

	public TimeDistance(double timeDistance) {
		this.timeDistance = timeDistance;
	}

	public double getTimeDistance() {
		return timeDistance;
	}

	@Override
	public String toString() {
		return "TimeDistance: " + timeDistance;
	}
}
