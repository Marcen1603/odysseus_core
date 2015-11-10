package de.uniol.inf.is.odysseus.codegenerator.scheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
/**
 * Interface for the scheduler 
 * @author Marc
 *
 */
public interface ICScheduler {
	
	/**
	 * return the startcode for the operator list
	 * @param sourceOPs
	 * @return
	 */
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> sourceOPs);
	
	/**
	 * return the name of the scheduler
	 * @return
	 */
	public String getName();
	
	/**
	 * return the target platform 
	 * @return
	 */
	public String getTargetPlatform();
	
	/**
	 * return the code for the scheduler
	 * @return
	 */
	public String getSchedulerCode();
	
	/**
	 * set the packageName for the getSchedulerCode 
	 * @param packageName
	 */
	public void setPackageName(String packageName);
}
