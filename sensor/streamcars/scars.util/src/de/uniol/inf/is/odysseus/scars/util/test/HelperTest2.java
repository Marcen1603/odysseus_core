package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HelperTest2 {

	public static void main(String[] args) {
		
		Example ex = new Example();

		SDFAttributeList schema = ex.getSchema();
		MVRelationalTuple<?> tuple = ex.getTuple();
		
		SchemaHelper helper = new SchemaHelper(schema);
		SchemaIndexPath start = helper.getSchemaIndexPath("base:a:b");
		
		for( TupleInfo info : new TupleIterator(tuple, start, 0) ) {
			
			TupleIndexPath tuplePath = info.tupleIndexPath;
			// In der Liste iterieren
			for( TupleInfo info2 : tuplePath ) {
				System.out.println(info2.tupleObject);
			}
		}
		
	}

}
