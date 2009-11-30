package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;
import java.util.Stack;

import org.junit.*;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import static org.junit.Assert.*;
@SuppressWarnings("unchecked")
public class BranchingBufferTest {

	
	Stack<StateMachineInstance> instSet1;
	Stack<StateMachineInstance> instSet2;
	Stack<StateMachineInstance> instSet3;
	Stack<StateMachineInstance> instSet4;
	Stack<StateMachineInstance> instSet5;
	Stack<StateMachineInstance> instSet6;
	BranchingBuffer testBuffer;

	StateMachineInstance tmpInst1 = null;
	StateMachineInstance tmpInst2 = null;
	StateMachineInstance tmpInst3 = null;
	StateMachineInstance tmpInst5 = null;
	StateMachineInstance tmpInst6 = null;

	@Before
	public void setUp() {
		this.instSet1 = new Stack<StateMachineInstance>();
		this.instSet2 = new Stack<StateMachineInstance>();
		this.instSet3 = new Stack<StateMachineInstance>();
		this.instSet4 = new Stack<StateMachineInstance>();
		this.instSet5 = new Stack<StateMachineInstance>();
		this.instSet6 = new Stack<StateMachineInstance>();

		for (int i = 0; i < 100; i++) {
			this.instSet1.push(new StateMachineInstance(new StateMachine()));
			this.instSet2.push(new StateMachineInstance(new StateMachine()));
			this.instSet3.push(new StateMachineInstance(new StateMachine()));
			this.instSet4.push(new StateMachineInstance(new StateMachine()));
			this.instSet5.push(new StateMachineInstance(new StateMachine()));
			this.instSet6.push(new StateMachineInstance(new StateMachine()));
		}

		this.testBuffer = new BranchingBuffer();
	}

	@After
	public void tearDown() {
		this.instSet1 = null;
		this.instSet2 = null;
		this.instSet3 = null;
		this.instSet4 = null;
		this.instSet5 = null;
		this.instSet6 = null;
		this.tmpInst1 = null;
		this.tmpInst1 = null;
		this.tmpInst2 = null;
		this.tmpInst3 = null;
		this.tmpInst5 = null;
		this.tmpInst6 = null;
		this.testBuffer = null;
	}

	/**
	 * Testet alle Methoden für einen leeren Buffer.
	 */
	@Test
	public void testEmptyBuffer() {
		LinkedList<StateMachineInstance> nestedList = this.testBuffer
				.getAllNestedStateMachineInstances(instSet1.pop());
		assertTrue(nestedList != null);
		assertTrue(nestedList.size() == 0);

		this.testBuffer.removeAllNestedBranches(this.instSet1.pop());
		this.testBuffer.removeBranch(this.instSet1.pop());

		this.testBuffer.addBranch(this.instSet1.pop(), this.instSet1.pop());
		LinkedList<StateMachineInstance> instList = new LinkedList<StateMachineInstance>();
		while (!this.instSet2.isEmpty()) {
			instList.add(this.instSet2.pop());
		}
		this.testBuffer.addBranch(this.instSet1.pop(), instList);
	}

	/**
	 * Erstellt mehrere Verzweigungsbäume und entfernt einen wieder.
	 */
	@Test
	public void testRemoveCertainInst() {

		for (int i = 0; i < 10; i++) {
			this.testBuffer
					.addBranch(this.instSet1.pop(), this.instSet1.peek());
			this.testBuffer
					.addBranch(this.instSet2.pop(), this.instSet2.peek());
			this.testBuffer
					.addBranch(this.instSet3.pop(), this.instSet3.peek());
			if (i == 5) {
				tmpInst1 = this.instSet1.peek();
				tmpInst2 = this.instSet2.peek();
				tmpInst3 = this.instSet3.peek();
			}
			if (i == 3)
				tmpInst5 = this.instSet1.peek();
			if (i == 7)
				tmpInst6 = this.instSet1.peek();
		}
		assertTrue(this.testBuffer.getBranches().size() == 3);

		// System.out.println(this.testBuffer.getBranches().get(0).toString("  "));

		this.testBuffer.removeBranch(this.instSet1.peek());
		this.testBuffer.removeBranch(this.instSet2.peek());
		this.testBuffer.removeBranch(this.instSet3.peek());

		this.testBuffer.removeBranch(tmpInst5);
		this.testBuffer.removeBranch(tmpInst6);

		// System.out.println(this.testBuffer.getBranches().get(0).toString("  "));

		assertTrue(this.testBuffer.getBranches().size() == 3);

		this.testBuffer.removeAllNestedBranches(tmpInst1);
		assertTrue(this.testBuffer.getBranches().size() == 2);
		// System.out.println(this.testBuffer.getBranches().get(0).toString("  "));

		this.testBuffer.removeAllNestedBranches(tmpInst2);
		assertTrue(this.testBuffer.getBranches().size() == 1);

		this.testBuffer.removeAllNestedBranches(tmpInst3);
		assertTrue(this.testBuffer.getBranches().size() == 0);

	}

	@Test
	public void testAdd() {
		for (int i = 0; i < 10; i++) {
			this.testBuffer
					.addBranch(this.instSet1.pop(), this.instSet1.peek());
			this.testBuffer
					.addBranch(this.instSet2.pop(), this.instSet2.peek());
			this.testBuffer
					.addBranch(this.instSet3.pop(), this.instSet3.peek());
			if (i == 5) {
				tmpInst1 = this.instSet1.peek();
				tmpInst2 = this.instSet2.peek();
				tmpInst3 = this.instSet3.peek();
			}
			if (i == 3)
				tmpInst5 = this.instSet1.peek();
			if (i == 7)
				tmpInst6 = this.instSet1.peek();
		}

		this.instSet1.pop();
		this.instSet2.pop();
		this.instSet3.pop();

		for (int i = 0; i < 10; i++) {
			this.testBuffer.addBranch(tmpInst5, this.instSet1.pop());
			this.testBuffer.addBranch(tmpInst6, this.instSet1.pop());
		}

		assertTrue(this.testBuffer.getBranches().size() == 3);
		System.out.println(this.testBuffer.getBranches().get(0)+"  ");
		
		this.tmpInst1 = null;
		this.testBuffer.addBranch(this.tmpInst1, this.tmpInst2);
		
		assertTrue(this.testBuffer.getBranches().size() == 4);
		
		this.testBuffer.addBranch(this.tmpInst2, this.tmpInst1);
	}

}
