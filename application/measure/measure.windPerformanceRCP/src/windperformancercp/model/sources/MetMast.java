package windperformancercp.model.sources;

import java.util.ArrayList;


public class MetMast extends AbstractSource {
	
	int temperatureInKelvin;
	
	public MetMast(String name, String identifier, String hostName, int portId, ArrayList<Attribute> attList, int tempInK){
		super(MMId, name, identifier, hostName, portId, attList, 0);
		this.temperatureInKelvin = tempInK;
		//System.out.println("MM Konstruktor: created new met mast: '"+this.toString());		
	}
	
	public MetMast(){
		super();
		this.temperatureInKelvin = -1;
	}
	
	public MetMast(MetMast copy){
		this(copy.getName(),
			copy.getStreamIdentifier(),
			copy.getHost(),
			copy.getPort(),
			copy.getAttributeList(),
			copy.getTemperatureInKelvin());
	}
	
	public int getTemperatureInKelvin(){
		return this.temperatureInKelvin;
	}
	
	public void setTemperatureInKelvin(int tIK){
		this.temperatureInKelvin = tIK;
	}
	
	
}
