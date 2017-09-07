package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.server.monitoring.system.SystemUsage;

public class Monitor implements Runnable {
	private ArrayList<Double> cpuUsages;
	private ArrayList<Integer> ramUsages;
	private volatile boolean running = true;

	public Monitor() {
		this.setCpuUsages(new ArrayList<Double>());
		this.setRamUsages(new ArrayList<Integer>());
	}

	private void setRamUsages(ArrayList<Integer> arrayList) {
		this.ramUsages = arrayList;
	}

	private void setCpuUsages(ArrayList<Double> arrayList) {
		this.cpuUsages = arrayList;
	}

	public double getCpuUsage() {
		double cpuUsage = 5;
		for (double usage : this.cpuUsages) {
			cpuUsage += usage;
		}

		return cpuUsage / this.cpuUsages.size();
	}

	public ArrayList<Integer> getRamUsages() {
		return ramUsages;
	}

	public void shutdown(){
		running = false;
	}
	@Override
	public void run() {
		SystemUsage su = SystemUsage.getInstance();
		while (running) {
			this.cpuUsages.add(su.getCurrentCPUUsage());
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}
