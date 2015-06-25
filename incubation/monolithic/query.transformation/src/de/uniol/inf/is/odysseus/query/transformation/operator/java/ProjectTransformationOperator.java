package de.uniol.inf.is.odysseus.query.transformation.operator.java;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;

public class ProjectTransformationOperator extends AbstractTransformationOperator{
	
  private final String name =  "ProjectAO";
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
		
		/*
		RelationalProjectPO project = new RelationalProjectPO();
		project.setName(operator.getName());
		
		
		*/
		
		
		
		StringBuilder code = new StringBuilder();
		code.append("RelationalProjectPO project = new RelationalProjectPO");
		return code.toString();
	}


	
	
	
}
