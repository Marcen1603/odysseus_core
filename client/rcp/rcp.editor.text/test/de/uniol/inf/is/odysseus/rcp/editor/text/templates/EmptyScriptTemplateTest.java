package de.uniol.inf.is.odysseus.rcp.editor.text.templates;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class EmptyScriptTemplateTest {
	
	@Test
	public void testNameEquality() throws Exception {
		IOdysseusScriptTemplate template = new EmptyScriptTemplate();

		assertEquals(template.getName(), OdysseusScriptTemplateRegistry.EMPTY_TEMPLATE_NAME);
	}
}
