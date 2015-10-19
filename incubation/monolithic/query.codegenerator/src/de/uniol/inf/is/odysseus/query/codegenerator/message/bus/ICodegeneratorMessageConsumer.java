package de.uniol.inf.is.odysseus.query.codegenerator.message.bus;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodegeneratorMessageEvent;

public interface ICodegeneratorMessageConsumer {
	
	public void addMessage(CodegeneratorMessageEvent update);

}
