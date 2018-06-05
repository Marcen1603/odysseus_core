package de.uniol.inf.is.odysseus.sensormanagement.server.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;

public class StopLiveViewFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -2770515253505078978L;

	public StopLiveViewFunction() {
		super("stopLiveView", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<String>(getTargets(), false) {
			@Override public boolean run(String sensorId) {
				SensorFactory.getInstance().stopLiveView(getSession(), sensorId);
				return true;
			}
		};
	}
}
