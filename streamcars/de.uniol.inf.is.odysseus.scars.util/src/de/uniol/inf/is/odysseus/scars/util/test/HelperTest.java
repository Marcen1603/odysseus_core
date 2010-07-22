package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.TuplePrinter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class HelperTest {

	public static void main( String[] args ) {
		final String sourceName = "source";
		
		// Schema erstellen:
		
		// base RECORD
		// - a RECORD
		//   - a STRING
		//   - b LIST
		//     - a INTEGER
		//   - c STRING
		// - b LONG
		// - c INTEGER
		// - d LONG
		SDFAttribute base = new SDFAttribute(sourceName, "base");
		base.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		SDFAttribute a = new SDFAttribute(sourceName, "a");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		SDFAttribute b = new SDFAttribute(sourceName, "b");
		b.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute c = new SDFAttribute(sourceName, "c");
		c.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		SDFAttribute d = new SDFAttribute(sourceName, "d");
		d.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		base.addSubattribute(a);
		base.addSubattribute(b);
		base.addSubattribute(c);
		base.addSubattribute(d);
		
		SDFAttribute aa = new SDFAttribute(sourceName, "a"); // a.a
		aa.setDatatype(SDFDatatypeFactory.getDatatype("String"));
		SDFAttribute ab = new SDFAttribute(sourceName, "b"); // a.b
		ab.setDatatype(SDFDatatypeFactory.getDatatype("List"));
		SDFAttribute ac = new SDFAttribute(sourceName, "c"); // a.c
		ac.setDatatype(SDFDatatypeFactory.getDatatype("String"));
		a.addSubattribute(aa);
		a.addSubattribute(ab);
		a.addSubattribute(ac);
		
		SDFAttribute aba = new SDFAttribute(sourceName, "a"); // a.b.a
		aba.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		ab.addSubattribute(aba);
		
		SDFAttributeList schema = new SDFAttributeList( new SDFAttribute[] {base} );
		
		// Tupel
		MVRelationalTuple<IProbability> baseTuple = new MVRelationalTuple<IProbability>(1);
		MVRelationalTuple<IProbability> tuple = new MVRelationalTuple<IProbability>(4);
		baseTuple.setAttribute(0, tuple);
		
		MVRelationalTuple<IProbability> tuple_a = new MVRelationalTuple<IProbability>(3);
		tuple.setAttribute(0, tuple_a);
		tuple.setAttribute(1, (long)100);
		tuple.setAttribute(2, (int)20);
		tuple.setAttribute(3, (long)1345);
		
		tuple_a.setAttribute(0, "Hallo");
		MVRelationalTuple<IProbability> tuple_a_b = new MVRelationalTuple<IProbability>(10); // die Liste
		tuple_a.setAttribute(1, tuple_a_b);
		tuple_a.setAttribute(2, "Moin");
		
		// Die Liste aufbauen
		for( int i = 0; i < 10; i++ ) 
			tuple_a_b.setAttribute(i, i * 11);
		
		// Alles ausgeben
		TuplePrinter.printSchema(schema, "base");
		System.out.println();
		TuplePrinter.printTuple(baseTuple, schema, "base");
	}
}
