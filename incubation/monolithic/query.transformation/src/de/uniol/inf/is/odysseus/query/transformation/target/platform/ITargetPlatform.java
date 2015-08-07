package de.uniol.inf.is.odysseus.query.transformation.target.platform;

import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;

public interface ITargetPlatform {
	public String getTargetPlatformName();
	public void convertQueryToStandaloneSystem(ILogicalOperator query, TransformationParameter parameter,BlockingQueue<ProgressBarUpdate> queue) throws InterruptedException;

}
