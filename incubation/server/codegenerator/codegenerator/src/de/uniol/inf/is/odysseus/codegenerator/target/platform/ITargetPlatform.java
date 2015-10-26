package de.uniol.inf.is.odysseus.codegenerator.target.platform;

import de.uniol.inf.is.odysseus.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.modell.enums.UpdateMessageEventType;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public interface ITargetPlatform {
	
	public String getTargetPlatformName();
	public void setTargetPlatformName(String targetPlatformName);
	public void convertQueryToStandaloneSystem(ILogicalOperator query,QueryAnalyseInformation transformationInforamtion, TransformationParameter parameter,TransformationConfiguration transformationConfiguration) throws InterruptedException;
	public void sendMessageEvent(int value, String text, UpdateMessageEventType statusType);

	
	public String getSpecialOptionInfos();
	

}
