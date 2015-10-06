package de.uniol.inf.is.odysseus.query.codegenerator.target.platform;

import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;

public interface ITargetPlatform {
	
	public String getTargetPlatformName();
	public void setTargetPlatformName(String targetPlatformName);
	public void convertQueryToStandaloneSystem(ILogicalOperator query,QueryAnalyseInformation transformationInforamtion, TransformationParameter parameter,BlockingQueue<ProgressBarUpdate> queue,TransformationConfiguration transformationConfiguration) throws InterruptedException;
	public void updateProgressBar(int value, String text, UpdateMessageStatusType statusType);
	public void setProgressBarQueue(BlockingQueue<ProgressBarUpdate> progressBarQueue);
	public BlockingQueue<ProgressBarUpdate> getProgressBarQueue();
	
	public String getSpecialOptionInfos();
	

}
