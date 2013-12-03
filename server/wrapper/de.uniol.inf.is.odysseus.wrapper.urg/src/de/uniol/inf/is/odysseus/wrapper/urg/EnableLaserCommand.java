package de.uniol.inf.is.odysseus.wrapper.urg;

import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.LF;
import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.ENABLE_COMMAND;
import de.uniol.inf.is.odysseus.wrapper.urg.utils.StringUtils;

public class EnableLaserCommand implements Command {
	private String command;
	
	public EnableLaserCommand() {
		command = ENABLE_COMMAND + LF;
	}

	public EnableLaserCommand(String message) {
		if (message == null || message.equals("")) {
			command = ENABLE_COMMAND + LF;
		} else {
			StringUtils.validateString(message);
			command = ENABLE_COMMAND + ";" + message + LF;
		}
	}

	@Override
	public String getCommand() {
		return command;
	}
}
