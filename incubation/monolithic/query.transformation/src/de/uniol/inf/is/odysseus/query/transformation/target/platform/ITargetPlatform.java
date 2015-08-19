package de.uniol.inf.is.odysseus.query.transformation.target.platform;

import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;

public interface ITargetPlatform {
	public String getTargetPlatformName();
	public void convertQueryToStandaloneSystem(ILogicalOperator query,QueryAnalyseInformation transformationInforamtion, TransformationParameter parameter,BlockingQueue<ProgressBarUpdate> queue,TransformationConfiguration transformationConfiguration) throws InterruptedException;

}
