package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.scars.util.TuplePrinter;

public class HelperTest {

	public static void main( String[] args ) {
		
		Example ex = new Example();
		
		// Alles ausgeben
		TuplePrinter.printSchema(ex.getSchema());
		System.out.println();
		TuplePrinter.printTuple(ex.getTuple(), ex.getSchema(), "base");
	}
}
