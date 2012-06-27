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
		return new SettingDescriptor<Integer>("Name", "Description", 100, true, true);
	}
}
