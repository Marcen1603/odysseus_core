package de.uniol.inf.is.odysseus.cep.epa.eventreading;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.AbstractEventReader;

public class RelationalReader extends AbstractEventReader<RelationalTuple<?>,Object> {

	HashMap<String, Integer> scheme;

	/**
	 * Erzeugt einen neuen Eventreader für Events vom Typ
	 * {@link RelationalTuple}.
	 * 
	 * @param scheme
	 *            Das relationale Schema der Tupel, die gelesen werden sollen.
	 */
	public RelationalReader(SDFAttributeList scheme) {
		super();
		this.scheme = new HashMap<String, Integer>();
		for (int i = 0; i < scheme.getAttributeCount(); i++) {
			this.scheme.put(scheme.get(i).toString(), new Integer(i));
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
	public Object getValue(String identifier, RelationalTuple<?> event) {
		if (identifier.isEmpty())
			return null;//Leere Attribut id bei bstimmten Aggregationen (z.B. Count)
		
		RelationalTuple<?> tuple = (RelationalTuple<?>) event;
		return tuple.getAttribute(this.scheme.get(identifier));
	}

}
