/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.dashboard.test.editors;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.test.TestDashboardPart;

public class DashboardPartPlacementTest {

	@Test
	public void testConstructor() {
		new DashboardPartPlacement(new TestDashboardPart(), "File", 100, 100, 200, 200);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorEmptyFilenameArg() {
		new DashboardPartPlacement(new TestDashboardPart(), "", 100, 100, 100, 100);
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testConstructorNullDashboardPartArg() {
		new DashboardPartPlacement(null, "File", 100, 100, 100, 100);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullFilenameArg() {
		new DashboardPartPlacement(new TestDashboardPart(), null, 100, 100, 100, 100);
	}

	@Test
	public void testGetterAndSetter() {
		final IDashboardPart part = new TestDashboardPart();
		final DashboardPartPlacement place = new DashboardPartPlacement(part, "File", 100, 200, 300, 400);
		assertEquals(place.getDashboardPart(), part);
		assertEquals(place.getX(), 100);
		assertEquals(place.getY(), 200);
		assertEquals(place.getWidth(), 300);
		assertEquals(place.getHeight(), 400);
		assertEquals(place.getFilename(), "File");

		place.setX(99);
		assertEquals(place.getX(), 99);

		place.setY(199);
		assertEquals(place.getY(), 199);

		place.setHeight(399);
		assertEquals(place.getHeight(), 399);

		place.setWidth(499);
		assertEquals(place.getWidth(), 499);
	}
}
