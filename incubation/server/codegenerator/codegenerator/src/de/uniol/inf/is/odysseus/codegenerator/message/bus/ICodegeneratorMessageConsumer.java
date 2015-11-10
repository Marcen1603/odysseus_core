package de.uniol.inf.is.odysseus.codegenerator.message.bus;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;

/**
 * interface to the CodegeneratorMessageConsumer
 * 
 * @author MarcPreuschaft
 *
 */
public interface ICodegeneratorMessageConsumer {
	
	/**
	 * receives the messageEvent form the codegeneratorBus
	 * @param update
	 */
	public void addMessageEvent(CodegeneratorMessageEvent update);

}
