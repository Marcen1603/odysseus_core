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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNull;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.test.TestDashboardPart;

public class DashboardPartPlacemnetTest {

	@Test
	public void testConstructor() {
		new DashboardPartPlacement(new TestDashboardPart(), "File", "Title", 100, 100, 200, 200);
	}
	
	@Test( dataProvider = "constructorInvalidPlacementDataDataProvider", expectedExceptions = IllegalArgumentException.class)
	public void testConstructorInvalidPlacementData(int x, int y, int w, int h) {
		new DashboardPartPlacement(new TestDashboardPart(),  "File", "Title", x, y, w, h);
	}
	
	@Test( expectedExceptions = NullPointerException.class )
	public void testConstructorNullArgs() {
		new DashboardPartPlacement(null, "File", "Title", 100, 100, 100, 100);
	}
	
	@Test
	public void testConstructorEmptyTitle() {
		DashboardPartPlacement place = new DashboardPartPlacement(new TestDashboardPart(), "File", null, 100, 100, 200, 200);
		assertNull(place.getTitle());
		assertFalse(place.hasTitle());
	}

	@Test
	public void testGetterAndSetter() {
		IDashboardPart part = new TestDashboardPart(); 
		DashboardPartPlacement place = new DashboardPartPlacement(part, "File", "Title", 100, 200, 300, 400);
		assertEquals(place.getDashboardPart(), part);
		assertEquals(place.getTitle(), "Title");
		assertEquals(place.getX(), 100);
		assertEquals(place.getY(), 200);
		assertEquals(place.getWidth(), 300);
		assertEquals(place.getHeight(), 400);
		assertEquals(place.getFilename(), "File");
		assertTrue(place.hasTitle());
		
		place.setX(99);
		assertEquals(place.getX(), 99);
		
		place.setY(199);
		assertEquals(place.getY(), 199);
		
		place.setHeight(399);
		assertEquals(place.getHeight(), 399);
		
		place.setWidth(499);
		assertEquals(place.getWidth(), 499);
		
		place.setTitle("Moin");
		assertEquals(place.getTitle(), "Moin");
		place.setTitle(null);
		assertNull(place.getTitle());
		assertFalse(place.hasTitle());
	}
	
	@SuppressWarnings("unused")
	@DataProvider
	private static Object[][] constructorInvalidPlacementDataDataProvider() {
		return new Object[][] {
				{-1, 1, 1, 1},	
				{1, -1, 1, 1},	
				{1, 1, -1, 1},	
				{1, 1, 1, -1},	
		};
	}
}
