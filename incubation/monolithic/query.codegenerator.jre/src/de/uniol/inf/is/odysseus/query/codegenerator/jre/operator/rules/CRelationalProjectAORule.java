package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCRelationalProjectAORule;


//TProjectAORule
public class CRelationalProjectAORule extends AbstractCRelationalProjectAORule<ProjectAO>{
	
	public CRelationalProjectAORule() {
		super(CRelationalProjectAORule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(ProjectAO operator) {
		CodeFragmentInfo projectPO = new CodeFragmentInfo();
		

		String operatorVariable = JreCodegeneratorStatus.getInstance().getVariable(operator);
		
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
