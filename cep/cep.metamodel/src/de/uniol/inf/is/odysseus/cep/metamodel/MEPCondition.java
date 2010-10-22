package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.ParseException;
import de.uniol.inf.is.odysseus.mep.Variable;

public abstract class MEPCondition extends AbstractCondition{

	
	/**
	 * Referenz auf den Ausdruck der Transitionsbedingung
	 */
	private IExpression<Boolean> expression;
	protected Map<CepVariable, Variable> symbolTable = new HashMap<CepVariable, Variable>();
	private boolean negate = false;
	
	static private Logger _logger = null;
	@SuppressWarnings("unused")
	static private Logger getLogger(){
		if (_logger == null){
			_logger = LoggerFactory.getLogger(MEPCondition.class);
		}
		return _logger;
	}
	
	public MEPCondition(String mepExpression) {
		setLabel(mepExpression);
	}

	@Override
	public void setLabel(String label) {
		super.setLabel((label == null || label.length() == 0) ? "1" : label);
		try {
			expression = MepHelper.initMEPExpressionFromLabel(label, symbolTable);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString(String indent) {
		String str = indent + "Condition: " + this.hashCode();
		indent += "  ";
		str += indent + "MEP-Expression: " + this.hashCode();
		indent += "  ";
		str += indent + this.expression.getValue();
		return str;
	}

	@Override
	public Set<CepVariable> getVarNames() {
		return symbolTable.keySet();
	}

	@Override
	public Boolean getValue() {
		return expression.getValue();
	}

	@Override
	public void setValue(CepVariable varName, Object newValue) {
		symbolTable.get(varName).bind(newValue);
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
		return expression.getValue();
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
	public void addAssignment(String attribute, String fullexpression) {
		// TODO: Implement Assignment
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
