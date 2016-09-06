package de.uniol.inf.is.odysseus.s100.common.geometry;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SC_CRS extends IdentifiedObject
{	
	public String scope;
	
	public SC_CRS()
	{
		scope = "";
	}
}
