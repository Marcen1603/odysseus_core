package de.uniol.inf.is.odysseus.cep.metamodel.jep;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.nfunk.jep.JEP;

import de.uniol.inf.is.odysseus.cep.metamodel.AbstractCondition;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.ConditionEvaluationException;

public class JEPCondition extends AbstractCondition {

	/**
	 * Referenz auf den Ausdruck der Transitionsbedingung
	 */
	private JEP expression;
	private Map<String, String> symbolTable = new HashMap<String, String>();
	private boolean negate = false;

	/**
	 * Erzeugt eine neue Transitionsbedingung aus einer textuellen Beschreibung
	 * des entsprechenden Ausdrcuks.
	 * 
	 * @param jepExpression
	 *            Die neue Transitionsbedingung als String. Darf nur (nach dem
	 *            internen Namensschema) gÃ¼ltige Variablennamen beinhalten.
	 *            Ausdruck muss von JEP geparst werden kÃ¶nnen. Wird null
	 *            Ã¼bergeben, so wird der Ausdruck auf 1 (true) gesetzt. Die
	 *            Transition wird somit zu einem Epsilon-Ãœbergang.
	 */

	public JEPCondition(String jepExpression) {
		setLabel(jepExpression);
	}

	public void setLabel(String label) {
		super.setLabel((label == null || label.length() == 0) ? "1" : label);
		initJEPExpressionFromLabel();
	}

	@SuppressWarnings("unchecked")
	private void initJEPExpressionFromLabel() {
		this.expression = new JEP();
		this.expression.setAllowUndeclared(true);
		String str = transformToJepVar(getLabel());
		this.expression.parseExpression(str);
		Set<String> v = (Set<String>) this.expression.getSymbolTable().keySet();
		for (String s : v) {
			this.symbolTable.put(transformToOutVar(s), s);
		}

	}

	private String transformToJepVar(String in) {
		String str = in.replace(CepVariable.getSeperator(), "ä");
		str = str.replace("-", "ß");
		str = str.replace("[", "ö");
		return str.replace("]", "ü");
	}

	private String transformToOutVar(String out) {
		String str = out.replace("ä", CepVariable.getSeperator());
		str = str.replace("ß", "-");
		str = str.replace("ö", "[");
		return str.replace("ü", "]");
	}

	public String toString(String indent) {
		String str = indent + "Condition: " + this.hashCode();
		indent += "  ";
		str += indent + "JEP-Expression: " + this.hashCode();
		indent += "  ";
		str += indent + this.expression.getValue();
		return str;
	}

	public Set<String> getVarNames() {
		return symbolTable.keySet();
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
		expression.getVar(symbolTable.get(varName)).setValue(newValue);
	}

	@Override
	public boolean evaluate() {
		/*
		 * C-Semantik: Alles ungleich 0 oder null ist true! JEP tut komische
		 * Dinge: - Vergleichsoperatoren liefern Boolean-Objekte und NaN
		 * getValue() - alle anderen Operatoren liefern Double-Objekte (auch
		 * fÃ¼r boolesche Operatoren, immer 0.0 oder 1.0)
		 */
		double conditionValue = expression.getValue();

		if (Double.isNaN(conditionValue)) {
			Boolean boolVal = (Boolean) expression.getValueAsObject();
			if (boolVal != null) {
				conditionValue = boolVal.booleanValue() ? 1.0 : 0.0;
			} else {

				for (String v : getVarNames()) {
					System.err.println("Variable " + v + " "
							+ expression.getVar(symbolTable.get(v)).getValue());
				}
				ConditionEvaluationException e = new ConditionEvaluationException();
				e.fillInStackTrace();
				throw e;
			}
		}
		return (conditionValue != 0.0);
	}

	@Override
	public void append(String fullExpression) {
		String curLabel = getLabel();
		if (curLabel == null || curLabel.length() == 0 || "1".equals(curLabel)) {
			setLabel(fullExpression);
		} else {
			setLabel(curLabel + " && " + fullExpression);
		}
	}

	@Override
	public void negate() {
		if (negate) negate = false; else negate = true;
		String curLabel = getLabel();
		setLabel("!(" + curLabel + ")");
	}
	
	@Override
	public boolean isNegate() {
		return negate;
	}
}
