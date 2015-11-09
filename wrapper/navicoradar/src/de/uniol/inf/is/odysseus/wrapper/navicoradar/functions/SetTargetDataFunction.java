package de.uniol.inf.is.odysseus.wrapper.navicoradar.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.wrapper.navicoradar.physicaloperator.NavicoRadarTransportHandler;

public class SetTargetDataFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -3994610795368389546L;
	
	public SetTargetDataFunction() {
		super("setTargetDataFunction", new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS});
	}
	
	@Override public TargetedCommand<?> getValue() 
	{		
		final int targetId = ((Number) getInputValue(1)).intValue();
		final int range = ((Number) getInputValue(2)).intValue();
		final int bearing = ((Number) getInputValue(3)).intValue();
		final int bearingType = ((Number) getInputValue(4)).intValue();		
		
		return new TargetedCommand<NavicoRadarTransportHandler>(getTargets(), true) {
			@Override public boolean run(NavicoRadarTransportHandler radar) {
				radar.setTargetData(targetId, range, bearing, bearingType);					
				return true;
			}
		};
	}	
}