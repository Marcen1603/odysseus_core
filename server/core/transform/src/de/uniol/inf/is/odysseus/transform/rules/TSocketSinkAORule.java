/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
 ******************************************************************************/
package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SocketSinkAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ISinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ObjectSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@Deprecated
public class TSocketSinkAORule extends AbstractTransformationRule<SocketSinkAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SocketSinkAO operator,
			TransformationConfiguration config) throws RuleException {
		try {

			// Is this sink already translated?
			ISink<?> socketSinkPO = getDataDictionary().getSinkplan(new Resource(getCaller().getUser(), operator
					.getSinkName()));

			if (socketSinkPO == null) {
				
				ISinkStreamHandlerBuilder streamHandler = getStreamHandler(operator.getSinkType());
				
				if (streamHandler == null){
					throw new TransformationException("No Handler for sink type "+operator.getSinkType()+" found.");
				}
				
				socketSinkPO = new SocketSinkPO(operator.getSinkPort(), operator.getHost(),
						streamHandler, false, false,
						operator.isLoginNeeded(), null, operator.getConnectToServer());

				socketSinkPO.setOutputSchema(operator.getOutputSchema());
				getDataDictionary().putSinkplan(new Resource(getCaller().getUser(), operator
						.getSinkName()), socketSinkPO);
			}
			defaultExecute(operator, socketSinkPO, config, true, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public ISinkStreamHandlerBuilder getStreamHandler(String type) {
		if (type.equalsIgnoreCase("object")) {
			return new ObjectSinkStreamHandlerBuilder();
		}
		return null;
	}

	@Override
	public boolean isExecutable(SocketSinkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet()
				&& operator.getSinkType().equalsIgnoreCase("object");
	}

	@Override
	public String getName() {
		return "SocketSinkAO -> SocketSinkPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SocketSinkAO> getConditionClass() {
		return SocketSinkAO.class;
	}

}
