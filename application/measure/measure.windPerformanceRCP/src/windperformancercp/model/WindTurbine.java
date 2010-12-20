package windperformancercp.model;

public class WindTurbine extends AbstractSource {

	double hubHeight;
	boolean activePowerControl;
	
	public WindTurbine(String identifier, int portId, boolean actPowerControl){
		super(WTId,identifier,portId);
		this.activePowerControl = actPowerControl;
	}
}
