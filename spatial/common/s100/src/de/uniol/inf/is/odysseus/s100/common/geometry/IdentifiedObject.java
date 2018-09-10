package de.uniol.inf.is.odysseus.s100.common.geometry;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IdentifiedObject 
{
	public String srsName;
	
	public IdentifiedObject()
	{
		srsName = "";
	}
}
