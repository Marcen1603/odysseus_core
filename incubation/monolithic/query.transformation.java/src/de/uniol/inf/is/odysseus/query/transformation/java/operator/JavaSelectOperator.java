package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorToVariable;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;


public class JavaSelectOperator extends AbstractTransformationOperator{
	
  private final String name =  new SelectAO().getClass().getSimpleName();
  private final String targetPlatform = "Java";
  
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();
		String operatorVariable = OperatorToVariable.getVariable(operator);
		
		
		code.append("Select "+operatorVariable+"SelectPO = new Select();");
		return code.toString();
	}

	@Override
	public Set<String> getNeededImports() {
	
		return null;
	}


	
	
	
}
