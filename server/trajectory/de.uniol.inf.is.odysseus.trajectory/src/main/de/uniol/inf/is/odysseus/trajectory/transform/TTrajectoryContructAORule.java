/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.transform;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.LevelDBEnrichAO;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryConstructAO;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryIdEnrichAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


/**
 * This Rule substitutes a <tt>TrajectoryConstructAO</tt> by various other logical operators based
 * on the input.
 * 
 * @author marcus
 *
 */
public class TTrajectoryContructAORule extends AbstractTransformationRule<TrajectoryConstructAO> {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(TTrajectoryContructAORule.class);
	
	
	@Override
	public void execute(final TrajectoryConstructAO operator, final TransformationConfiguration config) {
		
		UnaryLogicalOp op = operator;
		final List<SDFAttribute> groupOrPartitionBy = Arrays.asList(operator.getTrajectoryId());
		
		if(!operator.getSubtrajectories()) {
			op = this.insertPredicateWindowAO(op, groupOrPartitionBy);
			op = this.insertAggregateAO(op, groupOrPartitionBy, Arrays.asList((AggregateItem)operator.getPositionMapping()));
			op = this.insertTrajectoryIdEnrichAO(op);
			if(operator.getLevelDBPath() != null) {
				op = this.insertLevelDBEnrichAO(op, operator.getTrajectoryId(), operator.getOutputSchema().getAttribute(3), operator.getLevelDBPath());
			}
			this.insertSystemTimeAO(op);
		} else {
			op = this.insertAggregateAO(op, groupOrPartitionBy, Arrays.asList((AggregateItem)operator.getPositionMapping()));
			op = this.insertTrajectoryIdEnrichAO(op);
			if(operator.getLevelDBPath() != null) {
				this.insertLevelDBEnrichAO(op, operator.getTrajectoryId(), operator.getOutputSchema().getAttribute(3), operator.getLevelDBPath());
			}
		}
		
		LogicalPlan.removeOperator(operator, false);
		this.retract(operator);
	}

	private final UnaryLogicalOp insertPredicateWindowAO(final UnaryLogicalOp operatorBefore, final List<SDFAttribute> groupOrPartitionBy) {
		final PredicateWindowAO predicateWindowAO = new PredicateWindowAO();
				
		predicateWindowAO.setSameStarttime(true);
		predicateWindowAO.setPartitionBy(groupOrPartitionBy);
		
		final IAttributeResolver attributeResolver = new DirectAttributeResolver(operatorBefore.getInputSchema());
		@SuppressWarnings("rawtypes")
		final RelationalPredicateBuilder builder = new RelationalPredicateBuilder();
		
		final RelationalExpression<?> predStart = (RelationalExpression<?>)builder.createPredicate(attributeResolver, "State = 0");
		predStart.initVars(operatorBefore.getInputSchema(), operatorBefore.getInputSchema());
		predicateWindowAO.setStartCondition(predStart);
		
		final RelationalExpression<?> predEnd = (RelationalExpression<?>)builder.createPredicate(attributeResolver, "State = -1");
		predEnd.initVars(operatorBefore.getInputSchema(), operatorBefore.getInputSchema());
		predicateWindowAO.setEndCondition(predEnd);
		predicateWindowAO.setOutputSchema(operatorBefore.getInputSchema());
		
		LogicalPlan.insertOperatorBefore(predicateWindowAO, operatorBefore);
		this.insert(predicateWindowAO);
		
		return predicateWindowAO;
	}
	
	private final UnaryLogicalOp insertAggregateAO(
			final UnaryLogicalOp operatorBefore, 
			final List<SDFAttribute> groupBy, 
			final List<AggregateItem> aggregates) {
		
		final AggregateAO aggregateAO = new AggregateAO();
		
		LogicalPlan.insertOperatorBefore(aggregateAO, operatorBefore);
		
		aggregateAO.setGroupingAttributes(groupBy);
		aggregateAO.setAggregationItems(aggregates);
		
		this.insert(aggregateAO);
		
		return aggregateAO;
	}
	
	private final UnaryLogicalOp insertTrajectoryIdEnrichAO(final UnaryLogicalOp operatorBefore) {
		final UnaryLogicalOp trajectoryConstructAO = new TrajectoryIdEnrichAO();
		
		LogicalPlan.insertOperatorBefore(trajectoryConstructAO, operatorBefore);
		this.insert(trajectoryConstructAO);
		
		return trajectoryConstructAO;
	}
	
	private final UnaryLogicalOp insertLevelDBEnrichAO(final UnaryLogicalOp operatorBefore, 
			final SDFAttribute in, final SDFAttribute out, final File levelDBPath) {
		final LevelDBEnrichAO levelDBEnrichAO = new LevelDBEnrichAO();
		levelDBEnrichAO.setIn(in);
		levelDBEnrichAO.setOut(out);
		levelDBEnrichAO.setLevelDBPath(levelDBPath);
		
		LogicalPlan.insertOperatorBefore(levelDBEnrichAO, operatorBefore);
		this.insert(levelDBEnrichAO);
		
		return levelDBEnrichAO;
	}
	
	private final UnaryLogicalOp insertSystemTimeAO(final UnaryLogicalOp operatorBefore) {
		final TimestampAO timestampAO = new TimestampAO();
		timestampAO.setName(timestampAO.getStandardName());
		timestampAO.setClearEnd(true);
				
		LogicalPlan.insertOperatorBefore(timestampAO, operatorBefore);
		this.insert(timestampAO);
		
		return timestampAO;
	}
	
	
	@Override
	public boolean isExecutable(final TrajectoryConstructAO operator,
			final TransformationConfiguration transformConfig) {
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
