package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * 
 * @author marcus
 *
 */
public class SubtrajectoryConstructStrategy implements ITrajectoryConstructStrategy {
	
	
	private final Map<String, Queue<Tuple<ITimeInterval>>> routesMap = new HashMap<>();
	

	@Override
	public List<RouteConstructResult> getResultsToTransfer(Tuple<ITimeInterval> incoming) {
		
		/* get the vehicle Id */
		final String vehicleId = incoming.getAttribute(TrajectoryConstructPO.VEHICLE_ID_POS);
		
		Queue<Tuple<ITimeInterval>> vehicleQueue = this.routesMap.get(vehicleId);		
		if(vehicleQueue == null) {
			this.routesMap.put(vehicleId, vehicleQueue = new LinkedList<>());
		}
		vehicleQueue.add(incoming);
		
		/* test old elements if they are still valid */
		final PointInTime startTimestamp = incoming.getMetadata().getStart();
		final List<RouteConstructResult> finishedRoutes = new LinkedList<>();
		
		//
		if(((int)incoming.getAttribute(TrajectoryConstructPO.STATE_POS)) == -1) {
			final RouteConstructResult result = new RouteConstructResult(vehicleId);
			while(!vehicleQueue.isEmpty()) {
				result.points.add(vehicleQueue.poll().getAttribute(3));
			}
			finishedRoutes.add(result);
		}
		
		for(final String vehicleIdKey : routesMap.keySet()) {
			final Queue<Tuple<ITimeInterval>> valueQueue = this.routesMap.get(vehicleIdKey);
			
			if(!valueQueue.isEmpty() && valueQueue.peek().getMetadata().getEnd().beforeOrEquals(startTimestamp)) {
				
				final RouteConstructResult result = new RouteConstructResult(vehicleIdKey);
				result.points.add(valueQueue.poll().getAttribute(TrajectoryConstructPO.POINT_POS));
				
				while(!valueQueue.isEmpty() && valueQueue.peek().getMetadata().getEnd().beforeOrEquals(startTimestamp)) {
					result.points.add(valueQueue.poll().getAttribute(TrajectoryConstructPO.POINT_POS));
				}
				
				finishedRoutes.add(result);
			}
		}

		return finishedRoutes;
	}
	
}
