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
package de.uniol.inf.is.odysseus.wrapper.protobuf;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.google.protobuf.GeneratedMessage;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TProtobufAccessAORule extends AbstractTransformationRule<AbstractAccessAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AbstractAccessAO operator, TransformationConfiguration config) {
		
		ChannelHandlerReceiverPO<?, ?> accessPO = null;

		int port = Integer.parseInt(operator.getOptionsMap().get("port"));
		
		SocketAddress socketAddress = new InetSocketAddress("0.0.0.0",port);
		GeneratedMessage msg = ProtobufTypeRegistry.getMessageType(operator.getOptionsMap().get("type"));
		if (msg == null){
			throw new RuntimeException( new TransformationException("No valid type given: " +operator.getOptionsMap().get("type")));
		}
		ITransformer transformer = new GeneratedMessageToTuple().getInstance(operator.getOptionsMap(),operator.getOutputSchema());
		accessPO = new ChannelHandlerReceiverPO(socketAddress, msg, transformer);		
		defaultExecute(operator, accessPO, config, true, true);
	}

	@Override
	public boolean isExecutable(AbstractAccessAO operator,
			TransformationConfiguration config) {
		return (operator.getWrapper() != null && operator.getWrapper()
				.equalsIgnoreCase(Activator.GOOGLE_PROTOBUF));
	}

	@Override
	public String getName() {
		return "AccessAO -> ChannelHandlerReceiverPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<? super AbstractAccessAO> getConditionClass() {
		return AbstractAccessAO.class;
	}

}
