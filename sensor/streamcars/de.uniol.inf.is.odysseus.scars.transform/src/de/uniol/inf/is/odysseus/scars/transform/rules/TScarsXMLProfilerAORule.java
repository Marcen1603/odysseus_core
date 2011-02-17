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

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.xmlprofiler.logicaloperator.XMLProfilerAO;
import de.uniol.inf.is.odysseus.scars.xmlprofiler.physicaloperator.XMLProfilerPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TScarsXMLProfilerAORule extends AbstractTransformationRule<XMLProfilerAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(XMLProfilerAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE XMLProfilerPO.");
		XMLProfilerPO xmlProfilerPO = new XMLProfilerPO();
		xmlProfilerPO.setOperatorName(operator.getOperatorName());
		xmlProfilerPO.setFileName(operator.getFileName());
		xmlProfilerPO.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, xmlProfilerPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(xmlProfilerPO);
		retract(operator);
		System.out.println("CREATE XMLProfilerPO finished.");
		
	}

	@Override
	public boolean isExecutable(XMLProfilerAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		$xmlProfilerAO : XMLProfilerAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "XMLProfilerAO -> XMLProfilerPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
