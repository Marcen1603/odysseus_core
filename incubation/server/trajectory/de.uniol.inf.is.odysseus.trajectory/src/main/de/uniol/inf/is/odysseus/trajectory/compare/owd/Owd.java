package de.uniol.inf.is.odysseus.trajectory.compare.owd;


public class Owd {//implements ITrajectoryCompareAlgorithm {

//	private final static Logger LOGGER = LoggerFactory.getLogger(Owd.class);
//	
//	
//	private final int k;
//	
//	private List<Point> queryTrajectory = new LinkedList<>();
//	private Map<Integer, OWDTrajectory> owdQs = new HashMap<>();
//	
//	
//	private final Map<Tuple<ITimeInterval>, Double> trajs = new HashMap<>();
//	
//	private Map<OWDTrajectory, Tuple<ITimeInterval>> owds;
//	
//	private final Deque<ITimeInterval> windows = new LinkedList<>();
//
//	public Owd(final int k) {
//		this.k = k;
//		this.windows.addLast(new TimeInterval(new PointInTime(-2), new PointInTime(-1)));
//	}
//	
////	@Override
////	public Tuple<ITimeInterval> getKNearest(Tuple<ITimeInterval> incoming) {
////		
////		final ITimeInterval incomingTI = incoming.getMetadata();
////
//////		/* remove all old trajectories */
//////		if (incomingTI.getEnd().after(this.windows.peekLast().getEnd())) {
//////			this.windows.addLast(incomingTI);
//////
//////			LOGGER.debug("Window shift detected by incoming tuple: "+ incomingTI);
//////			
//////			if (incomingTI.getStart().afterOrEquals(this.windows.peekFirst().getEnd())) {
//////
//////				LOGGER.debug("Window end detected at: " + this.windows.pollFirst().getEnd());
//////				
//////				/* remove invalid tuples after */
//////				final Collection<Tuple<ITimeInterval>> keysToRemove = new LinkedList<>();
//////				for(final Tuple<ITimeInterval> key : this.trajs.keySet()) {
//////					if(key.getMetadata().getEnd().beforeOrEquals(incomingTI.getStart())) {
//////						keysToRemove.add(key);
//////					}
//////				}
//////				for(final Tuple<ITimeInterval> keyToRemove : keysToRemove) {
//////					LOGGER.trace("Remove invalid trajectory: " + this.trajs.remove(keyToRemove));
//////				}
//////			}
//////		}
//////		
////		OWDTrajectory hhh = 
////				new OWDTrajectory((List<Point>)incoming.getAttribute(TrajectoryComparePO.POINTS_POS), 1);
////		
////		// berechnen
////		double distance = 0;
////		for(Point p : queryTrajectory) {
////			for(Point t : (Iterable<Point>)incoming.getAttribute(TrajectoryComparePO.POINTS_POS)) {
////				distance += p.distance(t);
////			}
////		}
////		
////		this.trajs.put(incoming, 81.0);
////		
////		
////		Object[] ooo = new Object[trajs.size() > k ? k : trajs.size()];
////		int i = 0;
////		for(Tuple<ITimeInterval> t : trajs.keySet()) {
////			if(i > ooo.length) break;
////			ooo[i++] = new Object[] {
////					i, 
////					distance,
////					t.getAttribute(TrajectoryComparePO.VEHICLE_ID_POS),
////					t.getAttribute(TrajectoryComparePO.POINTS_POS)
////				};
////		}
////		
////		return new Tuple<ITimeInterval>(new Object[] {"QTId", this.getK(), 10, 10, ooo }, true);
////	}
//	
//	@Override
//	public int getK() {
//		return this.k;
//	}
//	
//	
//	private void calc() {
//		
//	}
//
//
//	@Override
//	public void removeBefore(PointInTime time) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public List<AbstractDataTrajectory> getKNearest(RawDataTrajectory incoming) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}