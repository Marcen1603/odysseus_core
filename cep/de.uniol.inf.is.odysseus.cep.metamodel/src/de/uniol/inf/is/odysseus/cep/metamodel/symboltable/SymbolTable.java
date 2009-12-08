package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import java.util.LinkedList;


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
	private LinkedList<SymbolTableEntry<Object>> entries;

	/**
	 * Standardkonstruktor. Erzeugt eine leere Symboltabelle
	 */
	public SymbolTable() {
		this.entries = new LinkedList<SymbolTableEntry<Object>>();
	}

	/**
	 * Erzeugt eine neue Symboltabelle aus einem Symboltabellenschema
	 * 
	 * @param symTabScheme
	 *            Symboltabellenschema, aus dem die Symboltabelle generiert
	 *            werden soll
	 */
//	public SymbolTable(SymbolTableScheme<Object> symTabScheme) {
//		this.entries = new LinkedList<SymbolTableEntry<Object>>();
//		for (int i = 0; i < symTabScheme.getEntries().size(); i++) {
//			if (symTabScheme.getEntries().get(i) != null) {
//				this.entries.add(new SymbolTableEntry<Object>(symTabScheme.getEntries()
//						.get(i)));
//			}
//		}
//	}

	/**
	 * Konstruktor, der nur für die clone()-Methode benötigt wird und daher
	 * nicht weiter verwendet werden sollte.
	 * 
	 * @param entries
	 *            Liste aller Einträge in der Symboltabelle, nicht null
	 */
	private SymbolTable(LinkedList<SymbolTableEntry<Object>> entries) {
		this.entries = entries;
	}

	/**
	 * @return Liste mit allen Einträgen der Symboltabelle
	 */
	public LinkedList<SymbolTableEntry<Object>> getEntries() {
		return this.entries;
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
		for (SymbolTableEntry<Object> entry : this.entries) {
			if (name.equals(entry.getVariable().getVariableName())) {
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
		LinkedList<SymbolTableEntry<Object>> newEntries = new LinkedList<SymbolTableEntry<Object>>();
		for (SymbolTableEntry<Object> entries : this.entries) {
			newEntries.add(entries.clone());
		}
		return new SymbolTable(newEntries);
	}
	
	@Override
	public String toString() {
		String str = "Symbol table (entries: " + this.entries.size() + "):";
		for (SymbolTableEntry<Object> entry : this.entries) {
			str += "\n" + entry.toString();
		}
		return str;
	}
	
}
