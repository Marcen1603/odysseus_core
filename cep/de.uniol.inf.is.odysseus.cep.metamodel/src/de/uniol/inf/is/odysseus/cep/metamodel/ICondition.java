package de.uniol.inf.is.odysseus.cep.metamodel;


public interface ICondition extends IExpression{

	/**
	 * Setzt die Transitionsbedingung.
	 * 
	 * @param label
	 *            Die neue Transitionsbedingung als String. Darf nur (nach dem
	 *            internen Namensschema) gültige Variablennamen beinhalten.
	 *         	  Wird null
	 *            übergeben, so wird der Ausdruck auf 1 (true) gesetzt. Die
	 *            Transition wird somit zu einem Epsilon-Übergang.
	 */

	public void setLabel(String label);
	public String getLabel();
	public String toString(String indent);
	
}
