package de.uniol.inf.is.odysseus.nlp.test.datastructure;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.nlp.datastructure.ToolkitFactory;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPToolkitNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;

public class ToolkitFactoryTest {

	@Before
	public void setUp() throws Exception {
		ToolkitFactory.register("test", NLPToolkit.class);
	}

	@Test
	public void testGet() throws NLPToolkitNotFoundException {
		ToolkitFactory.get("test");
	}
	
	@Test(expected=NLPToolkitNotFoundException.class)
	public void testGetFailed() throws NLPToolkitNotFoundException {
		ToolkitFactory.get("notfound");
	}

}
