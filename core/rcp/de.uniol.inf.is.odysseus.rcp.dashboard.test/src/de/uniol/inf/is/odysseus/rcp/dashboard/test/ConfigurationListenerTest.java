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

package de.uniol.inf.is.odysseus.rcp.dashboard.test;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.IConfigurationListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class ConfigurationListenerTest {

	private Configuration configuration;
	private Map<String, Setting<?>> settingMap;

	@BeforeTest
	public void createSettingMap() {
		SettingDescriptor<Integer> intSetting = new SettingDescriptor<Integer>("IntSetting", "Description", "Integer", 100, true, true );
		SettingDescriptor<String> strSetting = new SettingDescriptor<String>("StrSetting", "Description 2", "String", "Default", true, false);
		
		settingMap = Maps.newHashMap();
		settingMap.put(intSetting.getName(), intSetting.createSetting());
		settingMap.put(strSetting.getName(), strSetting.createSetting());
		
		configuration = new Configuration(settingMap);
	}
	
	@Test
	public void testConfigurationListener() throws Throwable {
		IConfigurationListener listener = new IConfigurationListener() {
			
//			@Mock(invocations = 1)
			@Override
			public void settingChanged(String settingName, Object oldValue, Object newValue) {
				assertEquals(settingName, "IntSetting");
				assertEquals(oldValue, 100);
				assertEquals(newValue, 200);
			}
		};
		
		configuration.addListener(listener);
		configuration.set("IntSetting", 200);
		configuration.removeListener(listener);
		
		configuration.set("IntSetting", 300);
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testAddListenerNullArgs() throws Throwable {
		configuration.addListener(null);
	}
	
	@Test
	public void testRemoveListenerNullArgs() throws Throwable {
		configuration.removeListener(null);
	}
	
}
