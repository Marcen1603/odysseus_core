package de.uniol.inf.is.odysseus.action.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.services.actuator.ActuatorAdapterManager;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.actuator.impl.TestActuator;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

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
		this.actuator = manager.createActuator("test", "de.uniol.inf.is.odysseus.action.services.actuator.impl.TestActuator(name)");
	}

	@Test
	public void testExecuteMethod() {
		Class<?>[] types = {byte.class, double.class, double.class, int.class};
		try {
			Action action = new Action(this.actuator, "doSomething", types);
			Object[] values = {(byte)1, (double)2.0, (double)2.5, (int)3};
			
			try {
				action.executeMethod(values);
			} catch (ActuatorException e) {
				fail();
			}
			assertEquals(8.5, ((TestActuator)this.actuator).giveSomething(), 0.0);
			
			values[0] = (int)5;
			try {
				action.executeMethod(values);
				fail("Should have raised ActuatorException");
			} catch (ActuatorException e) {}
		} catch (ActionException e) {
			fail();
		}
	}
}
