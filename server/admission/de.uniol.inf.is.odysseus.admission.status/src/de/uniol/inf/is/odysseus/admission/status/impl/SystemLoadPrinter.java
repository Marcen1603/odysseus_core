package de.uniol.inf.is.odysseus.admission.status.impl;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.admission.status.ExecutorAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.status.SystemLoadAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.latency.LatencyValuesContainer;

public class SystemLoadPrinter extends Thread {

	private static final int MEASURE_INTERVAL_MILLIS = 3000;
	
	private boolean running;
	
	public SystemLoadPrinter() {
		setName("SystemLoad status printer");
		setDaemon(true);
	}
	
	@Override
	public void run() {
		ExecutorAdmissionStatusComponent executorStatus = new ExecutorAdmissionStatusComponent();
		SystemLoadAdmissionStatusComponent systemLoadStatus = new SystemLoadAdmissionStatusComponent();
		
		System.err.println("Waiting...");
		while( !isServerExecutorBound() || executorStatus.getRunningQueryCount() == 0 ) {
			trySleep(200);
		}
		System.err.println("Start cpu load measurement...");

		long startTimestamp = System.currentTimeMillis();

		running = true;
		while (running) {

			long time = (System.currentTimeMillis() - startTimestamp) / 1000;

			System.err.print(time + ",");
			System.err.print((int)systemLoadStatus.getCpuLoadPercentage() + ",");
			System.err.print(executorStatus.getRunningQueryCount() + ",");
			System.err.print(executorStatus.getPartialQueryCount() + ",");
			System.err.print(executorStatus.getSuspendedQueryCount() + ",");
			System.err.print(executorStatus.getStoppedQueryCount() + ",");
			System.err.println(LatencyValuesContainer.popAverage());

			trySleep(MEASURE_INTERVAL_MILLIS);
		}
	}

	private static boolean isServerExecutorBound() {
		return AdmissionStatusPlugIn.getServerExecutor() != null;
	}

	private static void trySleep(long timems) {
		try {
			Thread.sleep(timems);
		} catch (InterruptedException e) {
		}
	}
	
	public void stopRunning() {
		running = false;
	}
}
