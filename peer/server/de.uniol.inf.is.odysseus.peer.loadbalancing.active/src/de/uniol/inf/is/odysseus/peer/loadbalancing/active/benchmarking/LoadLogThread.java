package de.uniol.inf.is.odysseus.peer.loadbalancing.active.benchmarking;

import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public void setInactive() {
		isActive = false;
	}
	
	public LoadLogThread(IPeerResourceUsageManager usageManager, String filename) {
		this.usageManager = usageManager;
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

}
