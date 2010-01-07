package de.uniol.inf.is.odysseus.action.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorAdapterManager;
import de.uniol.inf.is.odysseus.action.output.IActuator;

/**
 * JUnit test for Action from de.uniol.inf.is.odysseus.action bundle
 * @author Simon Flandergan
 *
 */
public class ActionTest {

	private IActuator actuator;

	@Before
	public void setUp() throws Exception {
		ActuatorAdapterManager manager = new ActuatorAdapterManager();
		this.actuator = manager.createActuator("test", "de.uniol.inf.is.odysseus.action.actuatorManagement.impl.TestActuator(name)");
	}

	@Test
	public void testExecuteMethod() {
		fail("Not yet implemented");
	}

	@Test
	public void testSortParameters() {
		fail("Not yet implemented");
	}

}
