package windperformancercp.model.sources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class MetMast extends AbstractSource {
	
	public MetMast(String name, String identifier, String hostName, int portId, ArrayList<Attribute> attList){
		super(MMId, name, identifier, hostName, portId, attList, 0);

		//System.out.println("MM Konstruktor: created new met mast: '"+this.toString());		
	}
	
	public MetMast(){
		super();
	}
	
	public MetMast(MetMast copy){
		this(copy.getName(),
			copy.getStreamIdentifier(),
			copy.getHost(),
			copy.getPort(),
			copy.getAttributeList());
	}
	
	@Override
	public boolean isWindTurbine() {
		return false;
	}

	@Override
	public boolean isMetMast() {
		return true;
	}
	
}
