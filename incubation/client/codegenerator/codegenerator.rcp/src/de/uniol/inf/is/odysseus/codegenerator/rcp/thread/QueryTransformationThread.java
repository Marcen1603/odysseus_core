package de.uniol.inf.is.odysseus.codegenerator.rcp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.message.bus.CodegeneratorMessageBus;
import de.uniol.inf.is.odysseus.codegenerator.message.bus.ICodegeneratorMessageConsumer;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.rcp.CAnalyseServiceBinding;
import de.uniol.inf.is.odysseus.codegenerator.rcp.window.QueryTransformationWindow;


/**
 * this thread start the transformation of a query from the gui
 * this thread is registered as a CodegenertorMessageConsumer to
 * receive all message events 
 * 
 * @author MarcPreuschaft
 *
 */
public class QueryTransformationThread extends Thread implements ICodegeneratorMessageConsumer{
	
	private static Logger LOG = LoggerFactory.getLogger(QueryTransformationThread.class);
	private final QueryTransformationWindow window;
	private TransformationParameter parameter; 

	public QueryTransformationThread(TransformationParameter parameter, QueryTransformationWindow window){
		this.window = window;
		this.parameter = parameter;
		CodegeneratorMessageBus.registerConsumer(this);
	}
	
	/**
	 * start the query transformation process
	 */
	@Override
	public void run() {
		LOG.debug("Query transformation thread started...");
		CAnalyseServiceBinding.getAnalyseComponent().startQueryTransformation(parameter);
		CodegeneratorMessageBus.unregisterConsumer(this);	
		window.showFinishButton();	
	}
	

	/**
	 * put the message to the gui
	 */
	@Override
	public void addMessageEvent(CodegeneratorMessageEvent update) {
		window.updateProgressbar(update);
	}


}
