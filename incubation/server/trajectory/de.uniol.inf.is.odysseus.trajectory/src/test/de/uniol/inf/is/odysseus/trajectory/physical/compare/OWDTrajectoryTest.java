package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import org.junit.Test;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.GraphBuilderFactory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.OsmGraphLoader;

public class OWDTrajectoryTest {

	GeometryFactory fact = new GeometryFactory();
	
	@Test
	public final void test() {
		for(Point p : GraphBuilderFactory.getInstance().load("new.osm", 32).getVertices()) {
			System.out.println(p);
		}
	}

}
