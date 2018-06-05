package de.uniol.inf.is.odysseus.s100.common.datatype;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.s100.common.geometry.DirectPosition;
import de.uniol.inf.is.odysseus.s100.common.geometry.SC_CRS;

class DirectPositionXmlAdapter extends XmlAdapter<String,DirectPosition> 
{
    public DirectPosition unmarshal(String val) throws Exception 
    {
    	DirectPosition result = new DirectPosition();
    	String[] list = val.split(" ");
    	
    	if (list.length < 2)
    		throw new IllegalArgumentException("Too few values");
    			
    	if (list.length > 3)
    		throw new IllegalArgumentException("Too many values");
    	
    	result.useAltitude = list.length == 3;
    	result.longitude = Double.parseDouble(list[0]);
    	result.latitude  = Double.parseDouble(list[1]);
    	
    	if (result.useAltitude)
    		result.altitude = Double.parseDouble(list[2]);
    	
        return result;
    }
    
    public String marshal(DirectPosition val) throws Exception 
    {
        return 	Double.toString(val.longitude) + " " +
        		Double.toString(val.latitude) + 
        		(val.useAltitude ? " " + Double.toString(val.altitude) : "");
    }
}

@XmlRootElement
public class GM_Point 
{
	@XmlJavaTypeAdapter(DirectPositionXmlAdapter.class)
	public DirectPosition position;
	
	public SC_CRS coordinateReferenceSystem;
	
	public GM_Point()
	{
		coordinateReferenceSystem = new SC_CRS();
		position = new DirectPosition();
	}
	
	public GM_Point(double longitude, double latitude)
	{
		coordinateReferenceSystem = new SC_CRS();
		position = new DirectPosition(longitude, latitude);
	}
	
	public GM_Point(double longitude, double latitude, double altitude)
	{
		coordinateReferenceSystem = new SC_CRS();
		position = new DirectPosition(longitude, latitude, altitude);
	}
}