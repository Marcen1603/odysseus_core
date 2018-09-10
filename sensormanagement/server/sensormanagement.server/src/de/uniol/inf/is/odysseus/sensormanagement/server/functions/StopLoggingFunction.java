package de.uniol.inf.is.odysseus.sensormanagement.server.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;

public class StopLoggingFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -2770515253505078978L;

	public StopLoggingFunction() {
		super("stopLogging", new SDFDatatype[][] {{SDFDatatype.STRING}});
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
