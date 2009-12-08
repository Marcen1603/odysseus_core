package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;


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
	private Map<String, Object> entries = new HashMap<String, Object>();
	private Map<String, CepVariable> vars = new HashMap<String, CepVariable>();

	/**
	 * Standardkonstruktor. Erzeugt eine leere Symboltabelle
	 */
	public SymbolTable() {
	}

	/**
	 * Erzeugt eine neue Symboltabelle aus einem Symboltabellenschema
	 * 
	 * @param symTabScheme
	 *            Symboltabellenschema, aus dem die Symboltabelle generiert
	 *            werden soll
	 */
	public SymbolTable(List<CepVariable> symTabScheme) {
		for (CepVariable v: symTabScheme){
			this.entries.put(v.getVariableName(), new LinkedList<Object>());
			this.vars.put(v.getVariableName(), v);
		}		
	}

	public Map<String, Object> getEntries() {
		return entries;
	}

	public Collection<CepVariable> getKeys() {
		return vars.values();
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
		return entries.get(name);
	}

	public Object getValue(CepVariable name) {
		return entries.get(name.getVariableName());
	}

	private void setValue(CepVariable variable, Object value) {
		entries.put(variable.getVariableName(), value);
	}
	
	@Override
	public String toString() {
		String str = "Symbol table (entries: " + this.entries.size() + "):";
		for (Entry<String, Object> entry : this.entries.entrySet()) {
			str += "\n" + entry.getKey()+":"+entry.getValue();
		}
		return str;
	}
	
	public void executeOperation(CepVariable variable, Object value) {
		setValue(variable,variable.getOperation().execute(getValue(variable), value));
	}


}
