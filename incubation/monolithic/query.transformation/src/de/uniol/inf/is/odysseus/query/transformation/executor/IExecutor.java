package de.uniol.inf.is.odysseus.query.transformation.executor;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public interface IExecutor {
	public String getName();
	
	public String getTargetPlatform();
	
	public void createNeededFiles(TransformationParameter parameter);
	
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> sourceOPs);
}
