package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import de.uniol.inf.is.odysseus.cep.metamodel.exception.InvalidDataTypeForSymTabOperationException;


/**
 * Diese Klasse stellt eine Zähl-Operatoren für die Symboltabelle dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Count extends AbstractSymbolTableOperation<Integer> {

	public Count() {
		super();
	}

	/**
	 * Implementierung der Schnittstelle zum Ausführen der Operation auf der
	 * Symboltabelle.
	 * 
	 * @param oldValue
	 *            Der alte Wert aus der Symboltabelle. Muss vom Typ
	 *            {@link java.lang.Integer} sein.
	 * @param eventValue
	 *            Der Attributwert aus dem Event. (Wird ignoriert)
	 * @return Ein neues Objekt, das den berechneten Wert zurück gibt. Objekt
	 *         ist vom Typ {@link java.lang.Integer} oder null, wenn ein Fehler
	 *         auftritt.
	 * @throws InvalidDataTypeForSymTabOperationException
	 *             Falls oldValue nicht vom Typ {@link java.lang.Integer} ist
	 */
	@Override
	public Integer execute(Integer oldValue, Integer eventValue)
			throws InvalidDataTypeForSymTabOperationException {
		if (oldValue == null) {
			return new Integer(1);
		} else {
			return new Integer(((Integer) oldValue).intValue() + 1);
		}
		
	}

	@Override
	public String toString(String indent) {
		String str = indent + "Count: " + this.hashCode();
		indent += "  ";
		return str;
	}

}
