package model;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * Datenstruktur für das All-Pattern.
 * @author Michael Falk
 */
public class AllPatternDataStructure<T extends IStreamObject<? extends ITimeInterval>> {
	
	private List<String> eventTypes;
	
	public AllPatternDataStructure(List<String> eventTypes) {
		this.eventTypes = new ArrayList<String>(eventTypes.size());
		this.eventTypes.addAll(eventTypes);
	}
	
	/**
	 * Führt den Matching-Prozess durch.
	 * @param eventType
	 * @return true, wenn Pattern erkannt, sonst false
	 */
	public boolean startMatching(String eventType) {
		int i = 0;
		schleife:
		while (i < eventTypes.size()) {
			if (eventType.equals(eventTypes.get(i))) {
				eventTypes.remove(i);
				break schleife;
			}
			i++;
		};
		if (eventTypes.size() == 0) return true;
		return false;
	}
	
}
