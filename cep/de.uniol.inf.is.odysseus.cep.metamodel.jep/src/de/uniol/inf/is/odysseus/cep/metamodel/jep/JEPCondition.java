package de.uniol.inf.is.odysseus.cep.metamodel.jep;

import java.util.Set;

import de.uniol.inf.is.odysseus.cep.metamodel.AbstractCondition;

import org.nfunk.jep.JEP;

public class JEPCondition extends AbstractCondition {

	/**
	 * Referenz auf den Ausdruck der Transitionsbedingung
	 */
	private JEP expression;
	
	/**
	 * Erzeugt eine neue Transitionsbedingung aus einer textuellen Beschreibung
	 * des entsprechenden Ausdrcuks.
	 * 
	 * @param jepExpression
	 *            Die neue Transitionsbedingung als String. Darf nur (nach dem
	 *            internen Namensschema) gültige Variablennamen beinhalten.
	 *            Ausdruck muss von JEP geparst werden können. Wird null
	 *            übergeben, so wird der Ausdruck auf 1 (true) gesetzt. Die
	 *            Transition wird somit zu einem Epsilon-Übergang.
	 */
	
	public JEPCondition(String jepExpression) {
		setLabel(jepExpression);
	}
	
	public void setLabel(String label) {
		super.setLabel((label==null) ? "1" : label);
		this.expression = new JEP();
		this.expression.setAllowUndeclared(true);
		this.expression.parseExpression(getLabel());
	}

	public String toString(String indent) {
		String str = indent + "Condition: " + this.hashCode();
		indent += "  ";
		str += indent + "JEP-Expression: " + this.hashCode();
		indent += "  ";
		str += indent + this.expression.getValue();
		return str;
	}


	@SuppressWarnings("unchecked")
	public Set<String> getVarNames() {
		return expression.getSymbolTable().keySet();
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
