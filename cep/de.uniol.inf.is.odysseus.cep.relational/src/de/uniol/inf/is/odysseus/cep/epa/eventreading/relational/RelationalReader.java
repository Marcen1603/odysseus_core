package de.uniol.inf.is.odysseus.cep.epa.eventreading.relational;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.AbstractEventReader;

public class RelationalReader extends AbstractEventReader<RelationalTuple<?>,Object> {

	HashMap<String, Integer> scheme;

	/**
	 * Erzeugt einen neuen Eventreader f√ºr Events vom Typ
	 * {@link RelationalTuple}.
	 * 
	 * @param scheme
	 *            Das relationale Schema der Tupel, die gelesen werden sollen.
	 */
	public RelationalReader(SDFAttributeList scheme) {
		this.scheme = new HashMap<String, Integer>();
		for (int i = 0; i < scheme.getAttributeCount(); i++) {
			String id = scheme.get(i).toString();
			this.scheme.put(id, new Integer(i));
			// Im Prinzip interessiert nur der Teil nach dem letzten Punkt
			String identifier = id.substring(id.lastIndexOf(".")+1);
			this.scheme.put(identifier, new Integer(i));			
		}
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
		return tuple.getAttribute(pos);
	}

}
