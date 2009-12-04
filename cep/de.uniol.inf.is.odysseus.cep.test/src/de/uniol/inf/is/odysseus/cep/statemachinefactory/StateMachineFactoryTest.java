package de.uniol.inf.is.odysseus.cep.statemachinefactory;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

public class StateMachineFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test() {
		SMF1 creator = new SMF1();
		StateMachine stm = creator.create(null);
		
		System.out.println(stm.toString());
	}

}
