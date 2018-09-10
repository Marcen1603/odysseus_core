package de.uniol.inf.is.odysseus.sensormanagement.server.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;

public class StopDefaultLoggingFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -2770515253505078978L;

	public StopDefaultLoggingFunction() {
		super("stopLogging", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<String>(getTargets(), false) {
			@Override public boolean run(String sensorId) {
				SensorFactory.getInstance().startLogging(getSession(), sensorId, SensorModel.DEFAULT_LOGGING_STYLE);
				return true;
			}
		};
	}
}
