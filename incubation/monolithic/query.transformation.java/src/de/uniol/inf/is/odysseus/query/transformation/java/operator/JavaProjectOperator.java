package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformIntegerArray;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;


public class JavaProjectOperator extends AbstractTransformationOperator{
	
	public JavaProjectOperator(){
		this.implClass = RelationalProjectPO.class;
		this.name =  "ProjectAO";
		this.targetPlatform = "Java";
		defaultImports();
	}
	

	/*
	RelationalProjectPO testProject = new RelationalProjectPO(new int[] {0});
	project.setName(operator.getName());
	
	*/
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo projectPO = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		ProjectAO projectAO = (ProjectAO)operator;
	
		int[] restrictList = projectAO.determineRestrictList();
		
		String restrictListCode = TransformIntegerArray.getCodeForIntegerArray(restrictList);
		
	
		StringTemplate relationalProjectPOTemplate = new StringTemplate("relationalProjectPO");
		
		relationalProjectPOTemplate.getSt().add("restrictList", restrictList);
		relationalProjectPOTemplate.getSt().add("operatorVariable", operatorVariable);
		
		
		code.append(relationalProjectPOTemplate.getSt().render());
		
		
	
		projectPO.addCode(code.toString());
		projectPO.addImports(getNeededImports());

		return projectPO;
	}


	@Override
	public void defineImports() {
		// TODO Auto-generated method stub
		
	}
	
	
}
