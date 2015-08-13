package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;

public interface IOperatorRule {
	
	public String getName();

	public int getPriority();	
	
	public boolean isExecutable(ILogicalOperator operator, TransformationConfiguration transformationConfiguration);
	
	public IOperator getOperatorTransformation();
	
	public String getTargetPlatform();
	
	public Class<?> getConditionClass();

}
