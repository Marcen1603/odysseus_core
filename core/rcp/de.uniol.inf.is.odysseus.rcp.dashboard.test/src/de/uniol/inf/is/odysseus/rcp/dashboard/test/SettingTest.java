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

import static org.testng.Assert.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;
import de.uniol.inf.is.odysseus.testng.TestUtil;

public class SettingTest {

	@Test
	public void testConstructor() {
		Setting<Integer> setting = new Setting<Integer>(newSettingDescriptor());
		assertNotNull(setting, "Constructor fails!");
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testConstructorNullArgs() {
		new Setting<Integer>(null);
	}
	
	@Test
	public void testSetAndGet() {
		Setting<Integer> setting = new Setting<Integer>(newSettingDescriptor());
		assertEquals(setting.get(), (Integer)100, "Startvalue differs!");
		
		setting.set(200);
		assertEquals(setting.get(), (Integer)200, "Value after set differs!");
		
		setting.reset();
		assertEquals(setting.get(), (Integer)100, "Value after reset differs from default value!");
	}
	
	@Test(dataProvider = "convertValueDataProvider")
	public void testConvertValue(String value, String type, Object realValue) throws Throwable {
		Object result = TestUtil.invoke("convertValue", Setting.class, value, type);
		
		if( realValue != null ) {
			assertEquals(result.getClass(), realValue.getClass());
			assertEquals(result, realValue);
		} else {
			assertNull(result);
		}
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConvertValueUnknownType() throws Throwable {
		TestUtil.invoke("convertValue", Setting.class, "100", "SomeUnknownType");
	}
	
	@SuppressWarnings("unused")
	@DataProvider
	private static Object[][] convertValueDataProvider() {
		return new Object[][] {
				{ "100", "Integer", 100},	
				{ "100", "Long", 100L},	
				{ "100.0", "Double", 100.0},	
				{ "true", "Boolean", true},	
				{ "100", "Float", 100.0f},	
				{ "Moin", "String", "Moin"},	
				{ null, "String", null},
				{ null, "Integer", null},
		};
	}
	
	private static SettingDescriptor<Integer> newSettingDescriptor() {
		return new SettingDescriptor<Integer>("Name", "Description", "Integer", 100, true, true);
	}
}
