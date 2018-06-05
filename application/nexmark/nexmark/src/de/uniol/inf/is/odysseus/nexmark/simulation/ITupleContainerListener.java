package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;

import de.uniol.inf.is.odysseus.nexmark.generator.TupleContainer;

public interface ITupleContainerListener {

	void newObject(TupleContainer container) throws IOException;


}
