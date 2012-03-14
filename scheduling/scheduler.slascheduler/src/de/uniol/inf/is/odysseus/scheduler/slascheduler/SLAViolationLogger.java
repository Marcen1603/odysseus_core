package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import de.uniol.inf.is.odysseus.core.server.OdysseusDefaults;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;

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
	 * path of log file
	 */
	public static final String PATH = OdysseusDefaults.getHomeDir();
	/**
	 * filewriter used for logging
	 */
	private FileWriter writer;

	/**
	 * creates a new event listener instance and initializes logger.
	 */
	public SLAViolationLogger() {
		String FILE_NAME = "sla_violations_"
			+ DateFormat.getDateTimeInstance().format(new Date()).replace(":", "_") + ".csv";
		File file = new File(PATH, FILE_NAME);
		try {
			this.writer = new FileWriter(file);
			this.writer.write(SLAViolationLogger.header());
			this.writer.flush();
		} catch (IOException e) {
			//throw new RuntimeException(e);
			e.printStackTrace();
		}
	}

	/**
	 * writes the given event to log file
	 */
	@Override
	public void slaViolated(SLAViolationEvent event) {
		try {
			this.writer.write(SLAViolationLogger.eventToString(event));
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
	private static String eventToString(SLAViolationEvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormat.getDateTimeInstance().format(new Date()))
				.append(SEPERATOR)
				.append(((SLA)event.getQuery().getParameter(SLA.class.getName())).getName())
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
	private static String header() {
		StringBuilder sb = new StringBuilder();
		sb.append("Timestamp").append(SEPERATOR).append("SLA Name")
				.append(SEPERATOR).append("Query ID").append(SEPERATOR)
				.append("Conformance").append(SEPERATOR)
				.append("Service Level").append(SEPERATOR)
				.append("Violation Cost\n");
		return sb.toString();
	}

}
