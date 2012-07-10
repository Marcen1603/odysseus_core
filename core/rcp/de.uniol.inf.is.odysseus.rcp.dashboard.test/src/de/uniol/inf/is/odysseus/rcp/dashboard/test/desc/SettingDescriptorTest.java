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

import static org.testng.Assert.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class SettingDescriptorTest {

	@Test
	public void testConstructor() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "Description", "Integer", 100, true, true);
		assertNotNull(desc, "Constructor of SettingDescriptor failed.");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testMissingName() {
		new SettingDescriptor<Integer>(null, "Description", "Integer", 100, true, false);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testEmptyName() {
		new SettingDescriptor<Integer>("", "Description", "Integer",100, true, false);
	}

	@Test
	public void testMissingDescription() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", null, "Integer",100, true, true);
		assertFalse( Strings.isNullOrEmpty(desc.getDescription()));
	}

	@Test
	public void testEmptyDescription() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "", "Integer", 100, true, true);
		assertFalse( Strings.isNullOrEmpty(desc.getDescription()));
	}

	@Test
	public void testGetter() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "Description", "Integer",100, true, true);
		
		assertEquals(desc.getDefaultValue(), (Integer)100, "Default value differs!");
		assertEquals(desc.getDescription(), "Description", "Description differs!");
		assertEquals(desc.getName(), "Name", "Name differs!");
	}
	
	@Test(dataProvider = "settingsDataProvider")
	public void testSettings( boolean optional, boolean editable ) {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "Description", "Integer",100, optional, editable);
		assertEquals(desc.isEditable(), editable, "IsEditable differs!");
		assertEquals(desc.isOptional(), optional, "IsOptional differs!");
	}
	
	@Test
	public void testCreateSetting() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "Description", "Integer",100, true, true);
		
		Setting<Integer> setting = desc.createSetting();
		assertNotNull(setting);
		assertEquals(setting.get(), (Integer)100);
	}
	
	@SuppressWarnings("unused")
	@DataProvider
	private static Object[][] settingsDataProvider() {
		return new Object[][] {
				{true, true},
				{false, true},
				{false, false},
				{true, false}
		};
	}
}
