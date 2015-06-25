package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;


public class JavaProjectOperator extends AbstractTransformationOperator{
	
  private final String name =  "ProjectAO";
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
		
		/*
		RelationalProjectPO project = new RelationalProjectPO();
		project.setName(operator.getName());
		
		
		*/
		
		
		
		StringBuilder code = new StringBuilder();
		code.append("RelationalProjectPO project = new RelationalProjectPO");
		return code.toString();
	}


	
	
	
}
