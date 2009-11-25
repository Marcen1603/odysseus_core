package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.LinkedList;

/**
 * Objekte dieser Klasse repräsentieren das Schema einer Symboltabelle
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SymbolTableScheme {

	/**
	 * Liste aller Einträge in der Symboltabelle
	 */
	private LinkedList<SymbolTableSchemeEntry> entries;

	/**
	 * Erzeugt ein Symboltabellenschema aus einer Liste von
	 * Symboltabellenschemaeinträgen
	 * 
	 * @param entries
	 *            Liste mit den Einträgen des Symboltabellenschemas
	 */
	public SymbolTableScheme(LinkedList<SymbolTableSchemeEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Erzeugt ein leeres Symboltabellenschema
	 */
	public SymbolTableScheme() {
		this.entries = new LinkedList<SymbolTableSchemeEntry>();
	}

	/**
	 * Liefert eine Liste mit allen Einträgen des Symboltabellen-Schemas.
	 * 
	 * @return Liste mit allen Symboltabellen-Schema-Einträgen.
	 */
	public LinkedList<SymbolTableSchemeEntry> getEntries() {
		return entries;
	}

	/**
	 * Setzt die Liste der Einträge im Symboltabellenschema.
	 * 
	 * @param entries
	 *            Liste der Einträge im Symboltabellenschema. Nicht null.
	 */
	public void setEntries(LinkedList<SymbolTableSchemeEntry> entries) {
		this.entries = entries;
	}
	
	public String toString(String indent) {
		String str = indent + "SymTabScheme: " + this.hashCode();
		indent += "  ";
		for (SymbolTableSchemeEntry entry : this.entries) {
			str += entry.toString(indent);
		}
		return str;
	}
}
