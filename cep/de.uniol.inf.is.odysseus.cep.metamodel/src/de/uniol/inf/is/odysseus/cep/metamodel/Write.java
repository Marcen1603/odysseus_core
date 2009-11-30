package de.uniol.inf.is.odysseus.cep.metamodel;

/**
 * Symboltabellen-Operator, der den Wert einer Variable in der Symboltabelle
 * setzt.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Write<T> extends SymbolTableOperation<T> {

	public Write() {
		super();
	}

	/**
	 * Implementiert die Schnittstelle zum Ausführen der Operation auf der
	 * Symboltabelle.
	 * 
	 * @param oldValue
	 *            Der alte Wert aus der Symboltabelle. (Wird ignoriert)
	 * @param eventValue
	 *            Der Attributwert aus dem Event.
	 * @return Gibt den Wert von eventValue zurück.
	 */
	@Override
	public T execute(T oldValue, T eventValue) {
		return eventValue;
	}
	
	@Override
	public String toString(String indent) {
		String str = indent + "Set: " + this.hashCode();
		indent += "  ";
		return str;
	}
}
