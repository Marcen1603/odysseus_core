package de.uniol.inf.is.odysseus.wrapper.baslercamera.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator.BaslerCameraTransportHandler;

public class TriggerFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = 579309336754019101L;

	public TriggerFunction() {
		super("trigger", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<BaslerCameraTransportHandler>(getTargets(), true) {
			@Override public boolean run(BaslerCameraTransportHandler camera) {
				return camera.trigger();
			}
		};
	}
}
