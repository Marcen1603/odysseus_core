/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;

public abstract class MEPCondition extends AbstractCondition{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6018967395205581050L;
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

	@SuppressWarnings("unchecked")
	@Override
	public void setLabel(String label){
		super.setLabel((label == null || label.length() == 0) ? "true" : label);
//		try {
			expression = MepHelper.initMEPExpressionFromLabel(getLabel(), symbolTable);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
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
	
	
	public boolean evaluate() {
		return expression.getValue();
	}

	@Override
	public void appendAND(String fullExpression) {
		String curLabel = getLabel();
		if (curLabel == null || curLabel.length() == 0 || "true".equals(curLabel)) {
			setLabel(fullExpression);
		} else {
			setLabel(curLabel + " && " + fullExpression);
		}
	}

	@Override
	public void appendOR(String fullExpression) {
		String curLabel = getLabel();
		if (curLabel == null || curLabel.length() == 0 || "true".equals(curLabel)) {
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
		String ret = (getLabel().equals("1")?"true":getLabel())+(doEventTypeChecking()?" && type == "+getEventType():"");
		return negate? "!("+ret+")":ret;
	}

}
