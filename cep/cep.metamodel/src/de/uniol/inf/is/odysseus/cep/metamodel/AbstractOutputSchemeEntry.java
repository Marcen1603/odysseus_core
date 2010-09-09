package de.uniol.inf.is.odysseus.cep.metamodel;

import de.uniol.inf.is.odysseus.cep.metamodel.exception.UndefinedExpressionLabelException;

/**
 * Eine Instanz diser Klasse stellt einen Eintrag im Ausgabeschema dar.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
abstract public class AbstractOutputSchemeEntry implements IOutputSchemeEntry{

	/**
	 * textuelle Darstellung des Ausdrucks
	 */
	private String label;

	public AbstractOutputSchemeEntry() {
	}

	/**
	 * Erzeugt einen Eintrag für das Ausgabeschema aus einer textuellen
	 * Beschreibung des Ausdrucks.
	 * 
	 * @param label
	 *            textuelle Darstellung des Ausdrucks. Nicht null.
	 */
	public AbstractOutputSchemeEntry(String label) {
		setLabel(label);
	}

	/**
	 * Setzt die textuelle Darstellung des Ausgabeschema-Eintrags und erzeugt
	 * daraus automatisch einen Ausdrucksbaum.
	 * 
	 * @param label De textuelle darstellung des Ausgabeschema-Eintrags.
	 * @throws UndefinedExpressionLabelException Falls das Label null oder leer ist.
	 */
	public void setLabel(String label) throws UndefinedExpressionLabelException {
		if (label == null) {
			throw new UndefinedExpressionLabelException();
		} else if (label.isEmpty()) {
			throw new UndefinedExpressionLabelException();
		} 	
		this.label = label;
	}

	/**
	 * Gibt das Label des Ausgabeschema-Eintrags zurück.
	 * 
	 * @return Das Label des Eintrags.
	 */
	public String getLabel() {
		return this.label;
	}


}
