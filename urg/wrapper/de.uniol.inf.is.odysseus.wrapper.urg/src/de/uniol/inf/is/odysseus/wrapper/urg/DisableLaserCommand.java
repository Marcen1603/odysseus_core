package de.uniol.inf.is.odysseus.wrapper.urg;

import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.DISABLE_COMMAND;
import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.LF;
import de.uniol.inf.is.odysseus.wrapper.urg.utils.StringUtils;

public class DisableLaserCommand implements Command {
	private String command;
	
	public DisableLaserCommand() {
		command = DISABLE_COMMAND + LF;
	}

	public DisableLaserCommand(String message) {
		if (message == null || message.equals("")) {
			command = DISABLE_COMMAND + LF;
		} else {
			StringUtils.validateString(message);
			command = DISABLE_COMMAND + ";" + message + LF;
		}
	}

	@Override
	public String getCommand() {
		return command;
	}
}
