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
package de.uniol.inf.is.odysseus.rcp.dashboard.test.desc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class DashboardPartDescriptorTest {

	@Test
	public void testConstructor() {
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", newEmptySettingDescriptorList());
		assertNotNull(desc);
	}

	@Test
	public void testConstructorEmptyOrNullDescription() {
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", null, newEmptySettingDescriptorList());
		assertFalse(Strings.isNullOrEmpty(desc.getDescription()));
	}

	@Test(expectedExceptions = { IllegalArgumentException.class, NullPointerException.class })
	public void testConstructorNullArgs() {
		new DashboardPartDescriptor(null, null, null);
	}

	@Test
	public void testCreateDefaultConfiguration() {
		final List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);

		final Configuration configuration = desc.createDefaultConfiguration();

		assertNotNull(configuration);

		for (final SettingDescriptor<?> settingDescriptor : settingDescriptors) {
			assertEquals(configuration.get(settingDescriptor.getName()), settingDescriptor.getDefaultValue());
		}
	}

	@Test
	public void testDuplicateNames() {
		final List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorListWithDuplicates();
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);

		final Optional<SettingDescriptor<?>> settingDescriptor = desc.getSettingDescriptor("Setting2");

		assertTrue(settingDescriptor.isPresent());
		assertEquals(settingDescriptor.get().getDescription(), "Another Description of setting2");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testEmptyNameConstructor() throws Throwable {
		new DashboardPartDescriptor("", "Description");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetSettingDescriptorEmptyString() {
		final List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);

		desc.getSettingDescriptor("");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetSettingDescriptorNull() {
		final List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);

		desc.getSettingDescriptor(null);
	}

	@Test
	public void testGetSettingDescriptors() {
		final List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);

		final ImmutableList<String> names = desc.getSettingDescriptorNames();
		assertEquals(names.size(), settingDescriptors.size());

		for (int i = 0; i < names.size(); i++) {
			final Optional<SettingDescriptor<?>> settingDesc = desc.getSettingDescriptor(settingDescriptors.get(i).getName());

			assertTrue(settingDesc.isPresent());
			assertEquals(settingDesc.get(), settingDescriptors.get(i));
		}
	}

	@Test
	public void testGetter() {
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description");

		assertEquals(desc.getDescription(), "Description");
		assertEquals(desc.getName(), "Name");
	}

	@Test
	public void testGetUnknownSettingDescriptor() {
		final List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);

		assertFalse(desc.hasSettingDescriptor("Waka"));

		final Optional<SettingDescriptor<?>> settingDesc = desc.getSettingDescriptor("Waka");
		assertFalse(settingDesc.isPresent());
	}

	@Test
	public void testSmallConstructor() throws Throwable {
		final DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description");
		assertNotNull(desc);
	}

	private static List<SettingDescriptor<?>> newEmptySettingDescriptorList() {
		return Lists.<SettingDescriptor<?>> newArrayList();
	}

	private static List<SettingDescriptor<?>> newSettingDescriptorList() {
		final List<SettingDescriptor<?>> settingDescriptors = Lists.newArrayList();
		settingDescriptors.add(new SettingDescriptor<Integer>("Setting1", "Description of setting1", "Integer", 100, true, true));
		settingDescriptors.add(new SettingDescriptor<String>("Setting2", "Description of setting2", "String", "Hallo", false, false));
		return settingDescriptors;
	}

	private static List<SettingDescriptor<?>> newSettingDescriptorListWithDuplicates() {
		final List<SettingDescriptor<?>> settingDescriptors = Lists.newArrayList();
		settingDescriptors.add(new SettingDescriptor<Integer>("Setting1", "Description of setting1", "Integer", 100, true, true));
		settingDescriptors.add(new SettingDescriptor<String>("Setting2", "Description of setting2", "String", "Hallo", false, false));
		settingDescriptors.add(new SettingDescriptor<String>("Setting2", "Another Description of setting2", "String", "Hallo Again", false, true));
		return settingDescriptors;
	}
}
