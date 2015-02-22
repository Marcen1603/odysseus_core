/*
 *Copyright 2015 Marcus Behrendt
 * 
 \* Licensed under the Apache License, Version 2.0 (the "License");
 \* you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Arrays;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.AdvancedBresenhamRasterizer;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.IRasterizer;

public class OWDTrajectoryTest {

	GeometryFactory fact = new GeometryFactory();
	
	@Test
	public final void test() {

		final GridCellList l2 = new GridCellList();
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
		
		final GridCellList l1 = new GridCellList();
				l1.addGridCell(1, 0);
				l1.addGridCell(1, 1);
				l1.addGridCell(1, 2);
				l1.addGridCell(2, 2);
				l1.addGridCell(2, 3);
				l1.addGridCell(2, 4);

				final IRasterizer rast = new AdvancedBresenhamRasterizer();
				
				final GeometryFactory gf = new GeometryFactory();
				final OwdDataTrajectory j = rast.rasterize(new RawDataTrajectory(Arrays.asList(
						gf.createPoint(new Coordinate(3000,110)),
							gf.createPoint(new Coordinate(4,101)),
							gf.createPoint(new Coordinate(332,11111))),
						null, 3, null, null), 1);


	}

}
