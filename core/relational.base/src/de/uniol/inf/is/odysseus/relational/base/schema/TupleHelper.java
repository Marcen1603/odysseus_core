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
package de.uniol.inf.is.odysseus.relational.base.schema;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Temporäre Klasse. Unterstützt die Auswertung von Tupel
 * über SchemaIndexPaths.
 * 
 * @author Timo Michelsen
 *
 */
public class TupleHelper {

	private RelationalTuple<?> tuple;
	
	/**
	 * Erstellt eine neue TupleHelper-Instanz mit dem angegebenen Tupel.
	 * 
	 * @param tuple Tupel
	 */
	public TupleHelper(RelationalTuple<?> tuple) {
		this.tuple = tuple;
	}
	
	/**
	 * Liefert das Objekt im Tupel mit dem angegebenen <code>SchemaIndexPath</code>
	 * 
	 * @param path Pfad zu einem Tupel als Schemaindexpfad
	 * @return Das Objekt, welches sich an der angegebenen Stelle im Tupel befindet
	 */
	public Object getObject( SchemaIndexPath path ) {
		return TupleIndexPath.fromSchemaIndexPath(path, tuple).getTupleObject();
	}
	
	/**
	 * Liefert das Objekt im Tupel mit dem angegebenen Integer-Pfad.
	 * 
	 * @param path Pfad zu einem Tupel als Schemaindexpfad
	 * @return Das Objekt, welches sich an der angegebenen Stelle im Tupel befindet
	 */
	public Object getObject( List<Integer> path ) {
		int[] i = new int[path.size()];
		for( int p = 0; p < path.size(); p++ )
			i[p] = path.get(p);
		return getObject(i);
	}
	
	/**
	 * Liefert das Objekt im Tupel mit dem angegebenen int-Pfad.
	 * 
	 * @param path Pfad zu einem Tupel als Schemaindexpfad
	 * @return Das Objekt, welches sich an der angegebenen Stelle im Tupel befindet
	 */
	@SuppressWarnings("unchecked")
	public Object getObject(int[] path ) {
		Object actualAttribute = null;
		Object[] actualList = tuple.getAttributes();
		for( int i = 0; i < path.length; i++ ) {
			actualAttribute = actualList[path[i]];
			if( actualAttribute instanceof RelationalTuple<?>) {
				actualList = ((RelationalTuple<?>)actualAttribute).getAttributes();
			} else if( actualAttribute instanceof List ) {
				actualList = ((List<Object>)actualAttribute).toArray();
			}
		}

		return actualAttribute;
	}

	/**
	 * Liefert das aktuell verwendete Tupel dieser TupleHelper-Instanz
	 * 
	 * @return Das verwendete Tuple
	 */
	public RelationalTuple<?> getTuple() {
		return tuple;
	}
	
	
}
