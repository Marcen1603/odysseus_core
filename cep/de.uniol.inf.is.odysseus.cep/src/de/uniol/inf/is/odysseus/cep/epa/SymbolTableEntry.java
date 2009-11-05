package de.uniol.inf.is.odysseus.cep.epa;

import de.uniol.inf.is.odysseus.cep.epa.eventreading.AbstractEventReader;
import de.uniol.inf.is.odysseus.cep.metamodel.SymbolTableSchemeEntry;

/**
 * Instanzen dieser Klasse stellen Einträge in einer Symboltabelle dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SymbolTableEntry {

	/**
	 * Referenz auf das Schema des Symboltabelleneintrags
	 */
	private SymbolTableSchemeEntry scheme;
	/**
	 * Der Wert einer Variablen
	 */
	private Object value;

	/**
	 * Erzeugt leeren Eintrag. Der Wert des Eintrags bleibt uninitialisiert!
	 */
	public SymbolTableEntry(SymbolTableSchemeEntry entryScheme) {
		this.scheme = entryScheme;
	}

	/**
	 * Liefert den aktuellen Wert des Eintrags.
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Ändert den aktuellen Wert des Symboltabelleneintrags.
	 * 
	 * @param value
	 *            Der neue Wert des Eintrags, nicht null.
	 */
	protected void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Liefert das zum Eintrag gehörende Schema.
	 * 
	 * @return Das zum Eintrag gehörende Schema.
	 */
	public SymbolTableSchemeEntry getScheme() {
		return scheme;
	}

	/**
	 * Führt die im Schema definierte Operation auf einem Eintrag in der
	 * Symboltabelle aus, wodurch der Eintrag auf den Stand des sich gerade in
	 * der Verarbeitung befindlichen Events aktualisiert wird.
	 * 
	 * @param event
	 *            Das Event, das gerade verarbeitet wird.
	 * @param eventReader
	 *            Eine konkrete EventReader-Implementierung, die das übergebene
	 *            Event auslesen kann.
	 */
	public void executeOperation(Object event, AbstractEventReader eventReader) {
		Object value = eventReader.getValue(this.scheme.getAttribute(), event);
		this.value = this.scheme.getOperation().execute(this.value, value);
	}

	/**
	 * Gibt eine flache Kopie des Symboltabelleneintrags zurück.
	 */
	@Override
	public SymbolTableEntry clone() {
		SymbolTableEntry newEntry = new SymbolTableEntry(this.getScheme());
		newEntry.setValue(this.value);
		return newEntry;
	}

	@Override
	public String toString() {
		return this.getScheme().getVariableName() + ": " + this.value; 
	}
}
