package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import de.uniol.inf.is.odysseus.cep.metamodel.exception.InvalidDataTypeForSymTabOperationException;

/**
 * Diese Klasse stellt einen Maximumoperator für die Symboltabelle dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Max extends AbstractSymbolTableOperation<Number> {

	public Max() {
		super();
	}

	/**
	 * Implementiert die Schnittstelle zum Ausführen der Operation auf der
	 * Symboltabelle.
	 * 
	 * @param oldValue
	 *            Der alte Wert aus der Symboltabelle. Muss von einem der
	 *            folgenden Typen und nicht null sein: Integer, Long, Float,
	 *            Double.
	 * @param eventValue
	 *            Der Attributwert aus dem Event. Muss vom selben Typen wie
	 *            {@link oldValue} sein. Darf nicht null sein.
	 * @return Ein neues Objekt, das den berechneten Wert zurück gibt.
	 * @throws InvalidDataTypeForSymTabOperationException
	 *             Falls die übergebenen Parameter nicht vom Typ
	 *             {@link java.lang.Integer} sind.
	 */
	@Override
	public Number execute(Number oldValue, Number eventValue){
		if (oldValue == null) {
			return eventValue;
		}
		if (oldValue instanceof Integer && eventValue instanceof Integer) {
			int a = ((Integer) oldValue).intValue();
			int b = ((Integer) eventValue).intValue();
			return (b > a) ? new Integer(b) : oldValue;
		} else if (oldValue instanceof Long && eventValue instanceof Long) {
			long a = ((Long) oldValue).longValue();
			long b = ((Long) eventValue).longValue();
			return (b > a) ? new Long(b) : oldValue;
		} else if (oldValue instanceof Float && eventValue instanceof Float) {
			float a = ((Float) oldValue).floatValue();
			float b = ((Float) eventValue).floatValue();
			return (b > a) ? new Float(b) : oldValue;
		} else if (oldValue instanceof Double && eventValue instanceof Double)  {
			double a = ((Double) oldValue).doubleValue();
			double b = ((Double) eventValue).doubleValue();
			return (b > a) ? new Double(b) : oldValue;
		} else {
			throw new InvalidDataTypeForSymTabOperationException(
			"Expected object of type java.lang.Integer");
}
	}

	@Override
	public String toString(String indent) {
		String str = indent + "Max: " + this.hashCode();
		indent += "  ";
		return str;
	}

}
