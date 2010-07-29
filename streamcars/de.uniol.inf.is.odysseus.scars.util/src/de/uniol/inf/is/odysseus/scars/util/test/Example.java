package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class Example {

	private SDFAttributeList schema;
	private MVRelationalTuple<?> baseTuple;
	
	public Example() {
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
		// - e MV FLOAT
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
		SDFAttribute e = new SDFAttribute(sourceName, "e");
		e.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		base.addSubattribute(a);
		base.addSubattribute(b);
		base.addSubattribute(c);
		base.addSubattribute(d);
		base.addSubattribute(e);
		
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
		
		schema = new SDFAttributeList( new SDFAttribute[] {base} );
		
		// Tupel
		baseTuple = new MVRelationalTuple<IProbability>(5);
		
		MVRelationalTuple<IProbability> tuple_a = new MVRelationalTuple<IProbability>(3);
		baseTuple.setAttribute(0, tuple_a);
		baseTuple.setAttribute(1, (long)100);
		baseTuple.setAttribute(2, (int)20);
		baseTuple.setAttribute(3, (long)1345);
		baseTuple.setAttribute(4, 123.45);
		
		tuple_a.setAttribute(0, "Hallo");
		MVRelationalTuple<IProbability> tuple_a_b = new MVRelationalTuple<IProbability>(10); // die Liste
		tuple_a.setAttribute(1, tuple_a_b);
		tuple_a.setAttribute(2, "Moin");
		
		// Die Liste aufbauen
		for( int i = 0; i < 10; i++ ) 
			tuple_a_b.setAttribute(i, i * 11);
	}
	
	public SDFAttributeList getSchema() {
		return schema;
	}
	
	public MVRelationalTuple<?> getTuple() {
		return baseTuple;
	}
}
