package de.uniol.inf.is.odysseus.cep.metamodel.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.statemachinefactory.SMF1;
import de.uniol.inf.is.odysseus.cep.statemachinefactory.SMF2;

public class ImportExportTest {

	ImportExport ie;
	File file;

	@Before
	public void setUp() throws Exception {
		this.ie = new ImportExport();
		this.file = new File("statemachine.xml");
	}

	@After
	public void tearDown() throws Exception {
		this.ie = null;
		this.file = null;
	}

	@Test
	public void test1() throws JAXBException, IOException {
		SMF1 factory = new SMF1();
		StateMachine stm1 = factory.create(null);
		this.ie.saveToFile(stm1, file);

		StateMachine stm2 = this.ie.loadFromFile(file);
		this.compareStateMachines(stm1, stm2);
	}
	
	@Test
	public void test2() throws JAXBException, IOException {
		SMF2 factory = new SMF2();
		StateMachine stm1 = factory.create(null);
		this.ie.saveToFile(stm1, file);

		StateMachine stm2 = this.ie.loadFromFile(file);
		this.compareStateMachines(stm1, stm2);
	}

	private void compareStateMachines(StateMachine stm1, StateMachine stm2) {
		
		assertTrue(stm2 != null);

		for (int i = 0; i < stm1.getStates().size(); i++) {
			System.out.println(stm2.getStates());
			assertEquals(stm1.getStates().get(i).getId(), stm2.getStates().get(
					i).getId());
			for (int k = 0; k < stm1.getStates().get(i).getTransitions().size(); k++) {
				assertEquals(stm1.getStates().get(i).getTransitions().get(k)
						.getId(), stm2.getStates().get(i).getTransitions().get(
						k).getId());
			}
		}
		assertEquals(stm1.getInitialState().getId(), stm2.getInitialState()
				.getId());

		for (int i = 0; i < stm1.getOutputScheme().getEntries().size(); i++) {
			assertEquals(stm1.getOutputScheme().getEntries().get(i).getLabel(),
					stm2.getOutputScheme().getEntries().get(i).getLabel());
		}

		for (int i = 0; i < stm1.getSymTabScheme().getEntries().size(); i++) {
			assertEquals(stm1.getSymTabScheme().getEntries().get(i)
					.getVariableName(), stm2.getSymTabScheme().getEntries()
					.get(i).getVariableName());
			assertEquals(stm1.getSymTabScheme().getEntries().get(i)
					.getOperation().getClass(), stm2.getSymTabScheme()
					.getEntries().get(i).getOperation().getClass());
		}
		
		assertEquals(stm1.getConsumptionMode(), stm2.getConsumptionMode());
	}
}
