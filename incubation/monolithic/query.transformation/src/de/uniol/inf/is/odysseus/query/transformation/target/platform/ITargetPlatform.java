package de.uniol.inf.is.odysseus.query.transformation.target.platform;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;

public interface ITargetPlatform {
	public String getTargetPlatformName();
	public void convertQueryToStandaloneSystem(ILogicalOperator query, TransformationParameter parameter);

}
