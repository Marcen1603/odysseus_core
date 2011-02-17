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
package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterCovarianceUpdatePO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterEstimateUpdatePO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterGainPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFilterAORule extends AbstractTransformationRule<FilterAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(FilterAO operator, TransformationConfiguration config) {

		System.out.print("CREATE Filter PO...");

		Vector<Object> functions = (Vector<Object>) operator.getFunctions();
		
		FilterEstimateUpdatePO filterEstimateUpdatePO;
		FilterCovarianceUpdatePO filterCovariancePO;
		FilterGainPO filterGainPO;
		
		for (int i=0; i<=functions.size(); i++) {
			if (functions.elementAt(i) instanceof AbstractDataUpdateFunction) {
				filterEstimateUpdatePO = new FilterEstimateUpdatePO();
				filterEstimateUpdatePO.setOutputSchema(operator.getOutputSchema());
				filterEstimateUpdatePO.setOldObjListPath(operator.getOldObjListPath());
				filterEstimateUpdatePO.setNewObjListPath(operator.getNewObjListPath());
				filterEstimateUpdatePO.setDataUpdateFunction((AbstractDataUpdateFunction) functions.elementAt(i));
				filterEstimateUpdatePO.setParameters(operator.getParameters());
				
				Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterEstimateUpdatePO);
					for (ILogicalOperator o:toUpdate)
				{
				update(o);
				}
				insert(filterEstimateUpdatePO);
				
				} else if (functions.elementAt(i) instanceof AbstractMetaDataCreationFunction) {
				filterGainPO = new FilterGainPO();
				filterGainPO.setOutputSchema(operator.getOutputSchema());
				filterGainPO.setMetaDataCreationFunction((AbstractMetaDataCreationFunction) functions.elementAt(i));
				filterGainPO.setParameters(operator.getParameters());
				
				Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterGainPO);
					for (ILogicalOperator o:toUpdate)
					{
					update(o);
					}
				insert(filterGainPO);
				
			} else if (functions.elementAt(i) instanceof AbstractMetaDataUpdateFunction) {
				filterCovariancePO = new FilterCovarianceUpdatePO();
				filterCovariancePO.setOutputSchema(operator.getOutputSchema());
				filterCovariancePO.setOldObjListPath(operator.getOldObjListPath());
				filterCovariancePO.setNewObjListPath(operator.getNewObjListPath());
				filterCovariancePO.setMetaDataUpdateFunction((AbstractMetaDataUpdateFunction) functions.elementAt(i));
				filterCovariancePO.setParameters(operator.getParameters());
				
				Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterCovariancePO);
					for (ILogicalOperator o:toUpdate)
					{
					update(o);
					}	
				insert(filterCovariancePO);
			
			}
			
			
		} 
			
		retract(operator);
		
		System.out.println("CREATE Filter AO finished.");
		
	}

	@Override
	public boolean isExecutable(FilterAO operator,
			TransformationConfiguration config) {
		Set<String> metaTypes = new HashSet<String>();
		metaTypes.add(ILatency.class.getCanonicalName());
		metaTypes.add(IProbability.class.getCanonicalName());
		metaTypes.add(IPredictionFunctionKey.class.getCanonicalName());
		
		if(config.getMetaTypes().containsAll(metaTypes) && 
				operator.isAllPhysicalInputSet()){
			return true;
		}
		return false;
		
		//DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		$filterAO : FilterAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "FilterAO -> FilterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
