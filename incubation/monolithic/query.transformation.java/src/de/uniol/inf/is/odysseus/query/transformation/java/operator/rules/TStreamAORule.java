package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.java.operator.JavaReceiverPO;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TStreamAORule extends AbstractRule{
	
	public TStreamAORule() {
		super("TStreamAORule", "Java");
	}
	
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof StreamAO){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public IOperator getOperatorTransformation() {
		return new JavaReceiverPO();
	}

	@Override
	public Class<?> getConditionClass() {
		return StreamAO.class;
	}


}
