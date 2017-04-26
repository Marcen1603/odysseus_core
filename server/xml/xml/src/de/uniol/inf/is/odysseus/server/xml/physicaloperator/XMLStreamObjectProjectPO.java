package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

public class XMLStreamObjectProjectPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>,  XMLStreamObject<T>>
{
	private List<SDFAttribute> paths;

	public XMLStreamObjectProjectPO(List<SDFAttribute> paths)
	{
		super();
		this.paths = paths;
	}

	public XMLStreamObjectProjectPO(XMLStreamObjectProjectPO<T> po)
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
		DocumentBuilder docBuilder;
		try
		{
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Node root = object.getDocument().getFirstChild();
			root = doc.importNode(root, false);
			doc.appendChild(root);
			for (SDFAttribute path : this.paths)
			{
				String pathURI = path.getURI();
				NodeList nl = object.xpathToNodeList(pathURI);
				for(int i = 0; i < nl.getLength(); i++)
				{			
					doc.appendChild(doc.importNode(nl.item(i), false));
				}					
			}
			XMLStreamObject<IMetaAttribute> newObject = XMLStreamObject.createInstance(doc); 
			newObject.setMetadata(object.getMetadata().clone());
			if(!newObject.isEmpty()) transfer((XMLStreamObject<T>) newObject);
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
}
