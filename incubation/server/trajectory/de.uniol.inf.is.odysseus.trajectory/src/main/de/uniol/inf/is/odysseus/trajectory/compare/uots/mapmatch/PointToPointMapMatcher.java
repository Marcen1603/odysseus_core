package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.GeometryItemDistance;
import com.vividsolutions.jts.index.strtree.STRtree;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;

public class PointToPointMapMatcher extends AbstractMapMatcher {

	private final static Logger LOGGER = LoggerFactory.getLogger(PointToPointMapMatcher.class);

	private final static PointToPointMapMatcher INSTANCE = new PointToPointMapMatcher();
	
	public static PointToPointMapMatcher getInstance() {
		return INSTANCE;
	}
	
	private final Map<NetGraph, STRtree> strTrees = new HashMap<>();
	
	
	private PointToPointMapMatcher() {
	}

	
	@Override
	protected List<Point> getGraphPoints(IRawTrajectory trajectory, NetGraph graph) {

		STRtree strTree = this.strTrees.get(graph);
		if(strTree == null) {
			strTree = new STRtree();
		
			for(final Point p : graph.getComplexGraph().getVertices()) {
				strTree.insert(new Envelope(p.getCoordinate()), p);
			}
			strTree.build();
			this.strTrees.put(graph, strTree);
		}

		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();

		for(final Point point : trajectory.getPoints()) {
			graphPoints.add((Point)strTree.nearestNeighbour(new Envelope(point.getCoordinate()), point, new GeometryItemDistance()));
		}

		return new ArrayList<Point>(graphPoints);
	}
}
