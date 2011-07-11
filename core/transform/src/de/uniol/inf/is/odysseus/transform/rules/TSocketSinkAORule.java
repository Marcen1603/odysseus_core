package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.SocketSinkAO;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.sink.IStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.sink.ObjectStreamHandlerBuilder;
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
		// TODO: Generischer machen. Zunaechst einfach nur object
		IStreamHandlerBuilder streamHandlerFac = null;
		if (operator.getSinkType().equalsIgnoreCase("object")){
			streamHandlerFac = new ObjectStreamHandlerBuilder();
		}
		ISink<?> socketSinkPO = new SocketSinkPO(operator.getSinkPort(),streamHandlerFac);
		
		socketSinkPO.setOutputSchema(operator.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, socketSinkPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		retract(operator);
		insert(socketSinkPO);		
	}

	@Override
	public boolean isExecutable(SocketSinkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
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
