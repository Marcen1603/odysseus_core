package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.VarNotFoundInSymTabException;
import de.uniol.inf.is.odysseus.cep.metamodel.SymbolTableScheme;

/**
 * Instanzen dieser Klasse stellen die Symboltabelle einer AutomatenInstanz dar.
 * In der Symboltabelle werden aktuelle Berechnungszustände für eine
 * Automateninstanz gespeichert.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SymbolTable {

	/**
	 * Liste mit allen Einträgen der Symboltabelle
	 */
	private LinkedList<SymbolTableEntry> entries;

	/**
	 * Standardkonstruktor. Erzeugt eine leere Symboltabelle
	 */
	public SymbolTable() {
		this.entries = new LinkedList<SymbolTableEntry>();
	}

	/**
	 * Erzeugt eine neue Symboltabelle aus einem Symboltabellenschema
	 * 
	 * @param symTabScheme
	 *            Symboltabellenschema, aus dem die Symboltabelle generiert
	 *            werden soll
	 */
	public SymbolTable(SymbolTableScheme symTabScheme) {
		this.entries = new LinkedList<SymbolTableEntry>();
		for (int i = 0; i < symTabScheme.getEntries().size(); i++) {
			if (symTabScheme.getEntries().get(i) != null) {
				this.entries.add(new SymbolTableEntry(symTabScheme.getEntries()
						.get(i)));
			}
		}
	}

	/**
	 * Konstruktor, der nur für die clone()-Methode benötigt wird und daher
	 * nicht weiter verwendet werden sollte.
	 * 
	 * @param entries
	 *            Liste aller Einträge in der Symboltabelle, nicht null
	 */
	private SymbolTable(LinkedList<SymbolTableEntry> entries) {
		this.entries = entries;
	}

	/**
	 * @return Liste mit allen Einträgen der Symboltabelle
	 */
	public LinkedList<SymbolTableEntry> getEntries() {
		return this.entries;
	}

	/**
	 * Aktualisiert einen Wert in der Symboltabelle.
	 * 
	 * @param name
	 *            Name der Variablen, die in der Symboltabelle aktualisiert
	 *            werden soll. Darf nicht null sein.
	 * @param event
	 *            Das Event, das soeben konsumiert wird.
	 * @throws VarNotFoundInSymTabException
	 *             Wenn der übergebene Variablenname nicht in der Symboltabelle
	 *             gefunden werden konnte.
	 */
	@Deprecated //verändert Wert direkt und nicht über Operation!
	public void update(String name, Object value)
			throws VarNotFoundInSymTabException {
		boolean found = false;
		for (SymbolTableEntry entry : this.entries) {
			if (entry.getScheme().getVariableName().equals(name)) {
				entry.setValue(value);
				found = true;
			}
		}
		if (!found) {
			throw new VarNotFoundInSymTabException(
					"The variable with the given name cannot be found in symbol table: "
							+ name);
		}
	}

	/**
	 * Liefert den zu einer Variablen gehörenden Wert oder null, falls noch kein
	 * Wert in der Symboltabelle gespeichert ist oder in der Symboltabelle kein
	 * passender Eintrag zur Variable existiert.
	 * 
	 * @param name
	 *            Name der Variablen, durch den sie eindeutig identifiziert
	 *            wird.
	 * @return Den aktuellen Wert der Variablen oder null falls ein Fehler
	 *         auftritt.
	 */
	public Object getValue(String name) {
		for (SymbolTableEntry entry : this.entries) {
			if (name.equals(entry.getScheme().getVariableName())) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Gibt eine tiefe Kopie der Symboltabelle zurück
	 */
	@Override
	public SymbolTable clone() {
		LinkedList<SymbolTableEntry> newEntries = new LinkedList<SymbolTableEntry>();
		for (SymbolTableEntry entries : this.entries) {
			newEntries.add(entries.clone());
		}
		return new SymbolTable(newEntries);
	}
	
	@Override
	public String toString() {
		String str = "Symbol table (entries: " + this.entries.size() + "):";
		for (SymbolTableEntry entry : this.entries) {
			str += "\n" + entry.toString();
		}
		return str;
	}
	
}
