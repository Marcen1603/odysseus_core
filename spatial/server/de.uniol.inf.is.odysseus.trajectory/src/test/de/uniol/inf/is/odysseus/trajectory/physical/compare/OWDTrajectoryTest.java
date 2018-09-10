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

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.util.UtmPointCreator;

public class OWDTrajectoryTest {

	GeometryFactory fact = new GeometryFactory();
	
	@Test
	public final void test() {

		System.out.println(new UtmPointCreator(32).createPoint(new Coordinate(53.157505,8.221421)));
	}

}
