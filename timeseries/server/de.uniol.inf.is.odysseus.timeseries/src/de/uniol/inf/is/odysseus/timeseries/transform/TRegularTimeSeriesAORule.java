package de.uniol.inf.is.odysseus.timeseries.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.RegularTimeSeriesAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class TRegularTimeSeriesAORule extends AbstractTransformationRule<RegularTimeSeriesAO> {

	@Override
	public void execute(final RegularTimeSeriesAO operator, final TransformationConfiguration config)
			throws RuleException {

		// current implementation of previous value-method
		
		// it is a tumbling window
		AbstractWindowWithWidthAO timewindowAO = new TimeWindowAO();
		TimeValueItem timeValueItem = operator.getRegularWindowSize();
		timewindowAO.setWindowSize(timeValueItem);
		timewindowAO.setWindowSlide(timeValueItem);
		
		LogicalPlan.insertOperatorBefore(timewindowAO, operator); 
		insert(timewindowAO);
		
		// Aggregation to the last elements of TimeWindow-Intervall
		AggregateAO aggregateAO = new AggregateAO();
		LogicalPlan.insertOperatorBefore(aggregateAO, timewindowAO); 

		SDFSchema inputSchema = operator.getInputSchema();
		List<SDFAttribute> inputAttributes =  inputSchema.getAttributes();
		
		for (SDFAttribute sdfAttribute : inputAttributes){
			AggregateFunction functionLast = new AggregateFunction("LAST");
			aggregateAO.addAggregation(sdfAttribute, functionLast, sdfAttribute);
		}
		
		insert(aggregateAO);
		
		LogicalPlan.removeOperator(operator, false);
		retract(operator);

	}

	@Override
	public boolean isExecutable(final RegularTimeSeriesAO operator, final TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	@Override
	public String getName() {
		return "RegularTimeSeriesAO -> TimeWindow -> Aggregate";
	}

}
