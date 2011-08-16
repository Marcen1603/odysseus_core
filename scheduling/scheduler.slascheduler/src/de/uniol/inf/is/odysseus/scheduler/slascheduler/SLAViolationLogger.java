package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Simple implementation of {@link ISLAViolationEventListener} interface for
 * logging violations of sla in evaluation.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAViolationLogger implements ISLAViolationEventListener {
	private static final String SEPERATOR = ";";
	/**
	 * name of log file
	 */
	public static final String FILE_NAME = "sla_violations_"
			+ DateFormat.getDateTimeInstance().format(new Date()) + ".csv";
	/**
	 * path of log file
	 */
	public static final String PATH = System.getProperty("user.home")
			+ File.separator + "odysseus" + File.separator;
	/**
	 * filewriter used for logging
	 */
	private FileWriter writer;

	/**
	 * creates a new event listener instance and initializes logger.
	 */
	public SLAViolationLogger() {
		File file = new File(PATH, FILE_NAME);
		try {
			this.writer = new FileWriter(file);
			this.writer.write(this.header());
			this.writer.flush();
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
			this.writer.flush();
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
		sb.append(DateFormat.getDateTimeInstance().format(new Date()))
				.append(SEPERATOR)
				.append(event.getQuery().getSLA().getName())
				.append(SEPERATOR)
				.append(event.getQuery().getID())
				.append(SEPERATOR)
				.append(String.format(Locale.GERMAN, "%15.8f",
						event.getConformance())).append(SEPERATOR)
				.append(event.getServiceLevel()).append(SEPERATOR)
				.append(String.format(Locale.GERMAN, "%7.2f", event.getCost()))
				.append("\n");
		return sb.toString();
	}

	/**
	 * @return csv header
	 */
	private String header() {
		StringBuilder sb = new StringBuilder();
		sb.append("Timestamp").append(SEPERATOR).append("SLA Name")
				.append(SEPERATOR).append("Query ID").append(SEPERATOR)
				.append("Conformance").append(SEPERATOR)
				.append("Service Level").append(SEPERATOR)
				.append("Violation Cost\n");
		return sb.toString();
	}

}
