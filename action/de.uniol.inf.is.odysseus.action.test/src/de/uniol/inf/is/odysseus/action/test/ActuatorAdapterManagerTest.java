package de.uniol.inf.is.odysseus.action.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorAdapterManager;
import de.uniol.inf.is.odysseus.action.actuatorManagement.impl.TestActuator;
import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.IActuator;

/**
 * JUnit test for ActuatorAdapter from de.uniol.inf.is.odysseus.action bundle.
 * @author Simon Flandergan
 *
 */
public class ActuatorAdapterManagerTest {

	@Before
	public void setUp() throws Exception {
	}


	@Test
	public void testCreateActuator() {
		ActuatorAdapterManager manager = new ActuatorAdapterManager();
		
		//testcase 1
		try {
			IActuator actuator = manager.createActuator("test", "de.uniol.inf.is.odysseus.action.actuatorManagement.impl.TestActuator(name)");
			assertEquals(TestActuator.class, actuator.getClass());
			assertEquals("name", ((TestActuator)actuator).getName());
		} catch (ActuatorException e) {
			fail(e.getMessage());
		}
		
		//testcase 2
		try {
			IActuator actuator = manager.createActuator("test2", "de.uniol.inf.is.odysseus.action.actuatorManagement.impl.TestActuator(name, 10:byte)");
			assertEquals(TestActuator.class, actuator.getClass());
			assertEquals("name", ((TestActuator)actuator).getName());
			assertEquals(10, ((TestActuator)actuator).getAdress());
		} catch (ActuatorException e) {
			fail(e.getMessage());
		}
		
		//testcase 3
		try {
			manager.createActuator("test3", "TestActuator()");
			fail("Should have raised ClassNotFoundException.");
		}catch (ActuatorException e){}
		
		//testcase 4
		try {
			manager.createActuator(" ", "de.uniol.inf.is.odysseus.action.actuatorManagement.impl.TestActuator(name");
			fail("Should have raised empty Name exception");
		}catch(ActuatorException e){}
		
		//testcase 5
		try {
			manager.createActuator("test4", "de.uniol.inf.is.odysseus.action.actuatorManagement.impl.TestActuator(");
			fail("Should have raised undefined Constructor exception");
		}catch(ActuatorException e){}
	
		
	}

}
