package de.uniol.inf.is.odysseus.query.codegenerator.rcp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.query.codegenerator.message.bus.CodegeneratorMessageBus;
import de.uniol.inf.is.odysseus.query.codegenerator.message.bus.ICodegeneratorMessageConsumer;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.CAnalyseServiceBinding;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.window.QueryTransformationWindow;



public class QueryTransformationThread extends Thread implements ICodegeneratorMessageConsumer{
	
	private static Logger LOG = LoggerFactory.getLogger(QueryTransformationThread.class);
	private final QueryTransformationWindow window;
	private TransformationParameter parameter; 

	public QueryTransformationThread(TransformationParameter parameter, QueryTransformationWindow window){
		this.window = window;
		this.parameter = parameter;
		CodegeneratorMessageBus.registerConsumer(this);
	}
	
	@Override
	public void run() {
		LOG.debug("Query transformation thread started...");
		CAnalyseServiceBinding.getAnalyseComponent().startQueryTransformation(parameter);
		CodegeneratorMessageBus.unregisterConsumer(this);	
		window.showFinishButton();	
	}
	

	@Override
	public void addMessageEvent(CodegeneratorMessageEvent update) {
		window.updateProgressbar(update);
	}


}
