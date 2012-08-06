package de.uniol.inf.is.odysseus.dbenrich.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dbenrich.logicaloperator.DBEnrichAO;
import de.uniol.inf.is.odysseus.dbenrich.physicaloperator.DBEnrichPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

//@SuppressWarnings({ "rawtypes" })
public class TDBEnrichAORule extends AbstractTransformationRule<DBEnrichAO> {

	@Override
	public int getPriority() {
		return 0; // No prioritization
	}

	@Override
	public void execute(DBEnrichAO logical, TransformationConfiguration transformConfig) {
		// System.out.println("transform; " + logical.getDebugString());

		// Maybe check, if operator is already existent (when is it 100% equal?)
		DBEnrichPO<ITimeInterval> physical = new DBEnrichPO<ITimeInterval>(
				logical.getConnectionName(),
				logical.getQuery(),
				logical.getAttributes(),
				logical.getCacheSize(),
				logical.getCachingStrategy());

		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, transformConfig);
		retract(logical);
	}

	@Override
	public boolean isExecutable(DBEnrichAO logical, TransformationConfiguration config) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DBEnrichAO -> DBEnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
