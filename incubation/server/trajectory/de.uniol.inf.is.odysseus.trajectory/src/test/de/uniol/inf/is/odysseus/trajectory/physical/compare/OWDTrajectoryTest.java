package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.OWDTrajectory.GridCell;

public class OWDTrajectoryTest {

	GeometryFactory fact = new GeometryFactory();
	
	@Test
	public final void test() {
		Point p1 = fact.createPoint(new Coordinate(1.4, 0.5));
		Point p2 = fact.createPoint(new Coordinate(9.5, 2.5));
		OWDTrajectory t = new OWDTrajectory(Arrays.asList(
				p1,
				p2

				),1);
		
		for(GridCell g : t.adjacenceCells) {
			System.out.println(g.x + " " + g.y);
		}
	}

}
