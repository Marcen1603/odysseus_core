package de.uniol.inf.is.odysseus.query.codegenerator.message.bus;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;

public interface ICodegeneratorMessageConsumer {
	
	public void addMessage(ProgressBarUpdate update);

}
