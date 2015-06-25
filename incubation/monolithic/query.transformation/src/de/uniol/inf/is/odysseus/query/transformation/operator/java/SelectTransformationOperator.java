package de.uniol.inf.is.odysseus.query.transformation.operator.java;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;

public class SelectTransformationOperator extends AbstractTransformationOperator{
	
  private final String name =  new SelectAO().getClass().getSimpleName();
  private final String programLanguage = "Java";
  
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getProgramLanguage() {
		return programLanguage;
	}

	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();
		code.append("Select selecttest = new Select();");
		return code.toString();
	}


	
	
	
}
