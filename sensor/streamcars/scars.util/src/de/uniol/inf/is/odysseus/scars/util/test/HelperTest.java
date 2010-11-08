package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.scars.util.UtilPrinter;

public class HelperTest {

	public static void main( String[] args ) {
		
		Example ex = new Example();
		
		// Alles ausgeben
		UtilPrinter.printSchema(ex.getSchema());
		System.out.println();
		UtilPrinter.printTuple(ex.getTuple(), ex.getSchema(), "base");
	}
}
