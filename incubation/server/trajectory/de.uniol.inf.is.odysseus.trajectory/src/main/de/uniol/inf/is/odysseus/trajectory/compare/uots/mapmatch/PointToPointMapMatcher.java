package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.GeometryItemDistance;
import com.vividsolutions.jts.index.strtree.ItemDistance;
import com.vividsolutions.jts.index.strtree.STRtree;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * An implementation of <tt>AbstractMapMatcher</tt>. Trajectories raw points
 * are mapped to their <i>nearest graph points in Euclidean space</i>.
 * 
 * @author marcus
 *
 */
public class PointToPointMapMatcher extends AbstractMapMatcher {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(PointToPointMapMatcher.class);

	/** the singleton instance */
	private final static PointToPointMapMatcher INSTANCE = new PointToPointMapMatcher();
	
	/**
	 * Returns the <tt>PointToPointMapMatcher</tt> as an eager singleton.
	 * 
	 * @return the <tt>PointToPointMapMatcher</tt> as an eager singleton
	 */
	public static PointToPointMapMatcher getInstance() {
		return INSTANCE;
	}
	
	/**
	 * the distance function how two measure the distance between two <tt>Points</tt>
	 * in an <tt>STRtree</tt>
	 */
	private static final ItemDistance ITEM_DISTANCE = new GeometryItemDistance();
	
	
	/**
	 * For each instance of an <tt>UndirectedSparseGraph</tt> an <tt>STRtree</tt> will be hold
	 */
	private volatile Map<UndirectedSparseGraph<Point, LineSegment>, STRtree> strTrees = new ConcurrentHashMap<>();
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private PointToPointMapMatcher() {}

	/**
	 * {@inheritDoc}
	 * <p>A trajectory's raw point is mapped to its nearest point in the graph. Here the
	 * distance function is applied in Euclidean space.</p>
	 * 
	 */
	@Override
	protected List<Point> getGraphPoints(final IRawTrajectory trajectory, final UndirectedSparseGraph<Point, LineSegment> graph) 
			throws IllegalArgumentException {

		if(trajectory == null) {
			throw new IllegalArgumentException("trajectory is null");
		}
		if(graph == null) {
			throw new IllegalArgumentException("graph is null");
		}
		
		STRtree strTree = this.strTrees.get(graph);
		if(strTree == null) {
			synchronized (this) {
				strTree = this.strTrees.get(graph);
				if(strTree == null) {
					strTree = new STRtree();
		
					for(final Point graphPoint : graph.getVertices()) {
						strTree.insert(graphPoint.getEnvelopeInternal(), graphPoint);
					}
			
					strTree.build();
					this.strTrees.put(graph, strTree);
			
					if(LOGGER.isDebugEnabled()) {
						LOGGER.debug("New STRtree build for Graph " + graph.hashCode());
					}
				}
			}
		}

		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();

		for(final Point point : trajectory.getPoints()) {
			graphPoints.add((Point)strTree.nearestNeighbour(point.getEnvelopeInternal(), point, ITEM_DISTANCE));
		}

		return new ArrayList<Point>(graphPoints);
	}
}
