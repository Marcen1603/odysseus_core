package de.uniol.inf.is.odysseus.codegenerator.message.bus;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;

public interface ICodegeneratorMessageConsumer {
	
	public void addMessageEvent(CodegeneratorMessageEvent update);

}
