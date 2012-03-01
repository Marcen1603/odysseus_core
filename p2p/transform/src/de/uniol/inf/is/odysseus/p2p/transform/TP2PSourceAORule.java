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
package de.uniol.inf.is.odysseus.p2p.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IdentityTransformation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base.P2PInputStreamAccessPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TP2PSourceAORule extends AbstractTransformationRule<P2PSourceAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	@Override
	public void execute(P2PSourceAO p2pSourceAO, TransformationConfiguration transformConfig) {
		// TODO: Add Username and Password to P2PSourceAO?
		P2PInputStreamAccessPO<?,?> p2pAccessPO = new P2PInputStreamAccessPO(new IdentityTransformation(), p2pSourceAO.getAdv(), PeerGroupTool.getPeerGroup(), null, null);
		p2pAccessPO.setOutputSchema(p2pSourceAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(p2pSourceAO, p2pAccessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(p2pSourceAO);
	}

	@Override
	public boolean isExecutable(P2PSourceAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "P2PSourceAO -> P2PInputStreamAccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
