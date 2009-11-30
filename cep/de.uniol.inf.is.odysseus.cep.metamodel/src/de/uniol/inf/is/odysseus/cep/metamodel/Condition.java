package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.Set;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Variable;

/**
 * Diese Klasse kapselt die Transitionsbedingung. 
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class Condition {

	/**
	 * Referenz auf den Ausdruck der Transitionsbedingung
	 */
	private JEP expression;
	/**
	 * Textuelle Repräsentation der Transitionsbedingung. Da aus diesem String
	 * mittels JEP der berechenbare Ausdruck geparst wird, muss der String stets
	 * von geparst werden können.
	 */
	private String label;

	/**
	 * Erzeugt eine neue Transitionsbedingung aus einer textuellen Beschreibung
	 * des entsprechenden Ausdrcuks.
	 * 
	 * @param expression
	 *            Die neue Transitionsbedingung als String. Darf nur (nach dem
	 *            internen Namensschema) gültige Variablennamen beinhalten.
	 *            Ausdruck muss von JEP geparst werden können. Wird null
	 *            übergeben, so wird der Ausdruck auf 1 (true) gesetzt. Die
	 *            Transition wird somit zu einem Epsilon-Übergang.
	 */
	public Condition(String expression) {
		this.setLabel(expression);
	}
	
	public Condition() {
	}

	/**
	 * Liefert die textuelle Repräsentation der Transitionsbedingung.
	 * 
	 * @return Transitionsbedingung als String.
	 */
	public String getLabel() {
		return label;
	}

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
	public void setLabel(String label) {
		this.label = (label==null) ? "1" : label;
		this.expression = new JEP();
		this.expression.setAllowUndeclared(true);
		this.expression.parseExpression(this.label);
	}

	public String toString(String indent) {
		String str = indent + "Condition: " + this.hashCode();
		indent += "  ";
		str += indent + "JEP-Expression: " + this.hashCode();
		indent += "  ";
		str += indent + this.expression.getValue();
		return str;
	}

	@Override
	public String toString() {
		return this.getLabel();
	}

	@SuppressWarnings("unchecked")
	public Set<String> getVarNames() {
		return expression.getSymbolTable().keySet();
	}

	public Variable getVar(String name) {
		return expression.getVar(name);
	}

	public double getValue() {
		return expression.getValue();
	}

	public Object getValueAsObject() {
		return expression.getValueAsObject();
	}

	public String getErrorInfo() {
		return expression.getErrorInfo();
	}

	public void setValue(String varName, Object newValue) {
		expression.getVar(varName).setValue(newValue);
	}
}
