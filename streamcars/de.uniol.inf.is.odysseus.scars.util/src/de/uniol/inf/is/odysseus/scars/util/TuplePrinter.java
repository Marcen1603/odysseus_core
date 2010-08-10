package de.uniol.inf.is.odysseus.scars.util;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TuplePrinter {

	public static void printTuple( MVRelationalTuple<?> tuple, SDFAttributeList schema, String attributeName ) {
		SchemaHelper pathHelper = new SchemaHelper(schema);
		
		SchemaIndexPath pathToSomeList = pathHelper.getSchemaIndexPath(attributeName);
		
		TupleIterator iterator = new TupleIterator(tuple, pathToSomeList);
		
		while( !iterator.isFinished() ) {
			
			// Tabs für Hierarchie
			for( int i = 0; i < iterator.getLevel(); i++ ) 
				System.out.print("\t");
			
			// Infos ausgeben
			Object obj = iterator.getTupleObject();
			if( obj instanceof MVRelationalTuple ) {
				System.out.print("TUPLE");
			} else {
				System.out.print(obj);
			}
			System.out.print(" (" + iterator.getAttribute().getAttributeName() + " : " + iterator.getAttribute().getDatatype().getQualName() + ")");
			System.out.print(iterator.getTupleIndexPath());
			System.out.print(iterator.getSchemaIndexPath());
			System.out.println(iterator.getSchemaIndexPath().getFullAttributeName());

			iterator.next();
		}
	}
	
	public static void printSchema(SDFAttributeList schema, String startAttributeName) {
		SchemaIterator iterator = new SchemaIterator(schema);

		while (!iterator.isFinished()) {
			for (int i = 0; i < iterator.getLevel(); i++)
				System.out.print("\t");

			System.out.print(iterator.getAttribute().getAttributeName());
			System.out.print(" (" + iterator.getAttribute().getDatatype().getQualName() + ")");
			System.out.println(iterator.getSchemaIndexPath());

			iterator.next();
		}
	}

}
