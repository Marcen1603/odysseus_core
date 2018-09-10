package de.uniol.inf.is.odysseus.wrapper.urg;

import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.LF;
import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.START_COMMAND;
import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.DEFAULT_END_STEP;
import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.DEFAULT_START_STEP;
import de.uniol.inf.is.odysseus.wrapper.urg.utils.StringUtils;

public class StartMeasurementCommand implements Command {
	private String command;
	
	public StartMeasurementCommand() {
		createCommand(DEFAULT_START_STEP, DEFAULT_END_STEP, 0, 0, 0, "");
	}
	
	public StartMeasurementCommand(String message) {
		if (message == null || message.equals("")) {
			createCommand(DEFAULT_START_STEP, DEFAULT_END_STEP, 0, 0, 0, "");
		} else {
			StringUtils.validateString(message);
			createCommand(DEFAULT_START_STEP, DEFAULT_END_STEP, 0, 0, 0, ";" + message);
		}
	}
	
	public StartMeasurementCommand(int startStep, int endStep, int clusterCount, int scanInterval, int numberOfScans) {
		createCommand(startStep, endStep, clusterCount, scanInterval, numberOfScans, "");
	}

	public StartMeasurementCommand(int startStep, int endStep, int clusterCount, int scanInterval, int numberOfScans, String message) {
		if (message == null || message.equals("")) {
			createCommand(startStep, endStep, clusterCount, scanInterval, numberOfScans, "");
		} else {
			StringUtils.validateString(message);
			createCommand(startStep, endStep, clusterCount, scanInterval, numberOfScans, ";" + message);
		}
	}
	
	private void createCommand(int startStep, int endStep, int clusterCount, int scanInterval, int numberOfScans, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(START_COMMAND);
		sb.append(String.format("%04d", startStep));
		sb.append(String.format("%04d", endStep));
		sb.append(String.format("%02d", clusterCount));
		sb.append(String.format("%01d", scanInterval));
		sb.append(String.format("%02d", numberOfScans));
		sb.append(message);
		sb.append(LF);
		command = sb.toString();
	}

	@Override
	public String getCommand() {
		return command;
	}
}
