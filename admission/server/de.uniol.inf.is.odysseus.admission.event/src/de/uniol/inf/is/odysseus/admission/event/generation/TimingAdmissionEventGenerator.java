package de.uniol.inf.is.odysseus.admission.event.generation;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.admission.event.AdmissionEventPlugIn;
import de.uniol.inf.is.odysseus.admission.event.TimingAdmissionEvent;

public class TimingAdmissionEventGenerator extends Thread {

	private final long intervalMillis;
	
	private boolean running;
	
	public TimingAdmissionEventGenerator( long intervalMillis ) {
		Preconditions.checkArgument(intervalMillis > 0, "IntervalMillis must be positive instead of %s", intervalMillis);
		
		this.intervalMillis = intervalMillis;
		
		setName("TimingAdmissionEvent generator thread");
		setDaemon(true);
	}
	
	@Override
	public void run() {
		running = true;
		long startTimestamp = System.currentTimeMillis();
		while( running ) {
			trySleep();
			
			if( isAdmissionControlBound() ) {
				AdmissionEventPlugIn.getAdmissionControl().processEventAsync(new TimingAdmissionEvent(intervalMillis, System.currentTimeMillis() - startTimestamp));
			}
		}
	}

	private static boolean isAdmissionControlBound() {
		return AdmissionEventPlugIn.getAdmissionControl() != null;
	}

	private void trySleep() {
		try {
			Thread.sleep(intervalMillis);
		} catch (InterruptedException e) {
		}
	}
	
	public void stopRunning() {
		running = false;
	}
}
