package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.List;

import javax.xml.xpath.XPathFactoryConfigurationException;

import org.w3c.dom.NodeList;

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
			NodeList nl = object.xpathToNodeList(path.getURI());		
			for(int i=0; i < nl.getLength(); i++)
			{
				if(!XMLStreamObject.hasParent(nl, nl.item(i)))
				{
					XMLStreamObject<IMetaAttribute> newObject;
					try {
						newObject = XMLStreamObject.createInstance(nl.item(i));
						newObject.setMetadata(object.getMetadata().clone());
						if(!newObject.isEmpty()) transfer((XMLStreamObject<T>) newObject);
					} catch (XPathFactoryConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
		}
	}
}
