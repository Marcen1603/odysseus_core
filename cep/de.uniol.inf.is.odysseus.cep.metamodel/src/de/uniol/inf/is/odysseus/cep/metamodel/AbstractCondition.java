package de.uniol.inf.is.odysseus.cep.metamodel;

/**
 * Diese Klasse kapselt die Transitionsbedingung. 
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
abstract public class AbstractCondition implements ICondition{

	/**
	 * Textuelle Repraesentation der Transitionsbedingung. 
	 */
	private String label;
	
	public AbstractCondition() {
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Liefert die textuelle Repraesentation der Transitionsbedingung.
	 * 
	 * @return Transitionsbedingung als String.
	 */
	public String getLabel() {
		return label;
	}
	
	@Override
	public String toString() {
		return this.getLabel();
	}

}
