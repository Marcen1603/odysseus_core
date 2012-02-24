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
package de.uniol.inf.is.odysseus.objecttracking.transform.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.MetadataObjectRelationalCreationPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TMetadataObjectRelationalCreationPORule extends AbstractTransformationRule<ISource>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(ISource operator, TransformationConfiguration config) {

		// DRL-Code
//		logicalOps : ArrayList() from collect(ILogicalOperator(physInputPOs contains source))
		ArrayList logicalOps = new ArrayList();
		for(Object o: super.getCollection()){
			if(o instanceof ILogicalOperator){
				ILogicalOperator logOp = (ILogicalOperator)o;
				Collection physIn = logOp.getPhysInputPOs();
				for(Object inputOp : physIn){
					if(inputOp == operator){
						logicalOps.add(logOp);
						break;
					}
				}
				// is this the same as above?
//				if(logOp.getPhysInputPOs().contains(operator)){
//					logicalOps.add(logOp);
//				}
			}
		}
		
		
		System.out.println("Create ObjectRelational Metadata USED");
		System.out.println("types: " + config.getMetaTypes());
		Class type = MetadataRegistry.getMetadataType(config.getMetaTypes());
		System.out.println("Metadata in use: " + type);
		
		MetadataObjectRelationalCreationPO po = null;
		for(PhysicalSubscription<ISink> sub : (Collection<PhysicalSubscription<ISink>>)operator.getSubscriptions()){
			if ((sub.getTarget() instanceof MetadataCreationPO) && ((MetadataCreationPO)sub.getTarget()).getType() == type) {
				po = (MetadataObjectRelationalCreationPO)sub.getTarget();
				break;
			}
		}
		if (po == null) {
			po = new MetadataObjectRelationalCreationPO(type);
			po.setOutputSchema(operator.getOutputSchema());
			operator.subscribeSink(po, 0, 0, operator.getOutputSchema()); 
		}
		
		for(ILogicalOperator op : (List<ILogicalOperator>)logicalOps) {
			for (Subscription<ISource<?>> psub : op.getPhysSubscriptionsTo()) {
					if (psub.getTarget() == operator){
						op.setPhysSubscriptionTo((ISource)po ,psub.getSinkInPort(), psub.getSourceOutPort(), psub.getSchema());
						update(op);
					}
				}
		}
		
		insert(po);
		
		retract(operator);
		
	}

	@Override
	public boolean isExecutable(ISource operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				!operator.isSink()){
			return true;
		}
		
		return false;
		
		//DRL-Code
//		source : ISource( )
//		eval(!source.isSink())
//		logicalOps : ArrayList() from collect(ILogicalOperator(physInputPOs contains source))
//		
//		$trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
	}

	@Override
	public String getName() {
		return "Create ObjectRelational Metadata";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.CREATE_METADATA;
	}

}
