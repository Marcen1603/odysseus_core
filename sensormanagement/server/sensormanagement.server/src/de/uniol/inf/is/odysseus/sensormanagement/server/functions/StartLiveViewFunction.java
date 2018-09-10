package de.uniol.inf.is.odysseus.sensormanagement.server.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;

public class StartLiveViewFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -2939045513333327299L;
	
	public StartLiveViewFunction() {
		super("startLiveView", new SDFDatatype[][] {{SDFDatatype.STRING}, {SDFDatatype.UNSIGNEDINT16}});
	}
	
	@Override public TargetedCommand<?> getValue() {
		final String ipAddress = (String) getInputValue(1);
		final int port = ((Number) getInputValue(1)).intValue();
		
		return new TargetedCommand<String>(getTargets(), false) {
			@Override public boolean run(String sensorId) {
				SensorFactory.getInstance().startLiveView(getSession(), sensorId, ipAddress, port);
				return true;
			}
		};
	}
}
