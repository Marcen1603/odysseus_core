package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

public class XPathPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>,  XMLStreamObject<T>>
{
	private List<SDFAttribute> paths;

	public XPathPO(List<SDFAttribute> paths)
	{
		super();
		this.paths = paths;
	}

	public XPathPO(XPathPO<T> po)
	{
		super(po);
		this.paths = po.paths;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port)
	{
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode()
	{
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")	
	@Override
	protected void process_next(XMLStreamObject<T> object, int port)
	{
		for (SDFAttribute path : this.paths)
		{
			String pathURI = path.getURI();
			XMLStreamObject<IMetaAttribute> newObject = XMLStreamObject.createInstance(object.xpathToDocument(pathURI)); 
			newObject.setMetadata(object.getMetadata().clone());
			if(!newObject.isEmpty()) transfer((XMLStreamObject<T>) newObject);
		}
	}
}
