package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * 
 * @author marcus
 *
 */
public class SubtrajectoryConstructStrategy implements ITrajectoryConstructStrategy {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SubtrajectoryConstructStrategy.class);
	
	private final Map<String ,Deque<Deque<Tuple<ITimeInterval>>>> vehTrajMap = new HashMap<>();

	private final Deque<ITimeInterval> windows = new LinkedList<>();
	
	private final Set<Tuple<ITimeInterval>> endedTrajsFirstPnts = new HashSet<>();
	
	public SubtrajectoryConstructStrategy() {
		this.windows.add(new TimeInterval(new PointInTime(-2), new PointInTime(-1)));
	}

	@Override
	public Deque<Deque<Tuple<ITimeInterval>>> getResultsToTransfer(Tuple<ITimeInterval> incoming) {

		final Deque<Deque<Tuple<ITimeInterval>>> result = new LinkedList<>();
		
		final ITimeInterval incomingTI = incoming.getMetadata();

		if (incomingTI.getEnd().after(this.windows.peekLast().getEnd())) {
			this.windows.addLast(incomingTI);

			LOGGER.debug("Window shift detected by incoming tuple: "+ incomingTI);
			
			if (incomingTI.getStart().afterOrEquals(this.windows.peekFirst().getEnd())) {

				LOGGER.debug("Window end detected: " + this.windows.pollFirst().getEnd());
				
				/* add each tuple included in the closed window to the result */
				for (final Deque<Deque<Tuple<ITimeInterval>>> vehTrajs : vehTrajMap.values()) {
					for (Deque<Tuple<ITimeInterval>> trajs : vehTrajs) {
						if (!trajs.isEmpty() && !this.endedTrajsFirstPnts.remove(trajs.peekFirst())) {
							result.add(new LinkedList<>(trajs));
							LOGGER.debug("Add Subtrajectory to Result: " + trajs);
						}
					}
				}
				
				/* remove invalid tuples after */
				for(final Deque<Deque<Tuple<ITimeInterval>>> vehTraj : this.vehTrajMap.values()) {
					while(!vehTraj.isEmpty()) {
						final Deque<Tuple<ITimeInterval>> trajPts = vehTraj.peekFirst();
						while (!trajPts.isEmpty() && trajPts.peekFirst().getMetadata().getEnd().beforeOrEquals(incomingTI.getStart())) {
							LOGGER.trace("Remove invalid tuple:" + trajPts.peekFirst());
							trajPts.removeFirst();
						}
						if(!trajPts.isEmpty()) {
							break;
						}
						vehTraj.removeFirst();
					}
				}
			}
		}
		
		/* get the vehicle Id */
		final String vehicleId = incoming.getAttribute(TrajectoryConstructPO.VEHICLE_ID_POS);

		/* add the tuple to its route */
		Deque<Deque<Tuple<ITimeInterval>>> vehicleTrajectories = this.vehTrajMap.get(vehicleId);
		if(vehicleTrajectories == null) {
			this.vehTrajMap.put(vehicleId, vehicleTrajectories = new LinkedList<>());			
		}
		if(vehicleTrajectories.isEmpty()) {
			vehicleTrajectories.addLast(new LinkedList<>());
		}
		
		vehicleTrajectories.peekLast().addLast(incoming);
		LOGGER.trace("Add Tuple: " + incoming);
		
		/* test whether this tuple marks the end of the route */
		if(((int)incoming.getAttribute(TrajectoryConstructPO.STATE_POS)) == -1) {
			
			final Deque<Tuple<ITimeInterval>> endedTraj = vehicleTrajectories.peekLast();
			LOGGER.debug("Add ended Trajectory to Result: " + endedTraj);
			
			result.add(new LinkedList<>(endedTraj));
			
			this.endedTrajsFirstPnts.add(endedTraj.peekFirst());
			LOGGER.debug("Add first tuple to fullTrajs: " + endedTraj.peekFirst());
			
			vehicleTrajectories.addLast(new LinkedList<Tuple<ITimeInterval>>());
		}
		return result;
	}

	@Override
	public void process_close() {
		this.vehTrajMap.clear();
		this.windows.clear();
		this.endedTrajsFirstPnts.clear();
	}
}
