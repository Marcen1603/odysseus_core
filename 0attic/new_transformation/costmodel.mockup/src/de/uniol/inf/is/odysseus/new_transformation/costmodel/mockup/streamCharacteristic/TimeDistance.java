package de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.streamCharacteristic;

import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.IStreamCharacteristic;

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
