package de.uniol.inf.is.odysseus.trajectory.physical.construct;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.trajectory.physical.Trajectory;

public class FulltrajectoryConstructStrategy implements ITrajectoryConstructStrategy {

	private final static Logger LOGGER = LoggerFactory.getLogger(SubtrajectoryConstructStrategy.class);
	
	private final Map<String ,Deque<Tuple<ITimeInterval>>> vehTrajMap = new HashMap<>();

	private final Deque<ITimeInterval> windows = new LinkedList<>();
		
	public FulltrajectoryConstructStrategy() {
		this.windows.add(new TimeInterval(new PointInTime(-2), new PointInTime(-1)));
	}

	@Override
	public List<Trajectory> getResultsToTransfer(Tuple<ITimeInterval> incoming) {
		
		final ITimeInterval incomingTI = incoming.getMetadata();

		if (incomingTI.getEnd().after(this.windows.peekLast().getEnd())) {
			this.windows.addLast(incomingTI);

			LOGGER.debug("Window shift detected by incoming tuple: "+ incomingTI);
			
			if (incomingTI.getStart().afterOrEquals(this.windows.peekFirst().getEnd())) {

				LOGGER.debug("Window end detected at: " + this.windows.pollFirst().getEnd());
				
				/* remove invalid tuples after */
				final Collection<String> keysToRemove = new LinkedList<>();
				for(final String key : this.vehTrajMap.keySet()) {
					if(this.vehTrajMap.get(key).peekFirst().getMetadata().getEnd().beforeOrEquals(incomingTI.getStart())) {
						keysToRemove.add(key);
					}
				}
				for(final String keyToRemove : keysToRemove) {
					LOGGER.trace("Remove invalid trajectory: " + this.vehTrajMap.remove(keyToRemove));
				}
			}
		}
		
		return null;
//		
//		final Deque<Deque<Tuple<ITimeInterval>>> result = new LinkedList<>();
//		
//		/* get the vehicle Id */
//		final String vehicleId = incoming.getAttribute(TrajectoryConstructPO.VEHICLE_ID_POS);
//
//		/* add the tuple to its route */
//		Deque<Tuple<ITimeInterval>> vehTraj = this.vehTrajMap.get(vehicleId);
//
//		if(vehTraj == null) {
//			if(((int)incoming.getAttribute(TrajectoryConstructPO.STATE_POS)) != 0) {
//				return result;
//			}
//			this.vehTrajMap.put(vehicleId, vehTraj = new LinkedList<>());	
//			LOGGER.trace("Next tuple is trajectory begin");
//		} 
//		vehTraj.addLast(incoming);
//		LOGGER.trace("Add Tuple: " + incoming);
//			
//		/* test whether this tuple marks the end of the route */
//		if(((int)incoming.getAttribute(TrajectoryConstructPO.STATE_POS)) == -1) {
//				
//			LOGGER.debug("Last tuple was trajectory end at: " + incoming);
//				
//			result.add(new LinkedList<>(vehTraj));
//			this.vehTrajMap.remove(vehicleId);
//		}
//
//		return result;
	}

	@Override
	public void process_close() {
		this.vehTrajMap.clear();
		this.windows.clear();
		this.windows.add(new TimeInterval(new PointInTime(-2), new PointInTime(-1)));
	}

}
