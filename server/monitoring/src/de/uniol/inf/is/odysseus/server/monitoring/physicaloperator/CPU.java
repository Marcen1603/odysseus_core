package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

import java.awt.List;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.server.monitoring.system.SystemUsage;

public class CPU implements IMeasurableValue {

	private int id;

	public CPU(IPhysicalOperator o) {
		// TODO Auto-generated constructor stub
		id = o.hashCode();
	}
	@Override
	public void start(long timestamp) {
		// Thread starten, CPU-Auslastung messen, counter erhöhen für die
		// Anzahl, wie oft die
		new Thread(() -> {
			SystemUsage su = SystemUsage.getInstance();
			ArrayList<Double> usages = new ArrayList<Double>();
			try {
				while (true) {
					usages.add(su.getCurrentCPUUsage());
					Thread.sleep(500);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}).start();
		System.out.println();
	}

	@Override
	public void stop(long timestamp) {
		// Berchne

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
