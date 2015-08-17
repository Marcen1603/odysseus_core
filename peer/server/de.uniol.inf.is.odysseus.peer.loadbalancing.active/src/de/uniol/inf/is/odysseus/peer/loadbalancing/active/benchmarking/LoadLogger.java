package de.uniol.inf.is.odysseus.peer.loadbalancing.active.benchmarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class LoadLogger implements ILogLoadService {

	
	LoadLogThread loggingThread;
	
	private IPeerResourceUsageManager usageManager;
	private IServerExecutor executor;
	private IP2PNetworkManager networkManager;
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LoadLogger.class);
	
	
	public void bindResourceUsageManager(IPeerResourceUsageManager serv) {
		this.usageManager = serv;
	}
	
	public void unbindResourceUsageManager(IPeerResourceUsageManager serv) {
		if(this.usageManager==serv) {
			this.usageManager = null;
		}
	}
	
	public void bindExecutor(IExecutor serv) {
		if(serv instanceof IServerExecutor) {
			executor = (IServerExecutor)serv;
		}
	}
	
	public void unbindExecutor(IExecutor serv) {
		if(serv==executor) {
			executor = null;
		}
	}
	
	public void bindNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
	}
	
	public void unbindNetworkManager(IP2PNetworkManager serv) {
		if(networkManager==serv) {
			networkManager = null;
		}
	}
	
	
	@Override
	public void startLogging() {
		Preconditions.checkNotNull(usageManager, "Peer Resource Usage Manager not bound.");
		
		
		if(loggingThread==null) {
			String filename = "loadlog_"+System.currentTimeMillis()+".csv";
			loggingThread = new LoadLogThread(usageManager,executor,networkManager, filename);
			loggingThread.start();
			LOG.info("Started Logging Load to {}",filename);
		}
		
	}

	@Override
	public void stopLogging() {
		loggingThread.stopThread();
		loggingThread = null;

		LOG.info("Stopped Logging Load");
	}

}
