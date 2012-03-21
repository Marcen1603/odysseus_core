package de.uniol.inf.is.odysseus.core.datahandler;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;


public interface IInputDataHandler<R,W> {

	void init();
	void done();
	void process(R input, IObjectHandler<W> objectHandler, IAccessConnectionHandler<R> accessHandler, ITransferHandler<W> transferHandler) throws ClassNotFoundException;
	IInputDataHandler<R,W> clone();
}
