package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCRelationalProjectAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;


//TProjectAORule
public class CRelationalProjectPORule extends AbstractCRelationalProjectAORule<ProjectAO>{
	
	public CRelationalProjectPORule() {
		super(CRelationalProjectPORule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(ProjectAO operator) {
		CodeFragmentInfo projectPO = new CodeFragmentInfo();
		

		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		ProjectAO projectAO = (ProjectAO)operator;
	
		int[] restrictList = projectAO.determineRestrictList();
		
	
		StringTemplate relationalProjectPOTemplate = new StringTemplate("operator","relationalProjectPO");
		
		relationalProjectPOTemplate.getSt().add("restrictList", restrictList);
		relationalProjectPOTemplate.getSt().add("operatorVariable", operatorVariable);
		
	
		projectPO.addCode(relationalProjectPOTemplate.getSt().render());
		projectPO.addFrameworkImport(RelationalProjectPO.class.getName());

		return projectPO;
	}


	
}
