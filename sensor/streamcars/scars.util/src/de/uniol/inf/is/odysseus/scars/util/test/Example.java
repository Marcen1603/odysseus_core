/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

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
		
		
		SDFAttributeList subschema_base = new SDFAttributeList();
		
		SDFAttribute a = new SDFAttribute(sourceName, "a");
		SDFAttributeList subschema_a = new SDFAttributeList();
		SDFAttribute aa = new SDFAttribute(sourceName, "a"); // a.a
		aa.setDatatype(GlobalState.getActiveDatadictionary().getDatatype("String"));
		
		SDFAttribute ab = new SDFAttribute(sourceName, "b"); // a.b
		SDFAttributeList subschema_ab = new SDFAttributeList();
		SDFAttribute aba = new SDFAttribute(sourceName, "a");
		aba.setDatatype(GlobalState.getActiveDatadictionary().getDatatype("Integer"));
		subschema_ab.add(aba);
		ab.setDatatype(new SDFDatatype(null, SDFDatatype.KindOfDatatype.SET, subschema_ab));
		
		
		
		SDFAttribute ac = new SDFAttribute(sourceName, "c"); // a.c
		ac.setDatatype(GlobalState.getActiveDatadictionary().getDatatype("String"));
		subschema_a.add(aa);
		subschema_a.add(ab);
		subschema_a.add(ac);
		SDFDatatype anonymous_a = new SDFDatatype(null, SDFDatatype.KindOfDatatype.TUPLE, subschema_a);
		a.setDatatype(anonymous_a);
		
		SDFAttribute b = new SDFAttribute(sourceName, "b");
		b.setDatatype(GlobalState.getActiveDatadictionary().getDatatype("StartTimestamp"));		
		SDFAttribute c = new SDFAttribute(sourceName, "c");
		c.setDatatype(GlobalState.getActiveDatadictionary().getDatatype("Integer"));
		SDFAttribute d = new SDFAttribute(sourceName, "d");
		d.setDatatype(GlobalState.getActiveDatadictionary().getDatatype("EndTimestamp"));
		SDFAttribute e = new SDFAttribute(sourceName, "e");
		e.setDatatype(GlobalState.getActiveDatadictionary().getDatatype("MV Float"));
		
		subschema_base.add(a);
		subschema_base.add(b);
		subschema_base.add(c);
		subschema_base.add(d);
		subschema_base.add(e);
		
		SDFDatatype anonymous_base = new SDFDatatype(null, SDFDatatype.KindOfDatatype.TUPLE, subschema_base);
		
		base.setDatatype(anonymous_base);
		
		schema = new SDFAttributeList( new SDFAttribute[] {base} );
		
		// Tupel
		baseTuple = new MVRelationalTuple<IProbability>(1);
		MVRelationalTuple<IProbability> tuple = new MVRelationalTuple<IProbability>(5);
		baseTuple.setAttribute(0, tuple);
		
		MVRelationalTuple<IProbability> tuple_a = new MVRelationalTuple<IProbability>(3);
		tuple.setAttribute(0, tuple_a);
		tuple.setAttribute(1, (long)100);
		tuple.setAttribute(2, (int)20);
		tuple.setAttribute(3, (long)1345);
		tuple.setAttribute(4, 123.45);
		
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
