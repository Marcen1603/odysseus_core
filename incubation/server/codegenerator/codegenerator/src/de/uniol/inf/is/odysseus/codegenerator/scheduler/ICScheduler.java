package de.uniol.inf.is.odysseus.codegenerator.scheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface ICScheduler {
	
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> sourceOPs);
	
	public String getName();
	
	public String getTargetPlatform();
	
	public String getExecutorCode();
	
	public void setPackageName(String packageName);
}
