package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

import java.awt.List;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.server.monitoring.system.SystemUsage;

public class OperatorCPUUsage implements IMeasurableValue {

	private int id;
	private static final int sleep = 500;

	public OperatorCPUUsage(IPhysicalOperator o) {
		// TODO Auto-generated constructor stub
		id = o.hashCode();
	}
	public void start(long timestamp) {
		// start Thread ,measure CPU-Usage , increase counter
		new Thread(() -> {
			SystemUsage su = SystemUsage.getInstance();
			ArrayList<Double> usages = new ArrayList<Double>();
			try {
				while (true) {
					usages.add(su.getCurrentCPUUsage());
					Thread.sleep(sleep);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}).start();
		System.out.println();
	}

	public void stop(long timestamp) {
		// Berchne

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Override
	public void startMeasurement(long timestamp) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stopMeasurement(long timestamp) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isConfirmed() {
		// TODO Auto-generated method stub
		return false;
	}
}
