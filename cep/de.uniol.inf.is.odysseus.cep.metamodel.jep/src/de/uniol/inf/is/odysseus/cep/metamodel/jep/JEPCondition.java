package de.uniol.inf.is.odysseus.cep.metamodel.jep;

import java.awt.Label;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.nfunk.jep.JEP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.ConditionEvaluationException;
import de.uniol.inf.is.odysseus.cep.metamodel.AbstractCondition;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;

abstract public class JEPCondition extends AbstractCondition {

	/**
	 * Referenz auf den Ausdruck der Transitionsbedingung
	 */
	private JEP expression;
	private Map<CepVariable, String> symbolTable = new HashMap<CepVariable, String>();
	private boolean negate = false;
	
	private Logger _logger = null;
	private Logger getLogger(){
		if (_logger == null){
			_logger = LoggerFactory.getLogger(JEPCondition.class);
		}
		return _logger;
	}

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
		String str = in.replace(CepVariable.getSeperator(), "�");
		str = str.replace("-", "�");
		str = str.replace("[", "�");
		return str.replace("]", "�");
	}

	private CepVariable transformToOutVar(String out) {
		String str = out.replace("�", CepVariable.getSeperator());
		str = str.replace("�", "-");
		str = str.replace("�", "[");
		str.replace("�", "]");
		return new CepVariable(str);
	}

	public String toString(String indent) {
		String str = indent + "Condition: " + this.hashCode();
		indent += "  ";
		str += indent + "JEP-Expression: " + this.hashCode();
		indent += "  ";
		str += indent + this.expression.getValue();
		return str;
	}

	public Set<CepVariable> getVarNames() {
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

	protected void setValue_internal(CepVariable varName, Object newValue) {
	//	getLogger().debug(this+" setValue "+varName+" to "+newValue);
		expression.getVar(symbolTable.get(varName)).setValue(newValue);
	}


	@Override
	public boolean evaluate(int eventTypePort) {
		boolean ret = checkEventTypeWithPort(eventTypePort) && evaluate();		
		return negate? !ret:ret;
	}
	
	@Override
	public boolean evaluate(String eventType) {
		boolean ret = checkEventType(eventType) && evaluate();		
		return negate? !ret:ret;
	}
	
	
	private boolean evaluate() {
		/*
		 * C-Semantik: Alles ungleich 0 oder null ist true! JEP tut komische
		 * Dinge: - Vergleichsoperatoren liefern Boolean-Objekte und NaN
		 * getValue() - alle anderen Operatoren liefern Double-Objekte (auch
		 * für boolesche Operatoren, immer 0.0 oder 1.0)
		 */
		double conditionValue = expression.getValue();

		if (Double.isNaN(conditionValue)) {
			Boolean boolVal = (Boolean) expression.getValueAsObject();
			if (boolVal != null) {
				conditionValue = boolVal.booleanValue() ? 1.0 : 0.0;
			} else {

				for (CepVariable v : getVarNames()) {
					getLogger().error("Variable " + v + " "
							+ expression.getVar(symbolTable.get(v)).getValue());
				}
				ConditionEvaluationException e = new ConditionEvaluationException();
				e.fillInStackTrace();
				throw e;
			}
		}
//		if (getLogger().isDebugEnabled()){
//			getLogger().debug("evaluate() --> "+this+" "+conditionValue);
//			if (conditionValue == 0.0){
//				for (CepVariable v : getVarNames()) {
//					System.err.println("Variable " + v + " "
//							+ expression.getVar(symbolTable.get(v)).getValue());
//				}				
//			}
//		}	
		return (conditionValue != 0.0);
	}

	@Override
	public void appendAND(String fullExpression) {
		String curLabel = getLabel();
		if (curLabel == null || curLabel.length() == 0 || "1".equals(curLabel)) {
			setLabel(fullExpression);
		} else {
			setLabel(curLabel + " && " + fullExpression);
		}
	}

	@Override
	public void appendOR(String fullExpression) {
		String curLabel = getLabel();
		if (curLabel == null || curLabel.length() == 0 || "1".equals(curLabel)) {
			setLabel(fullExpression);
		} else {
			setLabel(curLabel + " || " + fullExpression);
		}
	}

	
	@Override
	public void negate() {
		if (negate) negate = false; else negate = true;
	}
	
	@Override
	public boolean isNegate() {
		return negate;
	}
	
	@Override
	public String toString() {
		String ret = (getLabel().equals("1")?"true":getLabel())+(doEventTypeChecking()?" AND type == "+getEventType():"");
		return negate? "!("+ret+")":ret;
	}
}
