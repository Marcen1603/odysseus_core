package de.uniol.inf.is.odysseus.action.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.output.StaticParameter;
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

	@Test
	public void testSortParameters() {
		try {
			//test with correctly ordered types & values
			Class<?>[] types = {byte.class, double.class, double.class, int.class};
			Action action = new Action(this.actuator, "doSomething", types);
			
			ArrayList<IActionParameter> parameters = new ArrayList<IActionParameter>();
			Object[] values = {(byte)1, (double)2.0, (double)2.5, (int)3};
			for (Object value : values){
				parameters.add(new StaticParameter(value));
			}
			
			List<IActionParameter> sortedParameters = action.sortParameters(parameters);
			for (int i=0; i<sortedParameters.size();i++){
				assertEquals(sortedParameters.get(i), parameters.get(i));
			}
			
			//test with correctly ordered types but unsorted values
			parameters = new ArrayList<IActionParameter>();
			Object[] values2 = {(int)4, (byte)1, (double)2.5, (double)3.0};
			for (Object value : values2){
				parameters.add(new StaticParameter(value));
			}
			sortedParameters = action.sortParameters(parameters);
			assertEquals(sortedParameters.get(0).getValue(), (byte)1);
			assertEquals(sortedParameters.get(1).getValue(), (double)2.5);
			assertEquals(sortedParameters.get(2).getValue(), (double)3.0);
			assertEquals(sortedParameters.get(3).getValue(), (int)4);
						
			//test with unsorted types & values
			Class<?>[] types2 = {double.class, byte.class,  int.class, double.class};
			action = new Action(this.actuator, "doSomething", types2);
			
			parameters = new ArrayList<IActionParameter>();
			Object[] values3 = {(double)3.0, (byte)1, (int)4, (double)2.5};
			for (Object value : values3){
				parameters.add(new StaticParameter(value));
			}
			sortedParameters = action.sortParameters(parameters);
			assertEquals(sortedParameters.get(0).getValue(), (byte)1);
			assertEquals(sortedParameters.get(1).getValue(), (double)3.0);
			assertEquals(sortedParameters.get(2).getValue(), (double)2.5);
			assertEquals(sortedParameters.get(3).getValue(), (int)4);
		}catch (ActionException e){
			fail();
		}
	}

}
