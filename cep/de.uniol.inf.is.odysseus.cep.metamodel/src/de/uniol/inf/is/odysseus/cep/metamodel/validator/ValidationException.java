package de.uniol.inf.is.odysseus.cep.metamodel.validator;

/**
 * Abstrakte Oberklasse für alle Ausnahmen während der Validierung.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class ValidationException {
	/**
	 * Referenz auf das Objekt, auf das sich diese Ausnahmen bezieht
	 */
	Object related;

	public ValidationException() {
		this.related = null;
	}

	/**
	 * Gibt das Objekt zurück, af das sich die Ausnahme bezieht.
	 * 
	 * @return Das betroffene Objekt.
	 */
	public Object getRelated() {
		return related;
	}

	/**
	 * Setzt das von der Ausnahme betroffene Objekt.
	 * 
	 * @param related
	 *            Das betroffene Objekt
	 */
	protected void setRelated(Object related) {
		this.related = related;
	}

}
