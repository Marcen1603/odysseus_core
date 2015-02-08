package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import org.junit.Test;

import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.OsmGraphLoader;

public class OWDTrajectoryTest {

	GeometryFactory fact = new GeometryFactory();
	
	@Test
	public final void test() {
		new OsmGraphLoader().load("new.osm", 32);
	}

}
