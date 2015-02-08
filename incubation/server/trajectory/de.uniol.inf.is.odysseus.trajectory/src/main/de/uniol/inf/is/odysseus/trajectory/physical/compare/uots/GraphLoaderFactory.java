package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class GraphLoaderFactory extends AbstractObjectLoaderFactory<IGraphLoader<String>, UndirectedSparseGraph<Point, LineSegment>, String> {

	@Override
	protected String convertKey(String key) {
		return key.substring(key.lastIndexOf(".") + 1).toUpperCase();
	}

	@Override
	protected IGraphLoader<String> createLoader(String convertedKey) {
		switch(convertedKey) {
		case "OSM" : return new OsmGraphLoader();
		}
		throw new RuntimeException("No GraphLoader found");
	}

}
