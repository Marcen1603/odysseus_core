package de.uniol.inf.is.odysseus.rcp.dashboard.test.desc;

import static org.testng.Assert.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class SettingDescriptorTest {

	@Test
	public void testConstructor() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "Description", 100, true, true);
		assertNotNull(desc, "Constructor of SettingDescriptor failed.");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testMissingName() {
		new SettingDescriptor<Integer>(null, "Description", 100, true, false);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testEmptyName() {
		new SettingDescriptor<Integer>("", "Description", 100, true, false);
	}

	@Test
	public void testMissingDescription() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", null, 100, true, true);
		assertFalse( Strings.isNullOrEmpty(desc.getDescription()));
	}

	@Test
	public void testEmptyDescription() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "", 100, true, true);
		assertFalse( Strings.isNullOrEmpty(desc.getDescription()));
	}

	@Test
	public void testGetter() {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "Description", 100, true, true);
		
		assertEquals(desc.getDefaultValue(), (Integer)100, "Default value differs!");
		assertEquals(desc.getDescription(), "Description", "Description differs!");
		assertEquals(desc.getName(), "Name", "Name differs!");
	}
	
	@Test(dataProvider = "settingsDataProvider")
	public void testSettings( boolean optional, boolean editable ) {
		SettingDescriptor<Integer> desc = new SettingDescriptor<Integer>("Name", "Description", 100, optional, editable);
		assertEquals(desc.isEditable(), editable, "IsEditable differs!");
		assertEquals(desc.isOptional(), optional, "IsOptional differs!");
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
