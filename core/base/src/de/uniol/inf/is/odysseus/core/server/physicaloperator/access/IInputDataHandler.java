package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;


public interface IInputDataHandler<R,W> {

	void init();
	void done();
	void process(R input, IObjectHandler<W> objectHandler, IAccessConnectionHandler<R> accessHandler, ITransferHandler transferHandler);
	IInputDataHandler<R,W> clone();
}
