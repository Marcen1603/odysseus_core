package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public abstract class AbstractDataTrajectory<E> extends AbstractQueryTrajectory<E> implements IDataTrajectory<E> {

	private final RawWithId rawTrajectory;
	
	private double distance;
	
	private final TimeInterval timeInterval;
	
	protected AbstractDataTrajectory(RawWithId rawTrajectory, TimeInterval timeInterval, E convertedData,
			Map<String, String> textualAttributes) {
		super(convertedData, textualAttributes);
		this.rawTrajectory = rawTrajectory;
		this.timeInterval = timeInterval;
	}

	@Override
	public RawWithId getRawTrajectory() {
		return this.rawTrajectory;
	}

	@Override
	public double getDistance() {
		return this.distance;
	}

	@Override
	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public TimeInterval getTimeInterval() {
		return this.timeInterval;
	}
}
