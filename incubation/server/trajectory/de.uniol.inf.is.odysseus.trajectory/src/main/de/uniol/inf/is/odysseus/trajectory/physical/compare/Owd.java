package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.trajectory.physical.TrajectoryComparePO;

public class Owd implements ITrajectoryCompareAlgorithm {

	private final static Logger LOGGER = LoggerFactory.getLogger(Owd.class);
	
	
	private final int k;
	
	private List<Point> q = new LinkedList<>();
	
	
	private final Map<Tuple<ITimeInterval>, Double> trajs = new HashMap<>();
	
	private final Deque<ITimeInterval> windows = new LinkedList<>();

	public Owd(final int k) {
		this.k = k;
		this.windows.addLast(new TimeInterval(new PointInTime(-2), new PointInTime(-1)));
		
		//Test
		GeometryFactory gf = new GeometryFactory();
		q.add(gf.createPoint(new Coordinate(1, 1)));
		q.add(gf.createPoint(new Coordinate(2, 2)));
		q.add(gf.createPoint(new Coordinate(3, 3)));
		q.add(gf.createPoint(new Coordinate(4, 4)));
		q.add(gf.createPoint(new Coordinate(5, 5)));
		q.add(gf.createPoint(new Coordinate(6, 6)));
		q.add(gf.createPoint(new Coordinate(7, 7)));

	}
	
	@Override
	public Tuple<ITimeInterval> getKNearest(Tuple<ITimeInterval> incoming) {
		
		final ITimeInterval incomingTI = incoming.getMetadata();

		/* remove all old trajectories */
		if (incomingTI.getEnd().after(this.windows.peekLast().getEnd())) {
			this.windows.addLast(incomingTI);

			LOGGER.debug("Window shift detected by incoming tuple: "+ incomingTI);
			
			if (incomingTI.getStart().afterOrEquals(this.windows.peekFirst().getEnd())) {

				LOGGER.debug("Window end detected at: " + this.windows.pollFirst().getEnd());
				
				/* remove invalid tuples after */
				final Collection<Tuple<ITimeInterval>> keysToRemove = new LinkedList<>();
				for(final Tuple<ITimeInterval> key : this.trajs.keySet()) {
					if(key.getMetadata().getEnd().beforeOrEquals(incomingTI.getStart())) {
						keysToRemove.add(key);
					}
				}
				for(final Tuple<ITimeInterval> keyToRemove : keysToRemove) {
					LOGGER.trace("Remove invalid trajectory: " + this.trajs.remove(keyToRemove));
				}
			}
		}
		
		
		// berechnen
		double distance = 0;
		for(Point p : q) {
			for(Point t : (Iterable<Point>)incoming.getAttribute(TrajectoryComparePO.POINTS_POS)) {
				distance += p.distance(t);
			}
		}
		
		this.trajs.put(incoming, 81.0);
		
		
		Object[] ooo = new Object[trajs.size() > k ? k : trajs.size()];
		int i = 0;
		for(Tuple<ITimeInterval> t : trajs.keySet()) {
			if(i > ooo.length) break;
			ooo[i++] = new Object[] {
					i, 
					distance,
					t.getAttribute(TrajectoryComparePO.VEHICLE_ID_POS),
					t.getAttribute(TrajectoryComparePO.POINTS_POS)
				};
		}
		
		return new Tuple<ITimeInterval>(new Object[] {"QTId", this.getK(), 10, 10, ooo }, true);
	}
	
	@Override
	public int getK() {
		return this.k;
	}

}