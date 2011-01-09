package windperformancercp.model;

public class WindTurbine extends AbstractSource {

	double hubHeight;
	boolean activePowerControl;
	
	public WindTurbine(String name, String identifier, String hostName, int portId, Attribute[] attList, double hubHeight, boolean actPowerControl){
		super(WTId, name, identifier, hostName, portId, attList,0);
		this.activePowerControl = actPowerControl;
		this.hubHeight = hubHeight;
System.out.println("WT Konstruktor: created new wind turbine: '"+this.getName()+"', '"+this.getHost()+"', '"+this.getPort());		
	}
}
