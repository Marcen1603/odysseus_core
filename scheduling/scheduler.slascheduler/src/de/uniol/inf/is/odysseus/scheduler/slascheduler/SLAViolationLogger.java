package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple implementation of {@link ISLAViolationEventListener} interface for
 * logging violations of sla in evaluation.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAViolationLogger implements ISLAViolationEventListener {

	/**
	 * filewriter used for logging
	 */
	private FileWriter writer;

	/**
	 * creates a new event listener instance and initializes logger.
	 */
	public SLAViolationLogger() {
		String fileName = "sla_violations_" + System.currentTimeMillis()
				+ ".csv";
		File file = new File(fileName);
		try {
			this.writer = new FileWriter(file);
			this.writer.write(this.header());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * writes the given event to log file
	 */
	@Override
	public void slaViolated(SLAViolationEvent event) {
		try {
			this.writer.write(this.eventToString(event));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * returns an csv-compliant string representation of the given event
	 * 
	 * @param event
	 *            the event that should be transformed to String
	 * @return
	 */
	private String eventToString(SLAViolationEvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append(event.getQuery().getSLA().getName())
				.append(this.getSeperator()).append(event.getQuery().getID())
				.append(this.getSeperator()).append(event.getCost())
				.append("\n");
		return sb.toString();
	}

	/**
	 * @return csv header
	 */
	private String header() {
		return "SLA Name" + this.getSeperator() + "Query ID"
				+ this.getSeperator() + "Violation Cost\n";
	}

	/**
	 * @return csv seperator
	 */
	private String getSeperator() {
		return ", ";
	}

}
