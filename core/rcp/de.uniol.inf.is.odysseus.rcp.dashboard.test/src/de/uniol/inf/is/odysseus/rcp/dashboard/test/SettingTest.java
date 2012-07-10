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
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

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
	
	private static SettingDescriptor<Integer> newSettingDescriptor() {
		return new SettingDescriptor<Integer>("Name", "Description", "Integer", 100, true, true);
	}
}
