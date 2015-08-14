package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.java.operator.JavaSelectPO;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TSelectAORule extends AbstractRule{

	public TSelectAORule() {
		super("SelectAO", "Java");
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if(logicalOperator instanceof SelectAO){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public IOperator getOperatorTransformation() {
		return new JavaSelectPO();
	}

	@Override
	public Class<?> getConditionClass() {
		return SelectAO.class;
	}

}
