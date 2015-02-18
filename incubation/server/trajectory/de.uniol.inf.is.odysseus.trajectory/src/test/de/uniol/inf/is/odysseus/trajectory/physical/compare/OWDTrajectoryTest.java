package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import org.junit.Test;

import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.owd.OwdDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCell;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdQueryTrajectory;

public class OWDTrajectoryTest {

	GeometryFactory fact = new GeometryFactory();
	
	@Test
	public final void test() {

		GridCellList l2 = new GridCellList();
				l2.add(new GridCell(0, 2));
				l2.add(new GridCell(1, 2));
				l2.add(new GridCell(1, 1));
				l2.add(new GridCell(1, 0));
				l2.add(new GridCell(2, 0));
				l2.add(new GridCell(3, 0));
				l2.add(new GridCell(4, 0));
				l2.add(new GridCell(5, 0));
				l2.add(new GridCell(6, 0));
				l2.add(new GridCell(6, 1));
				l2.add(new GridCell(6, 2));
				l2.add(new GridCell(6, 3));
		
		GridCellList l1 = new GridCellList();
				l1.add(new GridCell(4, 10));
				l1.add(new GridCell(5, 10));
				l1.add(new GridCell(6, 10));
				l1.add(new GridCell(7, 10));
				l1.add(new GridCell(8, 10));

		
		System.out.println(new OwdDistance().getDistance(
				new OwdQueryTrajectory(null, new OwdData(l1), null),
				new OwdDataTrajectory(null, new OwdData(l2))));

	}

}
