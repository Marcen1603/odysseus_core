package de.uniol.inf.is.odysseus.rcp.editor.text.templates;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class OdysseusScriptTemplateRegistryTest {
	
	@BeforeMethod
	public void beforeMethod() {
		registry().reset();
	}
	
	@Test
	public void testSingleton() throws Exception {
		OdysseusScriptTemplateRegistry regInstance1 = registry();
		OdysseusScriptTemplateRegistry regInstance2 = registry();
		
		assertEquals(regInstance2, regInstance1);
	}
	
	@Test
	public void testRegister() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
		
		assertIsNotRegistered(template);
		registry().register(template);
		assertIsRegistered(template);
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testRegisterNull() throws Exception {
		registry().register(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testRegisterTwice() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
		
		registry().register(template);
		registry().register(template);
	}
	
	@Test
	public void testUnregister() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
		
		registry().register(template);
		assertIsRegistered(template);
		
		registry().unregister(template);
		assertIsNotRegistered(template);
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testUnregisterNull() throws Exception {
		registry().unregister(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnregisterTwice() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
		
		registry().register(template);
		
		registry().unregister(template);
		registry().unregister(template);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnregisterEmptyTemplate() throws Exception {
		IOdysseusScriptTemplate emptyTemplate = registry().getTemplate(OdysseusScriptTemplateRegistry.EMPTY_TEMPLATE_NAME);
		
		registry().unregister(emptyTemplate);
	}
	
	@Test
	public void testUnregisterAll() throws Exception {
		IOdysseusScriptTemplate template1 = createTemplate();
		IOdysseusScriptTemplate template2 = createTemplate("TestTemplate2");
		IOdysseusScriptTemplate template3 = createTemplate("TestTemplate3");
		
		registry().register(template1);
		registry().register(template2);
		registry().register(template3);
		
		assertIsRegistered(template1);
		assertIsRegistered(template2);
		assertIsRegistered(template3);
		
		registry().unregisterAll();
		
		assertIsNotRegistered(template1);
		assertIsNotRegistered(template2);
		assertIsNotRegistered(template3);
	}

	@Test
	public void testUnregisterAllEmpty() throws Exception {
		registry().unregisterAll();
	}
	
	@Test
	public void testGetTemplateNames() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
		registry().register(template);
		
		ImmutableCollection<String> names = registry().getTemplateNames();
		
		assertNotNull(names);
		assertEquals(names.size(), 2);
	}
	
	@Test
	public void testGetTemplateNamesEmpty() throws Exception {
		ImmutableCollection<String> names = registry().getTemplateNames();
		
		assertNotNull(names);
		assertEquals(names.size(), 1);
	}
	
	@Test
	public void testGetTemplate() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
		registry().register(template);
		
		IOdysseusScriptTemplate recTemplate = registry().getTemplate(template.getName());
		
		assertEquals(recTemplate, template);
		assertEquals(recTemplate.getName(), template.getName());
	}
	
	@Test
	public void testGetTemplateByName() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
		registry().register(template);
		
		IOdysseusScriptTemplate recTemplate = registry().getTemplate(TestOdysseusScriptTemplate.TEMPLATE_NAME);
		
		assertEquals(recTemplate, template);
		assertEquals(recTemplate.getName(), template.getName());	
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetTemplateByNameNull() throws Exception {
		registry().getTemplate("");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetNotRegisteredTemplate() throws Exception {
		IOdysseusScriptTemplate template = createTemplate();
				
		registry().getTemplate(template.getName());
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testGetTemplateNull() throws Exception {
		registry().getTemplate(null);
	}
	
	private static OdysseusScriptTemplateRegistry registry() {
		return OdysseusScriptTemplateRegistry.getInstance();
	}
	
	private static IOdysseusScriptTemplate createTemplate() {
		return new TestOdysseusScriptTemplate();
	}
	
	private static IOdysseusScriptTemplate createTemplate(final String name) {
		return new TestOdysseusScriptTemplate() {
			@Override
			public String getName() {
				return name;
			}
		};
	}

	private void assertIsRegistered(IOdysseusScriptTemplate template) {
		assertTrue(registry().isRegistered(template));
	}

	private void assertIsNotRegistered(IOdysseusScriptTemplate template) {
		assertFalse(registry().isRegistered(template));
	}
}
