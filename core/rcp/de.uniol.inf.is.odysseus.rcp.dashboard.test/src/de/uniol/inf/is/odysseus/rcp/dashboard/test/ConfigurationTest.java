package de.uniol.inf.is.odysseus.rcp.dashboard.test;

import static org.testng.Assert.*;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class ConfigurationTest {

	private Map<String, Setting<?>> settingMap;
	
	@BeforeTest
	public void createSettingMap() {
		SettingDescriptor<Integer> intSetting = new SettingDescriptor<Integer>("IntSetting", "Description", 100, true, true );
		SettingDescriptor<String> strSetting = new SettingDescriptor<String>("StrSetting", "Description 2", "Default", true, false);
		
		settingMap = Maps.newHashMap();
		settingMap.put(intSetting.getName(), intSetting.createSetting());
		settingMap.put(strSetting.getName(), strSetting.createSetting());
		
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
		Configuration config = new Configuration(settingMap);
		
		assertEquals(config.get("IntSetting"), (Integer)100);
		
		config.set("IntSetting", 200);
		
		assertEquals(config.get("IntSetting"), (Integer)200);
		
		config.reset("IntSetting");
		
		assertEquals(config.get("IntSetting"), (Integer)100);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetUnknownSetting() {
		Configuration config = new Configuration(settingMap);
		
		config.get("UnknownSetting");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetUnknownSetting() {
		Configuration config = new Configuration(settingMap);
		
		config.set("UnknownSetting", 1000);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testResetUnknownSetting() {
		Configuration config = new Configuration(settingMap);
		
		config.reset("UnknownSetting");
	}
	
	@Test
	public void testGetNames() {
		Configuration config = new Configuration(settingMap);
		
		ImmutableList<String> settingNames = config.getNames();
		assertEquals(settingNames.size(), 2);
		
		assertTrue( settingNames.contains("IntSetting"));
		assertTrue( settingNames.contains("StrSetting"));
	}
	
	@Test
	public void testResetAll() {
		Configuration config = new Configuration(settingMap);

		config.set("IntSetting", 200);
		config.set("StrSetting", "Moin");
		
		config.resetAll();
		
		assertEquals(config.get("IntSetting"), 100);
		assertEquals(config.get("StrSetting"), "Default");
	}
}
