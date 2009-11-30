package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;

import org.junit.*;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Count;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import static org.junit.Assert.*;
@SuppressWarnings("unchecked")
public class SymbolTableTest {

	private SymbolTable symTab;
	private SymbolTableScheme scheme;

	@Before
	public void setUp() {
		this.scheme = new SymbolTableScheme();
		this.scheme.getEntries().add(
				new CepVariable(1, "state1", 1, "attribute1",
						new Write()));
		this.scheme.getEntries().add(
				new CepVariable(2, "state2", -1, "attribute1",
						new Count()));
		this.symTab = new SymbolTable(this.scheme);
	}

	@After
	public void tearDown() {
		this.scheme = null;
		this.symTab = null;
	}

	/**
	 * Erzeugt eine Liste mit relationalen tupeln. tupel entsprechen schema von
	 * generateEventScheme(). Tupel enthalten Integer Objekte als Attribute
	 * 
	 * @param number
	 * @return
	 */
	@SuppressWarnings("unused")
	private LinkedList<RelationalTuple> generateEvents(int number) {
		LinkedList<RelationalTuple> tuples = new LinkedList<RelationalTuple>();
		for (int i = 0; i < number; i++) {
			Object[] attributes = new Object[2];
			attributes[0] = new Integer(i);
			attributes[1] = new Integer(100 + i);
			tuples.add(new RelationalTuple(attributes));
		}

		return tuples;
	}

	/**
	 * erzeugt ein relationales schema bestehend aus 2 attributen
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private SDFAttributeList generateEventScheme() {
		SDFAttributeList list = new SDFAttributeList();
		list.add(new SDFAttribute("attribute1"));
		list.add(new SDFAttribute("attribute2"));
		return list;
	}

	@Test
	public void test() {
//		assertTrue(this.symTab.getValue("state1_1_attribute1") == null);
//		assertTrue(this.symTab.getValue("state2__attribute1") == null);
//		assertTrue(this.symTab.getValue("not_1_existingname") == null);
//		assertTrue(this.symTab.getValue("123____:invaild_name") == null);
//
//		this.symTab.getEntries().get(0).setValue(new Integer(1));
//		this.symTab.getEntries().get(1).setValue(new Integer(2));
//
//		assertTrue(this.symTab.getValue("state1_1_attribute1") != null);
//		assertTrue(this.symTab.getValue("state2__attribute1") != null);
//		assertTrue(this.symTab.getValue("not_1_existingname") == null);
//		assertTrue(this.symTab.getValue("123____:invaild_name") == null);
//
//		assertEquals(new Integer(1), this.symTab
//				.getValue("state1_1_attribute1"));
//		assertEquals(new Integer(2), this.symTab.getValue("state2__attribute1"));
	}
	
//	@Test
//	public void test2() {
//		RelationalReader reader = new RelationalReader(this
//				.generateEventScheme());
//		LinkedList<RelationalTuple> events = this.generateEvents(100);
//		int i = 0;
//		for (RelationalTuple event : events) {
//			for (SymbolTableEntry entry : this.symTab.getEntries()) {
//				entry.executeOperation(event, reader);
//				if (entry.getScheme().getEventIdentifier().equals("state1"))
//					assertEquals(new Integer(i), entry.getValue());
//				if (entry.getScheme().getEventIdentifier().equals("state2"))
//					assertEquals(new Integer(i+1), entry.getValue());
//			}
//			i++;
//		}
//	}
}
