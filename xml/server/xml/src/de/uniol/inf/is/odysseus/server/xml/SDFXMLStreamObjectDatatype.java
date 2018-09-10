package de.uniol.inf.is.odysseus.server.xml;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import java.util.ArrayList;
import java.util.List;

public class SDFXMLStreamObjectDatatype  extends SDFDatatype
{
	public SDFXMLStreamObjectDatatype(String URI)
	{
		super(URI);
	}

	private static final long serialVersionUID = 8837019983105934507L;

	public static final SDFDatatype XMLSTREAMOBJECT = new SDFDatatype("XMLStreamObject");

	public static final SDFDatatype LIST_XMLSTREAMOBJECT = new SDFDatatype("List_XMLStreamObject", SDFDatatype.KindOfDatatype.LIST, XMLSTREAMOBJECT);	
	
	public static List<SDFDatatype> getAll() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFXMLStreamObjectDatatype.XMLSTREAMOBJECT);
		ret.add(SDFXMLStreamObjectDatatype.LIST_XMLSTREAMOBJECT);
		return ret;
	}
}