package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TimerTransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SetPeriodFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -3994610795368389546L;
	
	public SetPeriodFunction() {
		super("setPeriod", new SDFDatatype[][] {SDFDatatype.NUMBERS});
	}
	
	@Override public TargetedCommand<?> getValue() {
		final long period = ((Number) getInputValue(1)).longValue();
		
		return new TargetedCommand<TimerTransportHandler>(getTargets(), true) {
			@Override public boolean run(TimerTransportHandler timer) {
				timer.setPeriod(period);
				return true;
			}
		};
	}
}
