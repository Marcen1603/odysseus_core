package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HelperTest2 {

	public static void main(String[] args) {
		
		Example ex = new Example();

		SDFAttributeList schema = ex.getSchema();
		MVRelationalTuple<?> tuple = ex.getTuple();
		
		SchemaHelper helper = new SchemaHelper(schema);
		SchemaIndexPath start = helper.getSchemaIndexPath("base:a:b");
		
		TupleIterator iterator1 = new TupleIterator(tuple, start, 0);
		while( !iterator1.isFinished() ) {
			
			TupleIndexPath tuplePath = iterator1.getTupleIndexPath();
			
			// In der Liste iterieren
			TupleIterator iterator2 = new TupleIterator(tuple, tuplePath);
			while( !iterator2.isFinished() ) {
				System.out.println(iterator2.getTupleObject());
				iterator2.next();
			}
			
			iterator1.next();
		}
		
	}

}
