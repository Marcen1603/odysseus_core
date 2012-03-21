package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import de.uniol.inf.is.odysseus.core.server.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.server.objecthandler.IObjectHandler;


public interface IInputDataHandler<R,W> {

	void init();
	void done();
	void process(R input, IObjectHandler<W> objectHandler, IAccessConnectionHandler<R> accessHandler, ITransferHandler<W> transferHandler) throws ClassNotFoundException;
	IInputDataHandler<R,W> clone();
}
