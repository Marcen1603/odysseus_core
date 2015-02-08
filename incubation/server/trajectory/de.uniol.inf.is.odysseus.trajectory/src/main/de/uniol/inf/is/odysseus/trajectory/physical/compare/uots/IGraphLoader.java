package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public interface IGraphLoader<K> extends IObjectLoader<UndirectedSparseGraph<Point, LineSegment>, K> {

}
