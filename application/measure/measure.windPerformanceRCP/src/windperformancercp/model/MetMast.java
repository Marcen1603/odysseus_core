package windperformancercp.model;

public class MetMast extends AbstractSource {
	
	public MetMast(String name, String identifier, String hostName, int portId, Attribute[] attList){
		super(MMId, name, identifier, hostName, portId, attList, 0);
		System.out.println("MM Konstruktor: created new met mast: '"+this.getName()+"' '"+this.getHost()+"' '"+this.getPort());		
	}
}
