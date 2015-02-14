package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import org.junit.Test;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.GraphBuilderFactory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch.MapMatcherFactory;

public class OWDTrajectoryTest {

	GeometryFactory fact = new GeometryFactory();
	
	@Test
	public final void test() {
//		for(Point p : GraphBuilderFactory.getInstance().load("new.osm", 32).getVertices()) {
//			System.out.println(p);
//		}
//		RawDataTrajectory raw = QueryTrajectoryLoaderFactory.getInstance().load("qT.csv", 32);
////		for(Point p : raw.getPoints()) {
////			System.out.println(p);
////		}
//		UotsTrajectory uots = MapMatcherFactory.getInstance().create("Point-to-Point").map(raw, GraphBuilderFactory.getInstance().load("new.osm", 32));
//		
//		for(Point p : uots.getGraphPoints()) {
//			System.out.println(p);
//		}
	}

}
