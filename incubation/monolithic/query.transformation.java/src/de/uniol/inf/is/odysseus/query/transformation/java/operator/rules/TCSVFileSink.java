package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.transformation.java.operator.JavaCSVFileSink;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TCSVFileSink extends AbstractRule {
	
	public TCSVFileSink() {
		super("TSenderAOGenericRule", "Java");
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof AbstractSenderAO){
			
			AbstractSenderAO operator = (AbstractSenderAO) logicalOperator;
			
				if (operator.getWrapper() != null) {
						if (Constants.GENERIC_PULL.equalsIgnoreCase(operator
								.getWrapper())) {
							return true;
						}
						if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator
								.getWrapper())) {
							return true;
						}
				}
	
			return false;
			
		}
		
		return false;
	}

	@Override
	public IOperator getOperatorTransformation() {
		return new JavaCSVFileSink();
	}

	@Override
	public Class<?> getConditionClass() {
		return AbstractSenderAO.class;
	}


}
