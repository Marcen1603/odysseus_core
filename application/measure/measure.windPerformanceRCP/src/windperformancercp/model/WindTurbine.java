package windperformancercp.model;

public class WindTurbine extends AbstractSource {

	double hubHeight;
	boolean activePowerControl;
	
	public WindTurbine(String name, String identifier, String hostName, int portId, Attribute[] attList, boolean actPowerControl){
		super(WTId, name, identifier, hostName, portId, attList);
		this.activePowerControl = actPowerControl;
	}
}
