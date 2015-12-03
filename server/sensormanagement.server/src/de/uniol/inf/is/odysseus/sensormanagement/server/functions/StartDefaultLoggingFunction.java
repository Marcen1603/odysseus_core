package de.uniol.inf.is.odysseus.sensormanagement.server.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.sensormanagement.server.Sensor;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;

public class StartDefaultLoggingFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -2939045513333327299L;
	
	public StartDefaultLoggingFunction() {
		super("startLogging", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<String>(getTargets(), false) {
			@Override public boolean run(String sensorId) {
				SensorFactory.getInstance().startLogging(getSession(), sensorId, Sensor.DEFAULT_LOGGING_STYLE);
				return true;
			}
		};
	}
}