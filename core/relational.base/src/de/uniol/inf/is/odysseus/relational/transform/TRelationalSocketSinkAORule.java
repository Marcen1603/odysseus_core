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
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SocketSinkAO;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ISinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalSocketSinkAORule extends
		AbstractTransformationRule<SocketSinkAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(SocketSinkAO operator,
			TransformationConfiguration config) {

		// Is this sink already translated?
		ISink<?> socketSinkPO = getDataDictionary().getSink(operator
				.getSinkName());

		if (socketSinkPO == null) {

			IDataHandler<?> handler = new TupleDataHandler().getInstance(operator.getOutputSchema());
			ByteBufferHandler<Tuple<ITimeInterval>> objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(
					handler);
			socketSinkPO = new SocketSinkPO(operator.getSinkPort(),
					getStreamHandler(operator), true, operator.isLoginNeeded(),
					objectHandler);

			socketSinkPO.setOutputSchema(operator.getOutputSchema());
			getDataDictionary().putSink(operator.getName(), socketSinkPO);
		}
		Collection<ILogicalOperator> toUpdate = config
				.getTransformationHelper().replace(operator, socketSinkPO,true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		retract(operator);
		insert(socketSinkPO);
	}

	public ISinkStreamHandlerBuilder getStreamHandler(SocketSinkAO operator) {
		if (operator.getSinkType().equalsIgnoreCase("bytebuffer")) {
			return new ByteBufferSinkStreamHandlerBuilder();
		}
		return null;
	}

	@Override
	public boolean isExecutable(SocketSinkAO operator,
			TransformationConfiguration config) {
		return config.getDataType().equals("relational") &&  operator.isAllPhysicalInputSet()
				&& operator.getSinkType().equalsIgnoreCase("bytebuffer");
	}

	@Override
	public String getName() {
		return "SocketSinkAO -> SocketSinkPO (Relational)";
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
