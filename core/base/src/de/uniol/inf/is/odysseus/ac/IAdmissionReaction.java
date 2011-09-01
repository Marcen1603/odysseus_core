package de.uniol.inf.is.odysseus.ac;

import java.util.List;

/**
 * Schnittstelle, welche Algorithmen zur Auswahl eines Vorschlags zur
 * Überlastauflösung aus einer Menge von Vorschlägen repräsentiert. Die aktuelle
 * Implementierung der Schnittstelle lässt sich über den entsprechenden Service
 * ermitteln.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IAdmissionReaction {

	/**
	 * Wählt aus einer Menge von Vorschlägen einen Vorschlag aus.
	 * 
	 * @param possibilities
	 *            Menge an Vorschlägen
	 * @return Gewählter Vorschlag
	 */
	public IPossibleExecution react(List<IPossibleExecution> possibilities);

}
