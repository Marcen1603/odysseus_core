package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.java.operator.JavaRelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;


//TProjectAORule
public class TRelationalProjectAORule extends AbstractRule{
	
	public TRelationalProjectAORule() {
		super("TProjectAORule", "java");
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
	public IOperator getOperatorTransformation() {
		return new JavaRelationalProjectPO();
	}

	@Override
	public Class<?> getConditionClass() {
		return RelationalProjectPO.class;
	}

}
