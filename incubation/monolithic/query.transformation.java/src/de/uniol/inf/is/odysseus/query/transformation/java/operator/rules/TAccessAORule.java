package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.transformation.java.operator.JavaAccessPO;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TAccessAORule extends AbstractRule{

	public TAccessAORule() {
		super("TAccessAoRule", "java");
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof AbstractAccessAO){
			
			AbstractAccessAO operator = (AbstractAccessAO) logicalOperator;
			if (operator.getWrapper() != null) {
				if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
					return true;
				}
				if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator.getWrapper())) {
					return true;
				}
			}
		}
	
		return false;
	}

	@Override
	public IOperator getOperatorTransformation() {
		return new JavaAccessPO();
	}

	@Override
	public Class<?> getConditionClass() {
		return AbstractAccessAO.class;
	}

}