package de.uniol.inf.is.odysseus.query.codegenerator.scheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;

public interface ICScheduler {
	
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> sourceOPs);
	
	public String getName();
	
	public String getTargetPlatform();
	
	public String getExecutorCode();
}
