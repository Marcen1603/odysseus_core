package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;

/**
 * Instanzen dieser Klasse stellen die Symboltabelle einer AutomatenInstanz dar.
 * In der Symboltabelle werden aktuelle Berechnungszustände für eine
 * Automateninstanz gespeichert.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class SymbolTable<T> {

	/**
	 * Liste mit allen Einträgen der Symboltabelle
	 */
	private Map<String, IPartialAggregate<T>> entries = new HashMap<String, IPartialAggregate<T>>();
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
			this.entries.put(v.getVariableName(), null);
			this.vars.put(v.getVariableName(), v);
		}		
	}

	public SymbolTable(SymbolTable<T> symbolTable) {
		this.entries = new HashMap<String, IPartialAggregate<T>>();
		for (Entry<String, IPartialAggregate<T>> e: symbolTable.entries.entrySet()){
				this.entries.put(e.getKey(), e.getValue()!=null?e.getValue().clone():null);
			
		}
		this.vars = new HashMap<String, CepVariable>(symbolTable.vars);
	}

	public Map<String, IPartialAggregate<T>> getEntries() {
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
//	public Object getValue(String name) {
//		return entries.get(name);
//	}

	public Object getValue(CepVariable name) {
		IPartialAggregate<T> v = entries.get(name.getVariableName());
		if (v  != null){
			return name.getOperation().evaluate(v);
		}else{
			return null;
		}
		// return entries.get(name.getVariableName());
	}

	public void updateValue(CepVariable variable, T value) {
		IPartialAggregate<T> e = entries.get(variable.getVariableName());
		if (e==null){
			e = variable.getOperation().init(value);
		}else{
			IPartialAggregate<T> e_new = null;
			e_new = variable.getOperation().merge(e, value, true);
			e = e_new;
		}
		entries.put(variable.getVariableName(), e);
	}

	
	@Override
	public String toString() {
		String str = "Symbol table (#: " + this.entries.size() + "): [";
		for (Entry<String, IPartialAggregate<T>> entry : this.entries.entrySet()) {
			str += " " + entry.getKey()+"="+entry.getValue();
		}
		return str+"]";
	}
	

	@Override
	public SymbolTable<T> clone() {
		return new SymbolTable<T>(this);
	}

}
