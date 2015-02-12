package de.uniol.inf.is.odysseus.trajectory.physical.construct;


public class DefaultTrajectoryConstructStrategy  {
//implements ITrajectoryConstructStrategy {

//	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultTrajectoryConstructStrategy.class);
//
//	protected final Map<String ,TrajectoryInter> vehTrajMap = new HashMap<>();
//	
//	@Override
//	public List<Trajectory> getResultsToTransfer(Tuple<ITimeInterval> incoming) {
//		
//		final List<Trajectory> result = new LinkedList<>();
//		
//		/* get the vehicle Id */
//		final String vehicleId = incoming.getAttribute(TrajectoryConstructPO.VEHICLE_ID_POS);
//
//		/* add the tuple to its route */
//		TrajectoryInter vehTraj = this.vehTrajMap.get(vehicleId);
//
//		if(vehTraj == null) {
//			if(((int)incoming.getAttribute(TrajectoryConstructPO.STATE_POS)) != 0) {
//				return result;
//			}
//			this.vehTrajMap.put(vehicleId, vehTraj = new TrajectoryInter());	
//			vehTraj.start = incoming.getAttribute(TrajectoryConstructPO.TIMESTAMP_POS);
//			LOGGER.trace("Next tuple is trajectory begin");
//		} 
//		vehTraj.points.add((Point)incoming.getAttribute(TrajectoryConstructPO.POINT_POS));
//		LOGGER.trace("Add Tuple: " + incoming);
//			
//		/* test whether this tuple marks the end of the route */
//		if(((int)incoming.getAttribute(TrajectoryConstructPO.STATE_POS)) == -1) {
//				
//			LOGGER.debug("Last tuple was trajectory end at: " + incoming);
//				
//			result.add(new Trajectory(
//					vehicleId, 
//					vehTraj.points, 
//					vehTraj.start,
//					(long)incoming.getAttribute(TrajectoryConstructPO.TIMESTAMP_POS))
//			);
//			this.vehTrajMap.remove(vehicleId);
//		}
//
//		return result;
//	}
//
//	@Override
//	public void process_close() {
//		this.vehTrajMap.clear();
//	}
//	
//	private final static class TrajectoryInter {
//		private long start;
//		private List<Point> points = new LinkedList<>();
//	}

}
