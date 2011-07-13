package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.SocketSinkAO;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.ObjectHandler;
import de.uniol.inf.is.odysseus.physicaloperator.sink.ByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.sink.ISinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleDataHandler;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalSocketSinkAORule extends AbstractTransformationRule<SocketSinkAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(SocketSinkAO operator,
			TransformationConfiguration config) {
		ISinkStreamHandlerBuilder streamHandlerFac = null;
		IAtomicDataHandler handler = new RelationalTupleDataHandler(
				operator.getOutputSchema());
		ObjectHandler<RelationalTuple<ITimeInterval>> objectHandler = new ObjectHandler<RelationalTuple<ITimeInterval>>(
				handler);
		ISink<?> socketSinkPO = new SocketSinkPO(operator.getSinkPort(),getStreamHandler(operator), true, objectHandler);
		
		socketSinkPO.setOutputSchema(operator.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, socketSinkPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		retract(operator);
		insert(socketSinkPO);		
	}
	
	public ISinkStreamHandlerBuilder getStreamHandler(SocketSinkAO operator){
		if (operator.getSinkType().equalsIgnoreCase("bytebuffer")){
			return new ByteBufferSinkStreamHandlerBuilder();
		}
		return null;
	}

	@Override
	public boolean isExecutable(SocketSinkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && operator.getSinkType().equalsIgnoreCase("bytebuffer");
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
	public Class<?> getConditionClass() {
		return SocketSinkAO.class;
	}

}
