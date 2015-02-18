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
				l2.addGridCell(0, 2);
				l2.addGridCell(1, 2);
				l2.addGridCell(1, 1);
				l2.addGridCell(1, 0);
				l2.addGridCell(2, 0);
				l2.addGridCell(3, 0);
				l2.addGridCell(4, 0);
				l2.addGridCell(5, 0);
				l2.addGridCell(6, 0);
				l2.addGridCell(6, 1);
				l2.addGridCell(6, 2);
				l2.addGridCell(6, 3);
		
		GridCellList l1 = new GridCellList();
				l1.addGridCell(4, 10);
				l1.addGridCell(5, 10);
				l1.addGridCell(6, 10);
				l1.addGridCell(7, 10);
				l1.addGridCell(8, 10);

		
		System.out.println(new OwdDistance().getDistance(
				new OwdQueryTrajectory(null, new OwdData(l1), null),
				new OwdDataTrajectory(null, new OwdData(l2))));

	}

}
