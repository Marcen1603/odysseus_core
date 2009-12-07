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
	private CepVariable<T> variable;
	/**
	 * Der Wert einer Variablen
	 */
	private T value;

	/**
	 * Erzeugt leeren Eintrag. Der Wert des Eintrags bleibt uninitialisiert!
	 */
	public SymbolTableEntry(CepVariable<T> variable) {
		this.variable = variable;
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
	public CepVariable<T> getVariable() {
		return variable;
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
		this.value = this.variable.getOperation().execute(this.value, (T)value);
	}

	/**
	 * Gibt eine flache Kopie des Symboltabelleneintrags zurück.
	 */
	@Override
	public SymbolTableEntry<T> clone() {
		SymbolTableEntry<T> newEntry = new SymbolTableEntry<T>(this.getVariable());
		newEntry.setValue(this.value);
		return newEntry;
	}

	@Override
	public String toString() {
		return this.getVariable().getVariableName() + ": " + this.value; 
	}
}
