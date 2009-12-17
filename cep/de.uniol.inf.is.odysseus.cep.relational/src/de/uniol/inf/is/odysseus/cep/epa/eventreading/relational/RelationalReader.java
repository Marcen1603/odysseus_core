package de.uniol.inf.is.odysseus.cep.epa.eventreading.relational;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.cep.epa.eventreading.AbstractEventReader;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class RelationalReader extends AbstractEventReader<RelationalTuple<? extends ITimeInterval>> {

	HashMap<String, Integer> scheme;
	
	
	/**
	 * Erzeugt einen neuen Eventreader fÃ¼r Events vom Typ
	 * {@link RelationalTuple}.
	 *  
	 * @param scheme
	 *            Das relationale Schema der Tupel, die gelesen werden sollen.
	 *            
	 */
	public RelationalReader(SDFAttributeList scheme, String type) {
		super(type);
		this.scheme = new HashMap<String, Integer>();
		int i=0;
		for (SDFAttribute a:scheme) {
			Integer pos = new Integer(i);
			this.scheme.put(a.toString(), pos);
			this.scheme.put(a.getAttributeName(), pos);
			i++;
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
	protected Object getValue_internal(String id, RelationalTuple<? extends ITimeInterval> event) {
		if (id.isEmpty())
			return null;//Leere Attribut id bei bstimmten Aggregationen (z.B. Count)
		
		Integer pos = this.scheme.get(id);
		if (pos == null){
			// Variable ist alles nach dem letzten "."
			String identifier = id.substring(id.lastIndexOf(".")+1);
			pos = this.scheme.get(identifier);
			if (pos!=null){
				// Beim nächsten mal sofort finden :-)
				scheme.put(id, pos);
			}
		}
		if (pos != null){
			RelationalTuple<?> tuple = (RelationalTuple<?>) event;
			Object ret = tuple.getAttribute(pos);
			return ret;
		}else{
			return null;
		}
	}
	
	@Override
	public long getTime(RelationalTuple<? extends ITimeInterval> event) {
		ITimeInterval meta = event.getMetadata();
		return meta.getStart().getMainPoint(); // ACHTUNG: Natürlich nur ganze Zahlen liefern
	}

}
