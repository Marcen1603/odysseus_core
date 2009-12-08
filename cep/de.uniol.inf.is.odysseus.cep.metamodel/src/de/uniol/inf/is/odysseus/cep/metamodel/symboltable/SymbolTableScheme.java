package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;

/**
 * Objekte dieser Klasse repräsentieren das Schema einer Symboltabelle
 * 
 * @author Thomas Vogelgesang
 * 
 */
@Deprecated
class SymbolTableScheme<T> {

	/**
	 * Liste aller Einträge in der Symboltabelle
	 */
	private LinkedList<CepVariable<T>> entries;

	/**
	 * Erzeugt ein Symboltabellenschema aus einer Liste von
	 * Symboltabellenschemaeinträgen
	 * 
	 * @param entries
	 *            Liste mit den Einträgen des Symboltabellenschemas
	 */
	public SymbolTableScheme(LinkedList<CepVariable<T>> entries) {
		this.entries = entries;
	}

	/**
	 * Erzeugt ein leeres Symboltabellenschema
	 */
	public SymbolTableScheme() {
		this.entries = new LinkedList<CepVariable<T>>();
	}

	/**
	 * Liefert eine Liste mit allen Einträgen des Symboltabellen-Schemas.
	 * 
	 * @return Liste mit allen Symboltabellen-Schema-Einträgen.
	 */
	public LinkedList<CepVariable<T>> getEntries() {
		return entries;
	}

	/**
	 * Setzt die Liste der Einträge im Symboltabellenschema.
	 * 
	 * @param entries
	 *            Liste der Einträge im Symboltabellenschema. Nicht null.
	 */
	public void setEntries(LinkedList<CepVariable<T>> entries) {
		this.entries = entries;
	}
	
	public String toString(String indent) {
		String str = indent + "SymTabScheme: " + this.hashCode();
		indent += "  ";
		for (CepVariable<T> entry : this.entries) {
			str += entry.toString(indent);
		}
		return str;
	}
	
	@Override
	public String toString() {
		return toString("");
	}
}
