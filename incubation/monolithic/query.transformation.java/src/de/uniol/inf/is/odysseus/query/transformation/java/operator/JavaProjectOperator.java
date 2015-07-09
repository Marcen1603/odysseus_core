package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformIntegerArray;
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

	/*
	RelationalProjectPO testProject = new RelationalProjectPO(new int[] {0});
	project.setName(operator.getName());
	
	*/
	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();
		
	
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		ProjectAO projectAO = (ProjectAO)operator;
	
		int[] restrictList = projectAO.determineRestrictList();
		
		String restrictListCode = TransformIntegerArray.getCodeForIntegerArray(restrictList);
	
		code.append("\n");
		code.append("RelationalProjectPO "+operatorVariable+"PO = new RelationalProjectPO("+restrictListCode+");");
		code.append("\n");
		code.append(operatorVariable+"PO.setOutputSchema("+operatorVariable+"SDFSchema);");
		code.append("\n");
		return code.toString();
	}
	
	@Override
	public Set<String> getNeededImports() {
		Set<String> imoportList = new HashSet<String>();
		imoportList.add(RelationalProjectPO.class.getPackage().getName()+"."+RelationalProjectPO.class.getSimpleName());
		return imoportList;

	}


	
	
	
}
