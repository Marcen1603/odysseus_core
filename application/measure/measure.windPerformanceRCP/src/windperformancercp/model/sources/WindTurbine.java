package windperformancercp.model.sources;

import java.util.ArrayList;

public class WindTurbine extends AbstractSource {

	double hubHeight;
	int powerControl;
	
	public WindTurbine(String name, String identifier, String hostName, int portId, ArrayList<Attribute> attList, double hubHeight, int powerControl){
		super(WTId, name, identifier, hostName, portId, attList,0);
		this.powerControl = powerControl;
		this.hubHeight = hubHeight;
			//System.out.println("WT Konstruktor: created new wind turbine: '"+this.toString());		
	}
	
	public WindTurbine(){
		super();
		this.powerControl = -1;
		this.hubHeight = 0.0;
	}
	
	public WindTurbine(WindTurbine copy){
		this(copy.getName(),
			copy.getStreamIdentifier(),
			copy.getHost(),
			copy.getPort(),
			copy.getAttributeList(),
			copy.getHubHeight(),
			copy.getPowerControl());
	}
	
	public void setHubHeight(double hh){
		this.hubHeight = hh;
	}
	
	public double getHubHeight(){
		return this.hubHeight;
	}
	
	public void setPowerControl(int pc){
		this.powerControl = pc;
	}
	
	public int getPowerControl(){
		return this.powerControl;
	}
}
