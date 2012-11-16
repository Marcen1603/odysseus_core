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
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class ConfigurationTest {

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
	public void testConstructor() {
		assertNotNull( new Configuration(settingMap));
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testConstructorNullArgs() {
		new Configuration(null);
	}

	@Test
	public void testGetAndSet() {
		assertEquals(configuration.get("IntSetting"), (Integer)100);
		
		configuration.set("IntSetting", 200);
		
		assertEquals(configuration.get("IntSetting"), (Integer)200);
		
		configuration.reset("IntSetting");
		
		assertEquals(configuration.get("IntSetting"), (Integer)100);
	}
	
	@Test
	public void testSetTwice() throws Throwable {
		configuration.set("IntSetting", 100);
		configuration.set("IntSetting", 100);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetUnknownSetting() {
		configuration.get("UnknownSetting");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetUnknownSetting() {
		configuration.set("UnknownSetting", 1000);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetNullArgs() throws Throwable {
		configuration.set(null, 1000);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetEmptyArgs() throws Throwable {
		configuration.set("", 1000);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetNullArgs() throws Throwable {
		configuration.get(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetEmptyArgs() throws Throwable {
		configuration.get("");
	}
	
	@Test
	public void testResetTwice() throws Throwable {
		configuration.set("IntSetting", 1000);
		
		configuration.reset("IntSetting");
		configuration.reset("IntSetting");

		assertEquals(configuration.get("IntSetting"), 100);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testResetUnknownSetting() {
		configuration.reset("UnknownSetting");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testResetNullArg() throws Throwable {
		configuration.reset(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testResetEmptyArgs() throws Throwable {
		configuration.reset("");
	}
	
	@Test
	public void testGetNames() {
		ImmutableList<String> settingNames = configuration.getNames();
		assertEquals(settingNames.size(), 2);
		
		assertTrue( settingNames.contains("IntSetting"));
		assertTrue( settingNames.contains("StrSetting"));
	}
	
	@Test
	public void testResetAll() {
		configuration.set("IntSetting", 200);
		configuration.set("StrSetting", "Moin");
		
		configuration.resetAll();
		
		assertEquals(configuration.get("IntSetting"), 100);
		assertEquals(configuration.get("StrSetting"), "Default");
	}
	
	@Test
	public void testSetAsString() throws Throwable {
		configuration.setAsString("IntSetting", "111");
		assertEquals(configuration.get("IntSetting"), 111);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetAsStringUnknownSetting() throws Throwable {
		configuration.setAsString("A", "B");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetAsStringNullArgs() throws Throwable {
		configuration.setAsString(null, "1000");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetAsStringEmptyArgs() throws Throwable {
		configuration.setAsString("", "1000");
	}
	
	@Test
	public void testSetAsStringTwice() throws Throwable {
		configuration.setAsString("IntSetting", "111");
		configuration.setAsString("IntSetting", "111");
		assertEquals(configuration.get("IntSetting"), 111);		
	}
	
	@Test
	public void testExists() throws Throwable {
		assertTrue(configuration.exists("IntSetting"));
		assertFalse(configuration.exists("Waka"));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testExistsNullArgs() throws Throwable {
		configuration.exists(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testExistsEmptyArgs() throws Throwable {
		configuration.exists("");
	}
	
	@Test
	public void testGetSettings() throws Throwable {
		ImmutableList<Setting<?>> settings = configuration.getSettings();
		assertNotNull(settings);
		assertFalse(settings.isEmpty());
		assertEquals(settings.size(), settingMap.size());
	}
	
	@Test
	public void testToString() throws Throwable {
		assertFalse( Strings.isNullOrEmpty(configuration.toString()));
	}
}
