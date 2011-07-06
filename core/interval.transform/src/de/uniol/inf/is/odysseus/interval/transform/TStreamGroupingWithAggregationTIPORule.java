/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;
import de.uniol.inf.is.odysseus.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TStreamGroupingWithAggregationTIPORule extends AbstractTransformationRule<AggregateAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(AggregateAO aggregateAO, TransformationConfiguration transformConfig) {
		StreamGroupingWithAggregationPO po = new StreamGroupingWithAggregationPO(aggregateAO.getInputSchema(), aggregateAO.getOutputSchema(), aggregateAO.getGroupingAttributes(),
				aggregateAO.getAggregations());
		po.setOutputSchema(aggregateAO.getOutputSchema()); 
		po.setDumpAtValueCount(aggregateAO.getDumpAtValueCount());
		po.setMetadataMerge(new CombinedMergeFunction());
		// ACHTUNG: Die Zeit-Metadaten werden manuell in der Aggregation gesetzt!!
		//((CombinedMergeFunction) po.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());

		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(aggregateAO, po);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		insert(po);
		retract(aggregateAO);

	}

	@Override
	public boolean isExecutable(AggregateAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AggregateAO -> StreamGroupingWithAggregationPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return AggregateAO.class;
	}

}
