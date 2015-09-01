package de.uniol.inf.is.odysseus.query.codegenerator.executor;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;

public interface ICExecutor {
	
	public String getName();
	
	public String getTargetPlatform();
	
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> sourceOPs);
	
	public String getExecutorCode();
}
