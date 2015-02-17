package de.uniol.inf.is.odysseus.trajectory.transform;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.LevelDBEnrichAO;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryConstructAO;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryIdEnricherAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


/**
 * 
 * @author marcus
 *
 */
public class TTrajectoryContructAORule extends AbstractTransformationRule<TrajectoryConstructAO> {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(TTrajectoryContructAORule.class);
	
	
	@Override
	public void execute(TrajectoryConstructAO operator, TransformationConfiguration config) {
		
		UnaryLogicalOp op = operator;
		final List<SDFAttribute> groupOrPartitionBy = Arrays.asList(operator.getTrajectoryId());
		
		if(!operator.getSubtrajectories()) {
			op = this.insertPredicateWindowAO(op, groupOrPartitionBy);
			op = this.insertAggregateAO(op, groupOrPartitionBy, Arrays.asList((AggregateItem)operator.getPositionMapping()));
			op = this.insertTrajectoryIdEnricherAO(op);
			if(operator.getLevelDBPath() != null) {
				op = this.insertLevelDBEnrichAO(op, operator.getTrajectoryId(), operator.getOutputSchema().getAttribute(3), operator.getLevelDBPath());
			}
			this.insertSystemTimeAO(op);
		} else {
			op = this.insertAggregateAO(op, groupOrPartitionBy, Arrays.asList((AggregateItem)operator.getPositionMapping()));
			op = this.insertTrajectoryIdEnricherAO(op);
			if(operator.getLevelDBPath() != null) {
				this.insertLevelDBEnrichAO(op, operator.getTrajectoryId(), operator.getOutputSchema().getAttribute(3), operator.getLevelDBPath());
			}
		}
		
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
	
	private final UnaryLogicalOp insertAggregateAO(
			final UnaryLogicalOp operatorBefore, 
			final List<SDFAttribute> groupBy, 
			final List<AggregateItem> aggregates) {
		
		final AggregateAO aggregateAO = new AggregateAO();
		
		RestructHelper.insertOperatorBefore(aggregateAO, operatorBefore);
		
		aggregateAO.setGroupingAttributes(groupBy);
		aggregateAO.setAggregationItems(aggregates);
		
		this.insert(aggregateAO);
		
		return aggregateAO;
	}
	
	private final UnaryLogicalOp insertTrajectoryIdEnricherAO(final UnaryLogicalOp operatorBefore) {
		final UnaryLogicalOp trajectoryConstructAO = new TrajectoryIdEnricherAO();
		
		RestructHelper.insertOperatorBefore(trajectoryConstructAO, operatorBefore);
		this.insert(trajectoryConstructAO);
		
		return trajectoryConstructAO;
	}
	
	private final UnaryLogicalOp insertLevelDBEnrichAO(final UnaryLogicalOp operatorBefore, 
			final SDFAttribute in, final SDFAttribute out, File levelDBPath) {
		final LevelDBEnrichAO levelDBEnrichAO = new LevelDBEnrichAO();
		levelDBEnrichAO.setIn(in);
		levelDBEnrichAO.setOut(out);
		levelDBEnrichAO.setLevelDBPath(levelDBPath);
		
		RestructHelper.insertOperatorBefore(levelDBEnrichAO, operatorBefore);
		this.insert(levelDBEnrichAO);
		
		return levelDBEnrichAO;
	}
	
	private final UnaryLogicalOp insertSystemTimeAO(final UnaryLogicalOp operatorBefore) {
		final TimestampAO timestampAO = new TimestampAO();
		timestampAO.setName(timestampAO.getStandardName());
		timestampAO.setClearEnd(true);
				
		RestructHelper.insertOperatorBefore(timestampAO, operatorBefore);
		this.insert(timestampAO);
		
		return timestampAO;
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
