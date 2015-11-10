package de.uniol.inf.is.odysseus.codegenerator.message.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;


/**
 * a default consumer for the messageBus system. This consumer print the 
 * eventMessages into the console
 * 
 * @author MarcPreuschaft
 *
 */
public class ConsolenMessages implements ICodegeneratorMessageConsumer{
	
	private static Logger LOG = LoggerFactory.getLogger(ConsolenMessages.class);

	@Override
	public void addMessageEvent(CodegeneratorMessageEvent update) {
		LOG.info(update.getStatusType()+": "+update.getText());
	}

}
