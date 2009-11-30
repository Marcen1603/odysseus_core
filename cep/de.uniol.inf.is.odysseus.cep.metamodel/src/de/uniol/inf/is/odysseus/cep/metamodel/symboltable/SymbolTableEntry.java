package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;

/**
 * Instanzen dieser Klasse stellen Einträge in einer Symboltabelle dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SymbolTableEntry<T> {

	/**
	 * Referenz auf das Schema des Symboltabelleneintrags
	 */
	private CepVariable<T> scheme;
	/**
	 * Der Wert einer Variablen
	 */
	private T value;

	/**
	 * Erzeugt leeren Eintrag. Der Wert des Eintrags bleibt uninitialisiert!
	 */
	public SymbolTableEntry(CepVariable<T> entryScheme) {
		this.scheme = entryScheme;
	}

	/**
	 * Liefert den aktuellen Wert des Eintrags.
	 * 
	 * @return
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Ändert den aktuellen Wert des Symboltabelleneintrags.
	 * 
	 * @param value
	 *            Der neue Wert des Eintrags, nicht null.
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Liefert das zum Eintrag gehörende Schema.
	 * 
	 * @return Das zum Eintrag gehörende Schema.
	 */
	public CepVariable<T> getScheme() {
		return scheme;
	}

	/**
	 * Führt die im Schema definierte Operation auf einem Eintrag in der
	 * Symboltabelle aus, wodurch der Eintrag auf den Stand des sich gerade in
	 * der Verarbeitung befindlichen Events aktualisiert wird.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void executeOperation(Object value) {
		// TODO: ist der Cast das hinten schlau? Geht das �berhaupt anders?
		this.value = this.scheme.getOperation().execute(this.value, (T)value);
	}

	/**
	 * Gibt eine flache Kopie des Symboltabelleneintrags zurück.
	 */
	@Override
	public SymbolTableEntry<T> clone() {
		SymbolTableEntry<T> newEntry = new SymbolTableEntry<T>(this.getScheme());
		newEntry.setValue(this.value);
		return newEntry;
	}

	@Override
	public String toString() {
		return this.getScheme().getVariableName() + ": " + this.value; 
	}
}
