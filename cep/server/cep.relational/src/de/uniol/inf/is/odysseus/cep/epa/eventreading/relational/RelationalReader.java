/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.cep.epa.eventreading.relational;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.cep.epa.eventreading.AbstractEventReader;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class RelationalReader extends AbstractEventReader<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {

	HashMap<String, Integer> scheme;
	private SDFSchema schema;
	
	
	/**
	 * Erzeugt einen neuen Eventreader für Events vom Typ
	 * {@link RelationalTuple}.
	 *  
	 * @param scheme
	 *            Das relationale Schema der Tupel, die gelesen werden sollen.
	 *            
	 */
	public RelationalReader(SDFSchema scheme, String type) {
		super(type);
		this.schema = scheme;
		this.scheme = new HashMap<String, Integer>();
		int i=0;
		for (SDFAttribute a:scheme) {
			Integer pos = new Integer(i);
			this.scheme.put(a.getURI(), pos);
			this.scheme.put(a.getAttributeName(), pos);
			i++;
		}
	}
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
	
	/**
	 * Liest den Wert eines Eventattributs aus einem relationalen Tupel aus.
	 * 
	 * @param identifier
	 *            Name des Attributs, dessen Wert gelesen werden soll.
	 * @param event
	 *            Das Event, aus dem der Attributwert gelesen werden soll. Muss
	 *            vom Typ {@link RelationalTuple} sein!
	 */
	@Override
	public Tuple<? extends ITimeInterval> getValue(String id, Tuple<? extends ITimeInterval> event) {
		if (id.isEmpty())
			return null;//Leere Attribut id bei bstimmten Aggregationen (z.B. Count)
		
		Integer pos = this.scheme.get(id);
		if (pos == null){
			// Variable ist alles nach dem letzten "."
			String identifier = id.substring(id.lastIndexOf(".")+1);
			pos = this.scheme.get(identifier);
			if (pos!=null){
				// Beim n�chsten mal sofort finden :-)
				scheme.put(id, pos);
			}
		}
		if (pos != null){
			return event.restrict(pos, true);
		}
        return null;
	}
	
	@Override
	public long getTime(Tuple<? extends ITimeInterval> event) {
		ITimeInterval meta = event.getMetadata();
		return meta.getStart().getMainPoint(); // ACHTUNG: Nat�rlich nur ganze Zahlen liefern
	}

}
