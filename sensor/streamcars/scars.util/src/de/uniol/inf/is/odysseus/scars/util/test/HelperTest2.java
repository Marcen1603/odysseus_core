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
import de.uniol.inf.is.odysseus.scars.util.helper.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.helper.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.helper.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.helper.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.helper.TupleIterator;
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
