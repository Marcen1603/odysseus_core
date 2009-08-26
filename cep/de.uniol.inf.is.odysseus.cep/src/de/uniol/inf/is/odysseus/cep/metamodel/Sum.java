package de.uniol.inf.is.odysseus.cep.metamodel;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidDataTypeForSymTabOperationException;

/**
 * Symboltabellen-Operation, die den Wert eines Attributs aufsummiert.
 * 
 * @author tommy
 * 
 */
public class Sum extends SymbolTableOperation {

	public Sum() {
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
	public Object execute(Object oldValue, Object eventValue)
			throws InvalidDataTypeForSymTabOperationException {
		if (oldValue == null) {
			return eventValue;
		}
		if (oldValue instanceof Integer && eventValue instanceof Integer) {
			int a = ((Integer) oldValue).intValue();
			int b = ((Integer) eventValue).intValue();
			return new Integer(a + b);
		} else if (oldValue instanceof Long && eventValue instanceof Long) {
			long a = ((Long) oldValue).longValue();
			long b = ((Long) eventValue).longValue();
			return new Long(a + b);
		} else if (oldValue instanceof Float && eventValue instanceof Float) {
			float a = ((Float) oldValue).floatValue();
			float b = ((Float) eventValue).floatValue();
			return new Float(a + b);
		} else if (oldValue instanceof Double && eventValue instanceof Double) {
			double a = ((Double) oldValue).doubleValue();
			double b = ((Double) eventValue).doubleValue();
			return new Double(a + b);
		} else {
			throw new InvalidDataTypeForSymTabOperationException(
					"Expected object of type java.lang.Integer");
		}
	}

	public String toString(String indent) {
		String str = indent + "Sum: " + this.hashCode();
		indent += "  ";
		return str;
	}

}
