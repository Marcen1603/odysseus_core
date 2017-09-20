package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.server.monitoring.system.SystemUsage;

public class ThreadCalculateCPUUsage extends Thread {

	private volatile boolean running = true;
	private HashMap<IPhysicalOperator, Long> latencys;
	private static final int sampleTime = 1000;
	private int totalLatency;
	
	public ThreadCalculateCPUUsage(HashMap<IPhysicalOperator, Long> l) {
		this.setName("Calculating_CPU_Usage");
		this.setLatencys(l);
	}

	public void shutdown() {
		this.running = false;
	}

	/**
	 * Looks at queue for new events
	 */
	@Override
	public void run() {
		while (running) {
			//TODO
			synchronized(latencys){
				for (IPhysicalOperator o : latencys.keySet()){
					//TODO: o.getThreadID
					int threadID =1;
					SystemUsage.getInstance().getCPUUsageforThread(threadID);
				}
			}
			try {
				wait(sampleTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized HashMap<IPhysicalOperator, Long> getLatencys() {
		return latencys;
	}

	public synchronized void setLatencys(HashMap<IPhysicalOperator, Long> latency) {
		this.latencys = latency;
		calcSum();
	}

	private void calcSum() {
		// TODO Auto-generated method stub
		
	}
}
