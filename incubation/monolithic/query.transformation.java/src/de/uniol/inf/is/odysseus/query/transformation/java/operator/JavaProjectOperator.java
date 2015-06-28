package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;

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
		RelationalProjectPO testProject = new RelationalProjectPO(new int[] {0});
		project.setName(operator.getName());
		
		*/
		
	
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("RelationalProjectPO testProject = new RelationalProjectPO(new int[] {0});");
		code.append("\n");
		code.append("\n");
		return code.toString();
	}
	
	public Set<String> getNeededImports(){
		Set<String> importList = new HashSet<String>();
		importList.add("de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO");
		importList.add("java.util.ArrayList");
		importList.add("java.util.List");

		return importList;
		
	}


	
	
	
}
