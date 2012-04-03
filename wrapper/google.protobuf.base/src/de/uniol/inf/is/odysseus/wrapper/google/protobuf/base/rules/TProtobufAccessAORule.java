package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base.rules;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;

import com.google.protobuf.GeneratedMessage;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.google.protobuf.base.ChannelHandlerReceiverPO;
import de.uniol.inf.is.odysseus.wrapper.google.protobuf.base.GeneratedMessageToTuple;
import de.uniol.inf.is.odysseus.wrapper.google.protobuf.base.ProtobufTypeRegistry;

public class TProtobufAccessAORule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AccessAO operator, TransformationConfiguration config) {
		String accessPOName = operator.getSourcename();
		ChannelHandlerReceiverPO<?, ?> accessPO = null;

		SocketAddress socketAddress = new InetSocketAddress("localhost",
				operator.getPort());
		GeneratedMessage msg = ProtobufTypeRegistry.getInstance().getMessageType(operator.getOptionsMap().get("type"));
		if (msg == null){
			throw new RuntimeException( new TransformationException("No valid type given: " +operator.getOptionsMap().get("type")));
		}
		ITransformer transformer = GeneratedMessageToTuple.getInstance();
		accessPO = new ChannelHandlerReceiverPO(socketAddress, msg, transformer);

		accessPO.setOutputSchema(operator.getOutputSchema());
		IDataDictionary dd = getDataDictionary();
		dd.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = config
				.getTransformationHelper().replace(operator, accessPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(operator);
		insert(accessPO);

	}

	@Override
	public boolean isExecutable(AccessAO operator,
			TransformationConfiguration config) {
		return (operator.getAdapter() != null && operator.getAdapter()
				.equalsIgnoreCase("GoogleProtoBuf"));
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
	public Class<?> getConditionClass() {
		return AccessAO.class;
	}

}
