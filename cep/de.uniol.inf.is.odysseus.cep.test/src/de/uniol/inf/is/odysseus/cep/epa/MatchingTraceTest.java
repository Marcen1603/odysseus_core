package de.uniol.inf.is.odysseus.cep.epa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.statemachinefactory.SMF1;
@SuppressWarnings("unchecked")
public class MatchingTraceTest {

	MatchingTrace trace;
	List<State> states;

	@Before
	public void setUp() {
		SMF1 stateMachine = new SMF1();
		states = stateMachine.create(null).getStates();
		this.trace = new MatchingTrace(states);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void test1() {
		/*
		 * Test aller Funktionen bei leerem MatchingTrace
		 */
		assertEquals(this.trace.getLastEvent(), null);

		assertTrue(this.trace.getStateBuffer().size() == 5);
//		for (StateBuffer buffer : this.trace.getStateBuffer()) {
//			assertTrue(buffer != null);
//		}

		for (State state : this.states) {
			assertTrue(this.trace.getStateBuffer(state) != null);
			assertTrue(this.trace.getStateBuffer(state).getState() == state);
		}

		this.trace.setLastEvent(new MatchedEvent(null, new Integer(1)));
		assertTrue(this.trace.getLastEvent() != null);

		this.trace.setLastEvent(null);
		assertTrue(this.trace.getLastEvent() == null);

		this.trace.addEvent(new Integer(1), this.states.get(0));

		assertTrue(this.trace.getLastEvent().getEvent().equals(new Integer(1)));

		for (State state : this.states) {
			if (state.getId().equals(this.states.get(0).getId())) {
				assertTrue(this.trace.getStateBuffer(state).getEvents().size() == 1);
			} else {
				assertTrue(this.trace.getStateBuffer(state).getEvents().size() == 0);
			}
		}

		for (State state : this.states) {
			this.trace.addEvent(new Integer(0), state);
		}

		for (State state : this.states) {
			if (state.getId().equals(this.states.get(0).getId())) {
				assertTrue(this.trace.getStateBuffer(state).getEvents().size() == 2);
			} else {
				assertTrue(this.trace.getStateBuffer(state).getEvents().size() == 1);
			}
		}

		for (int i = 0; i < 5; i++) {
			this.trace.addEvent(new Integer(2), this.states.get(2));
		}

		assertTrue(this.trace.getStateBuffer(this.states.get(2)).getEvents()
				.size() == 6);

		this.trace.addEvent(new Integer(10), this.states.get(1));

		assertTrue(this.trace.getStateBuffer(this.states.get(1)).getEvents()
				.size() == 2);
		assertEquals(this.trace.getLastEvent().getEvent(), new Integer(10));
		assertEquals(this.trace.getLastEvent().getPrevious().getEvent(),
				new Integer(2));
//		assertEquals(this.trace.getStateBuffer(this.states.get(1)).getEvents()
//				.get(
//						this.trace.getStateBuffer(this.states.get(1))
//								.getEvents().size() - 1).getEvent(),
//				new Integer(10));

		// LinkedList<Transition> tList = new LinkedList<Transition>();
		// this.trace.addEvent(new Integer(1), new State("",true, tList));

	}

}
