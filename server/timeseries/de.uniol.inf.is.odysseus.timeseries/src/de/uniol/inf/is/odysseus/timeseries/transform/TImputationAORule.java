package de.uniol.inf.is.odysseus.timeseries.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.ImputationAO;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.RegularTimeSeriesAO;
import de.uniol.inf.is.odysseus.timeseries.physicaloperator.ImputationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class TImputationAORule extends AbstractTransformationRule<ImputationAO> {

	@Override
	public void execute(final ImputationAO operator, final TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new ImputationPO(operator.getImputationWindowSize()), config, true, false);
	}

	@Override
	public boolean isExecutable(final ImputationAO operator, final TransformationConfiguration config) {
		// QN: Welche Bedeutung hat dies?
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {

		// QN: Welche Bedeutung hat dies?
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
