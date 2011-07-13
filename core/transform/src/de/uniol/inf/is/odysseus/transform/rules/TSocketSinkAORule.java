package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.SocketSinkAO;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.sink.ISinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.sink.ObjectSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSocketSinkAORule extends AbstractTransformationRule<SocketSinkAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SocketSinkAO operator,
			TransformationConfiguration config) {
		try {
			ISinkStreamHandlerBuilder streamHandlerFac = null;
			ISink<?> socketSinkPO = new SocketSinkPO(operator.getSinkPort(),
					getStreamHandler(operator.getSinkType()), false, null);

			socketSinkPO.setOutputSchema(operator.getOutputSchema());
			Collection<ILogicalOperator> toUpdate = config
					.getTransformationHelper().replace(operator, socketSinkPO);
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}

			retract(operator);
			insert(socketSinkPO);
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
	public Class<?> getConditionClass() {
		return SocketSinkAO.class;
	}

}
