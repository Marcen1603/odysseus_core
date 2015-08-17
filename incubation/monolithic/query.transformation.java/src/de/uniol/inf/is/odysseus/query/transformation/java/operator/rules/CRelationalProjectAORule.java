package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractCRelationalProjectAORule;


//TProjectAORule
public class CRelationalProjectAORule extends AbstractCRelationalProjectAORule{
	
	public CRelationalProjectAORule() {
		super(CRelationalProjectAORule.class.getName(), "java");
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo projectPO = new CodeFragmentInfo();
		

		String operatorVariable = OperatorTransformationInformation.getInstance().getVariable(operator);
		
		ProjectAO projectAO = (ProjectAO)operator;
	
		int[] restrictList = projectAO.determineRestrictList();
		
	
		StringTemplate relationalProjectPOTemplate = new StringTemplate("operator","relationalProjectPO");
		
		relationalProjectPOTemplate.getSt().add("restrictList", restrictList);
		relationalProjectPOTemplate.getSt().add("operatorVariable", operatorVariable);
		
	
		projectPO.addCode(relationalProjectPOTemplate.getSt().render());
		projectPO.addImport(RelationalProjectPO.class.getName());

		return projectPO;
	}


	
}
