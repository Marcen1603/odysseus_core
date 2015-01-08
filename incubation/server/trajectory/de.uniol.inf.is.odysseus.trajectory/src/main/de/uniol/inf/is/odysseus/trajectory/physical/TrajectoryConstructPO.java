package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class TrajectoryConstructPO<T extends Tuple<ITimeInterval>> extends
		AbstractPipe<T, T> {

	final static int VEHICLE_ID_POS = 2;
	final static int POINT_POS = 3;
	final static int STATE_POS = 4;
	
	private ITrajectoryConstructStrategy strategy;

	public TrajectoryConstructPO(final ITrajectoryConstructStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		for (RouteConstructResult result : this.strategy.getResultsToTransfer(object)) {
			final Tuple<ITimeInterval> route = new Tuple<>(new Object[] {
					result.vehicleId, result.points }, true);
			route.setMetadata(object.getMetadata());
			this.transfer((T) route);
		}
	}
}

final class RouteConstructResult {

	final String vehicleId;
	final List<Point> points = new LinkedList<>();

	RouteConstructResult(final String vehicleId) {
		this.vehicleId = vehicleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result
				+ ((vehicleId == null) ? 0 : vehicleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteConstructResult other = (RouteConstructResult) obj;
		if(this.vehicleId != other.vehicleId) {
			return false;
		}
		if(this.points.size() != other.points.size()) {
			return false;
		}

		for(int i = 0; i < this.points.size(); i++) {
			if(!this.points.get(i).getCoordinate().equals(other.points.get(i).getCoordinate())) {
				return false;
			}
		}

		return true;
	}
}
