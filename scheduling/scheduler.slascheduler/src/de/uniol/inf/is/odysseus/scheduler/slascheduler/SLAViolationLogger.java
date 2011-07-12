package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SLAViolationLogger implements ISLAViolationEventListener {

	private FileWriter writer;

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

	@Override
	public void slaViolated(SLAViolationEvent event) {
		try {
			this.writer.write(this.eventToString(event));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String eventToString(SLAViolationEvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append(event.getQuery().getSLA().getName())
				.append(this.getSeperator()).append(event.getQuery().getID())
				.append(this.getSeperator()).append(event.getCost())
				.append("\n");
		return sb.toString();
	}

	private String header() {
		return "SLA Name, Query ID, Violation Cost\n";
	}

	private String getSeperator() {
		return ", ";
	}

}
