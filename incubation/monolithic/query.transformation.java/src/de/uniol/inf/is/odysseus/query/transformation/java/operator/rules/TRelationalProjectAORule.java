package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;


//TProjectAORule
public class TRelationalProjectAORule extends AbstractRule{
	
	public TRelationalProjectAORule() {
		super(TRelationalProjectAORule.class.getName(), "java");
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof ProjectAO){
			ProjectAO operator = (ProjectAO) logicalOperator;
			
			if (operator.getInputSchema().getType() == Tuple.class) {
					return true;
			}
		
			return false;
		}
		
		return false;

	}



	@Override
	public Class<?> getConditionClass() {
		return RelationalProjectPO.class;
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
	CodeFragmentInfo projectPO = new CodeFragmentInfo();
		

		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
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
