package de.uniol.inf.is.odysseus.wrapper.navicoradar.functions;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;
import de.uniol.inf.is.odysseus.wrapper.navicoradar.physicaloperator.NavicoRadarTransportHandler;

public class SetOwnShipDataFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -3994610795368389546L;
	
	public SetOwnShipDataFunction() {
		super("setOwnShipDataFunction", new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS});
	}
	
	@Override public TargetedCommand<?> getValue() 
	{		
		final int speedType = ((Number) getInputValue(1)).intValue();
		final double speed = ((Number) getInputValue(2)).doubleValue();
		final int headingType = ((Number) getInputValue(3)).intValue();
		final double heading = ((Number) getInputValue(4)).doubleValue();		
		
		return new TargetedCommand<NavicoRadarTransportHandler>(getTargets(), true) {
			@Override public boolean run(NavicoRadarTransportHandler radar) {
				return radar.setOwnShipData(speedType, speed, headingType, heading);					
			}
		};
	}	
}