package de.uniol.inf.is.odysseus.sensormanagement.server.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;

public class StartLoggingFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -2939045513333327299L;
	
	public StartLoggingFunction() {
		super("startLogging", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		final String loggingStyle = (String) getInputValue(1);
		
		return new TargetedCommand<String>(getTargets(), false) {
			@Override public boolean run(String sensorId) {
				SensorFactory.getInstance().startLogging(getSession(), sensorId, loggingStyle);
				return true;
			}
		};
	}
}