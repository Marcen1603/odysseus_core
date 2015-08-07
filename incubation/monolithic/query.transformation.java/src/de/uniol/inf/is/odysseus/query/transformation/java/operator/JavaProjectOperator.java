package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;


public class JavaProjectOperator extends AbstractTransformationOperator{
	
	public JavaProjectOperator(){
		super(RelationalProjectPO.class, "ProjectAO","Java");
	}
	

	/*
	RelationalProjectPO testProject = new RelationalProjectPO(new int[] {0});
	project.setName(operator.getName());
	
	*/
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo projectPO = new CodeFragmentInfo();
		

		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		ProjectAO projectAO = (ProjectAO)operator;
	
		int[] restrictList = projectAO.determineRestrictList();
		
	
		StringTemplate relationalProjectPOTemplate = new StringTemplate("java","relationalProjectPO");
		
		relationalProjectPOTemplate.getSt().add("restrictList", restrictList);
		relationalProjectPOTemplate.getSt().add("operatorVariable", operatorVariable);
		
	
		projectPO.addCode(relationalProjectPOTemplate.getSt().render());
		projectPO.addImports(getNeededImports());

		return projectPO;
	}


	@Override
	public void defineImports() {
		// TODO Auto-generated method stub
		
	}
	
	
}
