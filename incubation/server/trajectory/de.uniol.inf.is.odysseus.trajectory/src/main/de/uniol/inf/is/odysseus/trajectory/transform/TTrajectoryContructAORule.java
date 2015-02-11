package de.uniol.inf.is.odysseus.trajectory.transform;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryConstructAO;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder.NestAggregateItem;
import de.uniol.inf.is.odysseus.trajectory.physical.TrajectoryConstructPO;
import de.uniol.inf.is.odysseus.trajectory.physical.construct.DefaultTrajectoryConstructStrategy;
import de.uniol.inf.is.odysseus.trajectory.physical.construct.FulltrajectoryConstructStrategy;
import de.uniol.inf.is.odysseus.trajectory.physical.construct.ITrajectoryConstructStrategy;
import de.uniol.inf.is.odysseus.trajectory.physical.construct.SubtrajectoryConstructStrategy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


/**
 * 
 * @author marcus
 *
 */
public class TTrajectoryContructAORule extends AbstractTransformationRule<TrajectoryConstructAO> {

	private final static Logger LOGGER = LoggerFactory.getLogger(TTrajectoryContructAORule.class);
	
	
	@Override
	public void execute(TrajectoryConstructAO operator, TransformationConfiguration config) {
		
		UnaryLogicalOp op = operator;
		final List<SDFAttribute> groupOrPartitionBy = Arrays.asList(operator.getTrajectoryId());
		
		if(!operator.getSubtrajectories()) {
			op = this.insertPredicateWindowAO(op, groupOrPartitionBy);
		}
		
		this.insertAggregateAO(op, groupOrPartitionBy, Arrays.asList((AggregateItem)operator.getPositionMapping()));
	
		RestructHelper.removeOperator(operator, false);
		this.retract(operator);
	}

	private final UnaryLogicalOp insertPredicateWindowAO(final UnaryLogicalOp operatorBefore, List<SDFAttribute> groupOrPartitionBy) {
		final PredicateWindowAO predicateWindowAO = new PredicateWindowAO();
		predicateWindowAO.setSameStarttime(true);
		predicateWindowAO.setPartitionBy(groupOrPartitionBy);
		
		final IAttributeResolver attributeResolver = new DirectAttributeResolver(operatorBefore.getInputSchema());
		final RelationalPredicateBuilder builder = new RelationalPredicateBuilder();
		
		final RelationalPredicate predStart = (RelationalPredicate)builder.createPredicate(attributeResolver, "State = 0");
		predStart.init(operatorBefore.getInputSchema(), operatorBefore.getInputSchema());
		predicateWindowAO.setStartCondition(predStart);
		
		final RelationalPredicate predEnd = (RelationalPredicate)builder.createPredicate(attributeResolver, "State = -1");
		predEnd.init(operatorBefore.getInputSchema(), operatorBefore.getInputSchema());
		predicateWindowAO.setEndCondition(predEnd);
		
		predicateWindowAO.setOutputSchema(operatorBefore.getInputSchema());
		
		RestructHelper.insertOperatorBefore(predicateWindowAO, operatorBefore);
		this.insert(predicateWindowAO);
		
		return predicateWindowAO;
	}
	
	private final void insertAggregateAO(final ILogicalOperator operatorBefore, List<SDFAttribute> groupBy, List<AggregateItem> aggregates) {
		final AggregateAO aggregateAO = new AggregateAO();
		
		RestructHelper.insertOperatorBefore(aggregateAO, operatorBefore);
		
		aggregateAO.setGroupingAttributes(groupBy);
		aggregateAO.setAggregationItems(aggregates);
		
		this.insert(aggregateAO);
	}
	
	
	@Override
	public boolean isExecutable(TrajectoryConstructAO operator,
			TransformationConfiguration transformConfig) {
		return true;
	}

	@Override
	public String getName() {
		return "TrajectoryConstructAO -> PO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

}
