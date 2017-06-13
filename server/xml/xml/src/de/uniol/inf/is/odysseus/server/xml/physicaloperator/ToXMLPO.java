package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.ganesh.transformer.DynamicXMLTransformer;

import de.uniol.inf.is.odysseus.core.collection.XMLStreanObject;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ToXMLAO;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XPathAO;

public class ToXMLPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreanObject<T>, XMLStreamObject<T>>
{
	private IStreamObjectDataHandler<XMLStreamObject<? extends IMetaAttribute>> xsoHandler = new XMLStreamObjectDataHandler();
	private List<String> expressions;
	private List<SDFAttribute> attributes;
	private static TransformerFactory transformerFactory = TransformerFactory.newInstance();

	
	public ToXMLPO(List<String> _expressions, List<SDFAttribute> _attributes)
	{
		expressions = _expressions;
		attributes = _attributes;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port)
	{
		sendPunctuation(punctuation, port);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode()
	{
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void process_next(XMLStreanObject<T> object, int port)
	{
		DynamicXMLTransformer xmlTransformer = new DynamicXMLTransformer();
		Map<String, String> xpaths = new LinkedHashMap<String, String>();
		SDFSchema schema = getInputSchema(port);

		for (int i = 0; i < attributes.size(); i++)
		{
			int index = schema.findAttributeIndex(attributes.get(i).getURI());
			xpaths.put(expressions.get(i), object.getAttribute(index).toString());
		}
		Document targetDoc;
		try
		{
			targetDoc = xmlTransformer.transform(xpaths);
			DOMSource source = new DOMSource(targetDoc);

			Transformer transformer = null;

			StringWriter sw = new StringWriter();
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, new StreamResult(sw));

			XMLStreamObject<T> output = (XMLStreamObject<T>) xsoHandler.readData(sw.toString());
			if (output != null && !output.isEmpty())
			{
				output.setMetadata((T) object.getMetadata().clone());
				transfer(output);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
