package de.uniol.inf.is.odysseus.rcp.dashboard.test.desc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
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
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", newEmptySettingDescriptorList());
		assertNotNull(desc);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullArgs() {
		new DashboardPartDescriptor(null, null, null, null);
	}

	@Test
	public void testConstructorEmptyOrNullDescription() {
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", null, newEmptySettingDescriptorList());
		assertFalse( Strings.isNullOrEmpty(desc.getDescription()));
	}
	
	@Test
	public void testGetSettingDescriptors() {
		List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);
		
		ImmutableList<String> names = desc.getSettingDescriptorNames();
		assertEquals( names.size(), settingDescriptors.size());
		
		for( int i = 0; i < names.size(); i++ ) {
			Optional<SettingDescriptor<?>> settingDesc = desc.getSettingDescriptor(settingDescriptors.get(i).getName());
			
			assertTrue(settingDesc.isPresent());
			assertEquals(settingDesc.get(), settingDescriptors.get(i));
		}
	}
	
	@Test
	public void testGetUnknownSettingDescriptor() {
		List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);
		
		assertFalse(desc.hasSettingDescriptor("Waka"));
		
		Optional<SettingDescriptor<?>> settingDesc = desc.getSettingDescriptor("Waka");
		assertFalse(settingDesc.isPresent());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetSettingDescriptorNull() {
		List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);
		
		desc.getSettingDescriptor(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetSettingDescriptorEmptyString() {
		List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);
		
		desc.getSettingDescriptor("");
	}
	
	@Test
	public void testDuplicateNames() {
		List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorListWithDuplicates();
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);
		
		Optional<SettingDescriptor<?>> settingDescriptor = desc.getSettingDescriptor("Setting2");
		
		assertTrue(settingDescriptor.isPresent());
		assertEquals(settingDescriptor.get().getDescription(), "Another Description of setting2");
	}
	
	@Test
	public void testGetter() {
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description");
		
		assertEquals(desc.getDescription(), "Description");
		assertEquals(desc.getName(), "Name");
	}
	
	@Test
	public void testImage() {
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description");
		
		assertFalse( desc.hasImage() );
		assertNull( desc.getImage() );
		
		Image image = newImage();
		desc = new DashboardPartDescriptor("Name", "Description", image);
		
		assertEquals(desc.getImage(), image);
		assertTrue(desc.hasImage());
	}
	
	@Test
	public void testCreateDefaultConfiguration() {
		List<SettingDescriptor<?>> settingDescriptors = newSettingDescriptorList();
		DashboardPartDescriptor desc = new DashboardPartDescriptor("Name", "Description", settingDescriptors);
		
		Configuration configuration = desc.createDefaultConfiguration();
		
		assertNotNull(configuration);
		
		for( SettingDescriptor<?> settingDescriptor : settingDescriptors ) {
			assertEquals(configuration.get(settingDescriptor.getName()), settingDescriptor.getDefaultValue());
		}
	}
	
	private static List<SettingDescriptor<?>> newEmptySettingDescriptorList() {
		return Lists.<SettingDescriptor<?>>newArrayList();
	}
	
	private static List<SettingDescriptor<?>> newSettingDescriptorList() {
		List<SettingDescriptor<?>> settingDescriptors = Lists.newArrayList();
		settingDescriptors.add(new SettingDescriptor<Integer>("Setting1", "Description of setting1", "Integer", 100, true, true));
		settingDescriptors.add(new SettingDescriptor<String>("Setting2", "Description of setting2", "String", "Hallo", false, false));
		return settingDescriptors;
	}
	
	private static List<SettingDescriptor<?>> newSettingDescriptorListWithDuplicates() {
		List<SettingDescriptor<?>> settingDescriptors = Lists.newArrayList();
		settingDescriptors.add(new SettingDescriptor<Integer>("Setting1", "Description of setting1", "Integer", 100, true, true));
		settingDescriptors.add(new SettingDescriptor<String>("Setting2", "Description of setting2", "String", "Hallo", false, false));
		settingDescriptors.add(new SettingDescriptor<String>("Setting2", "Another Description of setting2", "String", "Hallo Again", false, true));
		return settingDescriptors;
	}
	
	private static Image newImage() {
		return new Image(Display.getDefault(), 100, 100);
	}
}
