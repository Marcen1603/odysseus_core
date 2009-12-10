package de.uniol.inf.is.odysseus.cep.epa.eventreading.relational;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.AbstractEventReader;

public class RelationalReader extends AbstractEventReader<RelationalTuple<?>,Object> {

	HashMap<String, Integer> scheme;
	private String name;
	
	/**
	 * Erzeugt einen neuen Eventreader f√ºr Events vom Typ
	 * {@link RelationalTuple}.
	 *  
	 * @param scheme
	 *            Das relationale Schema der Tupel, die gelesen werden sollen.
	 *            
	 */
	public RelationalReader(SDFAttributeList scheme, String name) {
		this.scheme = new HashMap<String, Integer>();
		int i=0;
		for (SDFAttribute a:scheme) {
			Integer pos = new Integer(i);
			this.scheme.put(a.toString(), pos);
			this.scheme.put(a.getAttributeName(), pos);
			i++;
		}
		this.name =name;
	}

	@Override
	public String getName() {
		return name;
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
	public Object getValue(String id, RelationalTuple<?> event) {
		if (id.isEmpty())
			return null;//Leere Attribut id bei bstimmten Aggregationen (z.B. Count)
		
		Integer pos = this.scheme.get(id);
		if (pos == null){
			// Variable ist alles nach dem letzten "."
			String identifier = id.substring(id.lastIndexOf(".")+1);
			pos = this.scheme.get(identifier);
			// Beim n‰chsten mal sofort finden :-)
			scheme.put(id, pos);
		}
		
		RelationalTuple<?> tuple = (RelationalTuple<?>) event;
		if (pos != null){
			return tuple.getAttribute(pos);
		}else{
			return null;
		}
	}

}
