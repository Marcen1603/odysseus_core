/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class DashboardPartRegistryTest {

	private DashboardPartDescriptor descriptor;
	private final Class<? extends IDashboardPart> dashboardPartClass = TestDashboardPart.class;

	@BeforeMethod
	public void createDashboardPartDescriptor() {
		descriptor = newDashboardPartDescriptor();
		
		DashboardPartRegistry.unregisterAll();
	}

	@Test
	public void testConstructor() {
		new DashboardPartRegistry();
	}

	@Test
	public void testRegisterAndUnregister() {
		assertFalse(DashboardPartRegistry.isRegistered(dashboardPartClass));
		assertFalse(DashboardPartRegistry.isRegistered(descriptor.getName()));

		DashboardPartRegistry.register(dashboardPartClass, descriptor);

		assertTrue(DashboardPartRegistry.isRegistered(dashboardPartClass));
		assertTrue(DashboardPartRegistry.isRegistered(descriptor.getName()));

		DashboardPartRegistry.unregister(descriptor.getName());

		assertFalse(DashboardPartRegistry.isRegistered(dashboardPartClass));
		assertFalse(DashboardPartRegistry.isRegistered(descriptor.getName()));
	}
	
	@Test
	public void testUnregisterByClass() throws Throwable {
		DashboardPartRegistry.register(dashboardPartClass, descriptor);
		
		DashboardPartRegistry.unregister(dashboardPartClass);

		assertFalse(DashboardPartRegistry.isRegistered(dashboardPartClass));
		assertFalse(DashboardPartRegistry.isRegistered(descriptor.getName()));
	}

	@Test
	public void testUnneededUnregister() {
		DashboardPartRegistry.unregister(descriptor.getName());

		assertFalse(DashboardPartRegistry.isRegistered(dashboardPartClass));
		assertFalse(DashboardPartRegistry.isRegistered(descriptor.getName()));
	}
	
	@Test
	public void testUnneededUnregisterByClass() throws Throwable {
		DashboardPartRegistry.unregister(TestDashboardPart2.class);
		
		assertFalse(DashboardPartRegistry.isRegistered(TestDashboardPart2.class));
	}

	@Test
	public void testUnregisterAll() {
		DashboardPartRegistry.register(dashboardPartClass, descriptor);

		assertTrue(DashboardPartRegistry.isRegistered(dashboardPartClass));
		assertTrue(DashboardPartRegistry.isRegistered(descriptor.getName()));

		DashboardPartRegistry.unregisterAll();

		assertFalse(DashboardPartRegistry.isRegistered(dashboardPartClass));
		assertFalse(DashboardPartRegistry.isRegistered(descriptor.getName()));
	}
	
	@Test
	public void testUnregisterAllNoRegistrations() {
		DashboardPartRegistry.unregisterAll();
	}

	@Test
	public void testGetNames() {
		List<String> names = DashboardPartRegistry.getDashboardPartNames();
		assertNotNull(names);
		assertEquals(names.size(), 0);

		DashboardPartRegistry.register(dashboardPartClass, descriptor);
		names = DashboardPartRegistry.getDashboardPartNames();

		assertNotNull(names);
		assertEquals(names.size(), 1);
		assertEquals(names.get(0), "SomeDashboardPart");
	}
	
	@Test
	public void testGetDashboardPartDescriptor() {
		DashboardPartRegistry.register(dashboardPartClass, descriptor);
		
		assertTrue( DashboardPartRegistry.getDashboardPartDescriptor(descriptor.getName()).isPresent());
		assertFalse( DashboardPartRegistry.getDashboardPartDescriptor("Waka").isPresent());
	}
	
	@Test
	public void testCreateDashboartPart() throws InstantiationException {
		DashboardPartRegistry.register(dashboardPartClass, descriptor);
		
		IDashboardPart part = DashboardPartRegistry.createDashboardPart(descriptor.getName());
		assertNotNull(part);
		assertTrue( part instanceof TestDashboardPart);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testCreateDashboardPartUnregisteredClass() throws InstantiationException {
		DashboardPartRegistry.createDashboardPart("Waka");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testCreateDashboardPartNullArgs() throws Throwable {
		DashboardPartRegistry.createDashboardPart(null);
	}
	
	@Test(expectedExceptions = InstantiationException.class, dataProvider = "invalidDashboardPartClasses")
	public void testCreateInvalidDashboardPart(Class<? extends IDashboardPart> dashboardPartClass ) throws Throwable {
		DashboardPartRegistry.register(dashboardPartClass, descriptor);
		
		DashboardPartRegistry.createDashboardPart(descriptor.getName());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testCreateDashboardPartEmptyString() throws Throwable {
		DashboardPartRegistry.createDashboardPart("");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testRegisterNullArgs() {
		DashboardPartRegistry.register(null, null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testRegisterClassTwice() throws Throwable {
		DashboardPartDescriptor desc2 = newDashboardPartDescriptor();
		DashboardPartRegistry.register(TestDashboardPart2.class, desc2);
		DashboardPartRegistry.register(TestDashboardPart2.class, descriptor);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testRegisterDescriptorTwice() throws Throwable {
		DashboardPartRegistry.register(TestDashboardPart2.class, descriptor);
		DashboardPartRegistry.register(TestDashboardPart.class, descriptor);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnregisterNullArgs() {
		DashboardPartRegistry.unregister((String)null);
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testUnregisterByClassNullArgs() {
		DashboardPartRegistry.unregister((Class<? extends IDashboardPart>)null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testIsRegisteredNullArgs() {
		DashboardPartRegistry.isRegistered((String)null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetDashboardPartDescriptorNullArgs() {
		DashboardPartRegistry.getDashboardPartDescriptor(null);
	}
	
	@Test
	public void testGetDashboardPartClass() throws Throwable {
		DashboardPartRegistry.register(dashboardPartClass, descriptor);
		
		Optional<Class<? extends IDashboardPart>> optClass = DashboardPartRegistry.getDashboardPartClass(descriptor.getName());
		assertTrue( optClass.isPresent());
		
		Optional<Class<? extends IDashboardPart>> optClass2 = DashboardPartRegistry.getDashboardPartClass("No one here");
		assertFalse( optClass2.isPresent() );
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetDashboardPartClassNullArgs() throws Throwable {
		DashboardPartRegistry.getDashboardPartClass(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetDashboardPartClassEmptyString() throws Throwable {
		DashboardPartRegistry.getDashboardPartClass("");
	}

	private static DashboardPartDescriptor newDashboardPartDescriptor() {
		SettingDescriptor<Integer> setting1 = newSettingDescriptor("Setting1", "Integer", 100);
		SettingDescriptor<String> setting2 = newSettingDescriptor("Setting2", "String", "Hallo");
		SettingDescriptor<Double> setting3 = newSettingDescriptor("Setting3", "Double", 20.0);

		List<SettingDescriptor<?>> settings = Lists.<SettingDescriptor<?>> newArrayList(setting1, setting2, setting3);

		return new DashboardPartDescriptor("SomeDashboardPart", "Description", settings);
	}

	private static <T> SettingDescriptor<T> newSettingDescriptor(String settingName, String type, T defaultValue) {
		return new SettingDescriptor<T>(settingName, "Description", type, defaultValue, false, true);
	}
	
	@SuppressWarnings("unused")
	@DataProvider
	private static Object[][] invalidDashboardPartClasses() {
		return new Object[][] {
				{ InvalidTestDashboardPart.class},	
				{ InvalidTestDashboardPart2.class},	
		};
	}
}
