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

package de.uniol.inf.is.odysseus.rcp.dashboard.test.handler;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.test.TestDashboardPart;

public class XMLDashboardPartHandlerTest {

	@BeforeMethod
	public void init() {
		DashboardPartRegistry.unregisterAll();
		DashboardPartRegistry.register(TestDashboardPart.class, newDashboardPartDescriptor());
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testLoadNullArgs() throws Exception {
		XMLDashboardPartHandler handler = new XMLDashboardPartHandler();
		handler.load(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testLoadEmptyArrayArgs() throws Exception {
		XMLDashboardPartHandler handler = new XMLDashboardPartHandler();
		handler.load(Lists.<String>newArrayList());
	}
	
	private static DashboardPartDescriptor newDashboardPartDescriptor() {
		return new DashboardPartDescriptor("testDashboardPart", "Something interesting");
	}
}
