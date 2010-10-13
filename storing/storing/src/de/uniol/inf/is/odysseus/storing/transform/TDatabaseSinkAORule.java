package de.uniol.inf.is.odysseus.storing.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseSinkAO;
import de.uniol.inf.is.odysseus.storing.physicaloperator.DatabaseSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDatabaseSinkAORule extends AbstractTransformationRule<DatabaseSinkAO>{

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(DatabaseSinkAO operator, TransformationConfiguration config) {
		DatabaseSinkPO dbSinkPO = new DatabaseSinkPO(operator.getConnection(), operator.getTable(), operator.isSaveMetaData(), operator.isCreate(),operator.isTruncate(), operator.isIfnotexists());
		dbSinkPO.setOutputSchema(operator.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, dbSinkPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(operator);
		
	}

	@Override
	public boolean isExecutable(DatabaseSinkAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DatabaseSinkAO -> DatabaseSinkPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
