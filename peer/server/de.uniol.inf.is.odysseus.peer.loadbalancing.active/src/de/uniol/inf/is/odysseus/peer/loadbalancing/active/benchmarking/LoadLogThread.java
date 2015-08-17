package de.uniol.inf.is.odysseus.peer.loadbalancing.active.benchmarking;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;


public class LoadLogThread extends Thread {
	

	private String filename;
	private static final char SEPERATOR = ',';
	private static final char LINEBREAK  = '\n';
	private static final int WRITE_BULK_SIZE=60; //60 Tuples -> Write about every minute.
	public static final int FETCH_LOAD_INTERVAL = 1000;
	int counter = 0;
	StringBuilder sb = new StringBuilder();
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LoadLogThread.class);
	
	private boolean isActive = true;
	private IPeerResourceUsageManager usageManager;
	private IServerExecutor executor;
	private IP2PNetworkManager networkManager;
	
	public void setInactive() {
		isActive = false;
	}
	
	public LoadLogThread(IPeerResourceUsageManager usageManager, IServerExecutor executor, IP2PNetworkManager networkManager,String filename) {
		this.usageManager = usageManager;
		this.executor = executor;
		this.networkManager = networkManager;
		this.filename = filename;
	}
	
	@Override
	public void run() {
		while(isActive) {
			IResourceUsage usage = usageManager.getLocalResourceUsage();
			
			long timestamp = System.currentTimeMillis();
			sb.append(timestamp);
			sb.append(SEPERATOR);
			
			double cpuUsage = 1-(usage.getCpuFree()/usage.getCpuMax());
			sb.append(cpuUsage);
			sb.append(SEPERATOR);
			
			double memUsage = 1-(1.0*usage.getMemFreeBytes()/usage.getMemMaxBytes());
			sb.append(memUsage);
			sb.append(SEPERATOR);
			
			double netUsage = (usage.getNetOutputRate()+usage.getNetInputRate())/usage.getNetBandwidthMax();
			sb.append(netUsage);
			sb.append(SEPERATOR);
			
			Pair<Integer,Integer> senderReceiverCount = getExternalJxtaOperatorCount();
			sb.append(senderReceiverCount.getE1());
			sb.append(SEPERATOR);
			
			sb.append(senderReceiverCount.getE2());
			sb.append(LINEBREAK);
			
			counter++;
			if(counter==WRITE_BULK_SIZE) {
				flushToFile();
				sb = new StringBuilder();
				counter = 0;
			}
				
			//Wait for update interval.
			try {
				Thread.sleep(FETCH_LOAD_INTERVAL);
			}
			catch (InterruptedException e) {
				LOG.error(e.getMessage());
				isActive = false;
			}
		}
		
		flushToFile();
		
		
	}
	
	private void flushToFile() {
		try
		{
		    FileWriter fw = new FileWriter(filename,true);
		    fw.write(sb.toString());
		    fw.close();
		}
		catch(IOException ioe)
		{
		    LOG.error("IOException: " + ioe.getMessage());
		    stopThread();
		}
	}
	
	public synchronized void stopThread() {
		this.isActive = false;
	}
	
	

	@SuppressWarnings("rawtypes")
	private Pair<Integer,Integer> getExternalJxtaOperatorCount() {
		
		String localPID = networkManager.getLocalPeerID().toString();

		int senderCount=0;
		int receiverCount=0;
		
		List<IPhysicalOperator> allOperatorsOnPeer = Lists.newArrayList();
		
		for(IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			allOperatorsOnPeer.addAll(query.getAllOperators());
		}
		
		
		Set<IPhysicalOperator> allOperatorsOnPeerSet = new HashSet<IPhysicalOperator>(allOperatorsOnPeer);
	
		for(IPhysicalOperator op : allOperatorsOnPeerSet) {
			if(op instanceof JxtaSenderPO)  {
				if(((JxtaSenderPO)op).getPeerIDString().equals(localPID))
					continue;
				senderCount++;
			}
			if(op instanceof JxtaReceiverPO) {
				if(((JxtaReceiverPO)op).getPeerIDString().equals(localPID))
				receiverCount++;
			}
		}
		
		return new Pair<Integer,Integer>(senderCount,receiverCount);
	}
	

}
