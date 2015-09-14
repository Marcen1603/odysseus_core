package de.uniol.inf.is.odysseus.query.codegenerator;

import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;

public interface ICAnalyse {

	
	public void startQueryTransformation(TransformationParameter parameter,BlockingQueue<ProgressBarUpdate> queue );
	
}
