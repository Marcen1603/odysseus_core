package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.IExecutionHandlerFactory;
import de.uniol.inf.is.odysseus.p2p.peer.execution.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

/**
 * 
 * 
 * @author Mart Köhler
 * 
 */
public abstract class AbstractPeer implements IPeer {

	private Logger logger;
	private HashMap<Query, IExecutionListener > queries;
	private HashMap<Lifecycle, IExecutionHandlerFactory> executionHandlerFactory;
	
	private ISocketServerListener socketServerListener;


	public AbstractPeer() {
		this.logger = LoggerFactory.getLogger(AbstractPeer.class);
		this.setQueries(new HashMap<Query, IExecutionListener>());
	
	}
	
	@Override
	public abstract void startPeer();

	@Override
	public abstract void stopPeer();
	
	public abstract Object getServerResponseAddress();
	
	public boolean registerMessageHandler(
			IMessageHandler messageHandler) {
		return getSocketServerListener().registerMessageHandler(messageHandler);
		
	}

	public boolean registerMessageHandler(
			List<IMessageHandler> messageHandler) {
		boolean success = true;
		for(IMessageHandler mHandler : messageHandler) {
			if(registerMessageHandler(mHandler)==false) {
				success = false;
			}
		}
		return success;
	}

	public Logger getLogger() {
		return logger;
	}

	protected ISocketServerListener getSocketServerListener() {
		return this.socketServerListener;
	}
	
	protected void setSocketServerListener(ISocketServerListener ssl) {
		this.socketServerListener = ssl;
	}

	public void setQueries(HashMap<Query, IExecutionListener > queries) {
		this.queries = queries;
	}

	public HashMap<Query, IExecutionListener > getQueries() {
		return queries;
	}
	
	public HashMap<Lifecycle, IExecutionHandlerFactory> getExecutionHandlerFactory() {
		return executionHandlerFactory;
	}
	
	public abstract void addQuery(Query query);
	public abstract void removeQuery(Query query);
}
