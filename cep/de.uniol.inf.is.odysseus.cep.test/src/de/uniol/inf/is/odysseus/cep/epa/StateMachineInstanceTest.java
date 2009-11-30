package de.uniol.inf.is.odysseus.cep.epa;

import org.junit.*;
import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndefinedActionException;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.statemachinefactory.SMF1;
@SuppressWarnings("unchecked")
public class StateMachineInstanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		SMF1 factory = new SMF1();
		StateMachine stm = factory.create(null);
		StateMachineInstance instance = new StateMachineInstance(stm);

		assertTrue(instance != null);

		assertEquals(stm.getInitialState(), instance.getCurrentState());

		assertTrue(instance.getMatchingTrace() != null);
		assertTrue(instance.getSymTab() != null);

		instance.setCurrentState(stm.getStates().getLast());
		assertEquals(stm.getStates().getLast(), instance.getCurrentState());

		StateMachineInstance cloned = instance.clone();

		assertTrue(cloned != null);
		assertFalse(cloned == instance);

		try {
			instance.executeAction(null, null, null);
			fail("Exception erwartet");
		} catch (UndefinedActionException e) {
			assertTrue(e instanceof UndefinedActionException);
			// e.printStackTrace();
		}

		try {
			instance.executeAction(EAction.discard, null, null);
		} catch (UndefinedActionException e) {
		}

		try {
			instance.executeAction(EAction.consume, null, null);
		} catch (UndefinedActionException e) {
		}

		try {
			instance.executeAction(EAction.consume, new Integer(1), null);
		} catch (UndefinedActionException e) {
		}

		// TODO weiter testen! executeAction() mit komplexeren Automaten testen,
		// der Symboltabellen-Einträge und Ausgabeschema-Einträge hat.
	}

}
