package de.uniol.inf.is.odysseus.codegenerator.target.platform;

import de.uniol.inf.is.odysseus.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.modell.enums.UpdateMessageEventType;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * interface for codegenerator targetPlatforms
 * 
 * @author MarcPreuschaft
 *
 */
public interface ITargetPlatform {
	
	/**
	 * return the name of the targetPlatform
	 * 
	 * @return
	 */
	public String getTargetPlatformName();
	
	/**
	 * set the name of the targetPlatform
	 * 
	 * @param targetPlatformName
	 */
	public void setTargetPlatformName(String targetPlatformName);
	
	/**
	 * convert a query to a standalonesystem
	 * 
	 * @param query
	 * @param transformationInforamtion
	 * @param parameter
	 * @param transformationConfiguration
	 * @throws InterruptedException
	 */
	public void convertQueryToStandaloneSystem(ILogicalOperator query,QueryAnalyseInformation transformationInforamtion, TransformationParameter parameter,TransformationConfiguration transformationConfiguration) throws InterruptedException;
	
	/**
	 * sends a event message e.g to the codegenerator.messageBus system
	 * 
	 * @param value
	 * @param text
	 * @param statusType
	 */
	public void sendMessageEvent(int value, String text, UpdateMessageEventType statusType);
	
	/**
	 * return the info text for the speical options for a targetPlatform
	 * 
	 * @return
	 */
	public String getSpecialOptionInfos();
	

}
