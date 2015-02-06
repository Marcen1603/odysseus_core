package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.trajectory.physical.construct.ITrajectoryConstructStrategy;

public class TrajectoryConstructPO<T extends Tuple<ITimeInterval>> extends
		AbstractPipe<T, T> {

	public final static int TIMESTAMP_POS = 0;
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
		for(final Trajectory traj : this.strategy.getResultsToTransfer(object)) {
			this.transfer((T)new Tuple<ITimeInterval>(new Object[] {
					traj.getId(), traj.getBegin(), traj.getEnd(), traj.getPoints()
			}, true), port);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	protected void process_close() {
		super.process_close();
		this.strategy.process_close();
	}
}
