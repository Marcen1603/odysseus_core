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

package de.uniol.inf.is.odysseus.rcp.dashboard.test.controller;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.test.TestDashboardPart;

public class DashboardPartControllerTest {

	@Test
	public void testConstructor() {
		new DashboardPartController(new TestDashboardPart());
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testConstructorNullArgs() {
		new DashboardPartController(null);
	}
	
	@Test
	public void testGetPart() {
		TestDashboardPart part = new TestDashboardPart();
		DashboardPartController controller = new DashboardPartController(part);
		
		assertEquals(controller.getDashboardPart(), part);
	}
}
