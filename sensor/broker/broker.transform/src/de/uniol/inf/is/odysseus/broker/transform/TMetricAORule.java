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
package de.uniol.inf.is.odysseus.broker.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.metric.MetricMeasureAO;
import de.uniol.inf.is.odysseus.broker.metric.MetricMeasurePO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMetricAORule extends AbstractTransformationRule<MetricMeasureAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	@SuppressWarnings({"unchecked","rawtypes"})
	public void execute(MetricMeasureAO metricMeasureAO, TransformationConfiguration transformConfig) {
		LoggerSystem.printlog(Accuracy.DEBUG, "Transform MetricMeasureAO"); 
		MetricMeasurePO<?> metricMeasurePO = new MetricMeasurePO(metricMeasureAO.getOnAttribute());
		metricMeasurePO.setOutputSchema(metricMeasureAO.getOutputSchema());						
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(metricMeasureAO,metricMeasurePO);							
		for (ILogicalOperator o:toUpdate){
			update(o);
		}				
		retract(metricMeasureAO);	
		
	}

	@Override
	public boolean isExecutable(MetricMeasureAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();		
	}

	@Override
	public String getName() {
		return "MetricMeasureAO -> MetricMeasurePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
