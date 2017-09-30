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

import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObjectDataHandler;

public class ConstructPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreamObject<T>>
{
	private IStreamObjectDataHandler<XMLStreamObject<? extends IMetaAttribute>> xsoHandler = new XMLStreamObjectDataHandler();
	private List<String> newExpressions;
	private List<String> expressions;
	private static TransformerFactory transformerFactory = TransformerFactory.newInstance();

	public ConstructPO(List<String> _expressions, List<String> _newExpressions)
	{
		expressions = _expressions;
		newExpressions = _newExpressions;
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

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(XMLStreamObject<T> object, int port)
	{
		DynamicXMLTransformer xmlTransformer = new DynamicXMLTransformer();
		Map<String, String> xpaths = new LinkedHashMap<String, String>();
		for (int i = 0; i < expressions.size(); i++)
		{
			xpaths.put(newExpressions.get(i), expressions.get(i));
		}
		Document targetDoc;
		try
		{
			Map<String, String> completeXpaths = xmlTransformer.generateCompleteXpaths(xpaths, object.getDocument());
			targetDoc = xmlTransformer.transform(completeXpaths);
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
