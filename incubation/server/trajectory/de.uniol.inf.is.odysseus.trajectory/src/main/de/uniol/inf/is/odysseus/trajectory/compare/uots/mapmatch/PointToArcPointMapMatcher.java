package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.ItemBoundable;
import com.vividsolutions.jts.index.strtree.ItemDistance;
import com.vividsolutions.jts.index.strtree.STRtree;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class PointToArcPointMapMatcher extends AbstractMapMatcher {
	
	
	private final static Logger LOGGER = LoggerFactory.getLogger(PointToArcPointMapMatcher.class);

	private final static PointToArcPointMapMatcher INSTANCE = new PointToArcPointMapMatcher();
	
	public static PointToArcPointMapMatcher getInstance() {
		return INSTANCE;
	}
	
	private static final ItemDistance ITEM_DISTANCE = new ItemDistance() {
		
		@Override
		public double distance(ItemBoundable item1, ItemBoundable item2) {
			return ((LineSegment)item1.getItem()).distance(((Point)item2.getItem()).getCoordinate());
		}
	};
	
	
	private final Map<UndirectedSparseGraph<Point, LineSegment>, STRtree> strTrees = new ConcurrentHashMap<>();
	
	private PointToArcPointMapMatcher() { }
	
	
	@Override
	protected List<Point> getGraphPoints(final IRawTrajectory trajectory, final UndirectedSparseGraph<Point, LineSegment> graph) {
		
		STRtree strTree = this.strTrees.get(graph);
		if(strTree == null) {
			synchronized(graph) {
				strTree = this.strTrees.get(graph);
				if(strTree == null) {
					this.strTrees.put(graph, strTree = new STRtree());
				
					for(final LineSegment ls : graph.getEdges()) {
						strTree.insert(new Envelope(ls.getCoordinate(0), ls.getCoordinate(1)), ls);
					}
					strTree.build();
				
					if(LOGGER.isDebugEnabled()) {
						LOGGER.debug("New STRtree build for Graph " + graph.hashCode());
					}
				}
			}
		}
		
		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();

		for(final Point point : trajectory.getPoints()) {
			final Pair<Point> endpoints = graph.getEndpoints(
							(LineSegment)strTree.nearestNeighbour(point.getEnvelopeInternal(), 
							point, 
							ITEM_DISTANCE)
			);
			
			graphPoints.add(
					point.distance(endpoints.getFirst()) < point.distance(endpoints.getSecond()) ? 
							endpoints.getFirst() : endpoints.getSecond());
		}
		
		return new ArrayList<>(graphPoints);
	}
}
