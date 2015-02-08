package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class GraphLoaderFactory extends AbstractObjectLoaderFactory<IGraphLoader<String, Integer>, UndirectedSparseGraph<Point, LineSegment>, String, Integer> {

	@Override
	protected String convertKey(String key) {
		return key.substring(key.lastIndexOf(".") + 1).toUpperCase();
	}


	@Override
	protected IGraphLoader<String, Integer> createLoader(String convertedKey) {
		switch(convertedKey) {
		case "OSM" : return new OsmGraphLoader();
		}
		throw new RuntimeException("No GraphLoader found");
	}

}
