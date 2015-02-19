package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Arrays;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.OwdDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCell;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.AdvancedBresenhamRasterizer;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.IRasterizer;

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
				l1.addGridCell(1, 0);
				l1.addGridCell(1, 1);
				l1.addGridCell(1, 2);
				l1.addGridCell(2, 2);
				l1.addGridCell(2, 3);
				l1.addGridCell(2, 4);

				IRasterizer rast = new AdvancedBresenhamRasterizer();
				
				GeometryFactory gf = new GeometryFactory();
				OwdDataTrajectory j = rast.rasterize(new RawIdTrajectory(Arrays.asList(
						gf.createPoint(new Coordinate(3000,110)),
							gf.createPoint(new Coordinate(4,101)),
							gf.createPoint(new Coordinate(332,11111))),
						null, 3, null, null), 1);
		for(GridCell c : j.getData().getGridCells().getFirst()) {
			//System.out.println(c);
		}
				
		System.out.println(new OwdDistance().getDistance(
				new OwdQueryTrajectory(null, new OwdData(l2), null),
				j));
		


	}

}
