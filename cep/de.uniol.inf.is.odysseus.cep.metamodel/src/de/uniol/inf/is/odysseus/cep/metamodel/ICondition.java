package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.Set;

public interface ICondition {

	/**
	 * Setzt die Transitionsbedingung.
	 * 
	 * @param label
	 *            Die neue Transitionsbedingung als String. Darf nur (nach dem
	 *            internen Namensschema) gültige Variablennamen beinhalten.
	 *            Ausdruck muss von JEP geparst werden können. Wird null
	 *            übergeben, so wird der Ausdruck auf 1 (true) gesetzt. Die
	 *            Transition wird somit zu einem Epsilon-Übergang.
	 */

	public void setLabel(String label);
	public String getLabel();
	public Set<String> getVarNames();
	public double getValue();
	public Object getValueAsObject();
	public String getErrorInfo();
	public void setValue(String varName, Object newValue);
	public String toString(String indent);
	
}
