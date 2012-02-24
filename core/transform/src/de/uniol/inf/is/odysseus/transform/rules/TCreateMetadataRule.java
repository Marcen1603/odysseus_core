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
package de.uniol.inf.is.odysseus.transform.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TCreateMetadataRule extends AbstractTransformationRule<ISource<?>> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(ISource source, TransformationConfiguration transformConfig) {
		Class type = MetadataRegistry.getMetadataType(transformConfig.getMetaTypes());
		MetadataCreationPO po = null;
		for(PhysicalSubscription<ISink> sub : (Collection<PhysicalSubscription<ISink>>)source.getSubscriptions()){
			if ((sub.getTarget() instanceof MetadataCreationPO) && ((MetadataCreationPO)sub.getTarget()).getType() == type) {
				po = (MetadataCreationPO)sub.getTarget();
				break;
			}
		}
		if (po == null) {
			po = new MetadataCreationPO(type);
			po.setOutputSchema(source.getOutputSchema());
			source.subscribeSink(po, 0, 0, source.getOutputSchema()); // TODO: Hier wird ignoriert, dass die Source evtl. mehrere Ausgänge hat 
		}
		
		ArrayList<ILogicalOperator> logicalOps = new ArrayList<ILogicalOperator>();
		for(Object o : getCollection()){
			if(o instanceof ILogicalOperator){
				ILogicalOperator oplog = (ILogicalOperator)o;
				if(oplog.getPhysInputPOs().contains(source)){
					logicalOps.add(oplog);
				}
			}
		}
		
		for(ILogicalOperator op : (List<ILogicalOperator>)logicalOps) {
			for (Subscription<ISource<?>> psub : op.getPhysSubscriptionsTo()) {
					if (psub.getTarget() == source){
						op.setPhysSubscriptionTo((ISource)po ,psub.getSinkInPort(), psub.getSourceOutPort(), psub.getSchema());
						update(op);
					}
				}
		}	
		insert(po);		
		retract(source);		
	}

	@Override
	public boolean isExecutable(ISource<?> source, TransformationConfiguration transformConfig) {
		if(!source.isSink()){
			if(!transformConfig.hasOption("NO_METADATA")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Create Metadata";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.CREATE_METADATA;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return ISource.class;
	}

}
