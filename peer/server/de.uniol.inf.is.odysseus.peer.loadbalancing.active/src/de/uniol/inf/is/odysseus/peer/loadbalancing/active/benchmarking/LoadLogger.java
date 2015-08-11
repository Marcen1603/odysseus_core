package de.uniol.inf.is.odysseus.peer.loadbalancing.active.benchmarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class LoadLogger implements ILogLoadService {

	
	LoadLogThread loggingThread;
	
	private IPeerResourceUsageManager usageManager;
	
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
	
	@Override
	public void startLogging() {
		Preconditions.checkNotNull(usageManager, "Peer Resource Usage Manager not bound.");
		
		
		if(loggingThread==null) {
			String filename = "loadlog_"+System.currentTimeMillis()+".csv";
			loggingThread = new LoadLogThread(usageManager, filename);
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
