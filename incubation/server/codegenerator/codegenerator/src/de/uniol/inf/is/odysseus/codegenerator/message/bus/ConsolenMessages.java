package de.uniol.inf.is.odysseus.codegenerator.message.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;

public class ConsolenMessages implements ICodegeneratorMessageConsumer{
	
	private static Logger LOG = LoggerFactory.getLogger(ConsolenMessages.class);

	@Override
	public void addMessageEvent(CodegeneratorMessageEvent update) {
		LOG.info(update.getStatusType()+": "+update.getText());
	}

}
