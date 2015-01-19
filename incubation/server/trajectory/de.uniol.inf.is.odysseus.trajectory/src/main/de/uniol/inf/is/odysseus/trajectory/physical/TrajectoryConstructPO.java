package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.Deque;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.trajectory.physical.construct.ITrajectoryConstructStrategy;

public class TrajectoryConstructPO<T extends Tuple<ITimeInterval>> extends
		AbstractPipe<T, T> {

	public final static int VEHICLE_ID_POS = 2;
	public final static int POINT_POS = 3;
	public final static int STATE_POS = 4;

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(TrajectoryConstructPO.class);
	
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
		for(final Deque<Tuple<ITimeInterval>> trajectory : this.strategy.getResultsToTransfer(object)) {
			final Deque<Point> points = new LinkedList<>();
			for(final Tuple<ITimeInterval> t : trajectory) {
				points.add((Point)t.getAttribute(POINT_POS));
			}
			final Tuple<ITimeInterval> trajTuple = 
					new Tuple<ITimeInterval>(new Object[] { trajectory.peekFirst().getAttribute(VEHICLE_ID_POS), points }, true);
			trajTuple.setMetadata(trajectory.peekFirst().getMetadata());
			this.transfer((T)trajTuple, port);
		}
	}
	
	@Override
	protected void process_close() {
		super.process_close();
		this.strategy.process_close();
	}
}