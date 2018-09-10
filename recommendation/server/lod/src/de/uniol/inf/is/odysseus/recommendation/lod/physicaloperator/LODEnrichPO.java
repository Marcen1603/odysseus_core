package de.uniol.inf.is.odysseus.recommendation.lod.physicaloperator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Christopher Schwarz
 */
public class LODEnrichPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	private String attribute = null;
	private String predicate = null;
	private String type = null;
	private NamedExpression urlExpression;
	
	private int attributeIndex;
	
	public LODEnrichPO(NamedExpression urlExpression, String type, String predicate, String attribute) {
		super();
		this.attribute = attribute;
		this.predicate = predicate;
		this.type = type;
		this.urlExpression = urlExpression;
	}

	@Override
	public OutputMode getOutputMode() {
	    return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator operator) {
		if(operator == null || !(operator instanceof LODEnrichPO)) return false;
		
		@SuppressWarnings("rawtypes")
		LODEnrichPO enrichOperator = (LODEnrichPO)operator;
		if(this.hasSameSources(enrichOperator)
		&& this.attribute.equals(enrichOperator.attribute)
		&& this.predicate.equals(enrichOperator.predicate)
		&& this.type.equals(enrichOperator.type)
		&& this.urlExpression.equals(enrichOperator.urlExpression)) {
			return true;
		}

		return true;
	}
	
	@Override
	protected void process_open() {
		super.process_open();
		attributeIndex = getInputSchema(0).findAttributeIndex(urlExpression.expression.getExpressionString());
	}

	@Override
	protected void process_next(Tuple<T> tuple, int port) {
		try {
			URL url = new URL(tuple.getAttributes()[attributeIndex].toString());
			if(type.toLowerCase().equals("xml")) {
				processXML(tuple, url, port);
			} else {
				tuple = tuple.append("<Error>", true);//TODO: better error handling
				transfer(tuple, port);
			}
		} catch (MalformedURLException e) {
			tuple = tuple.append("<Error>", true);//TODO: better error handling
			transfer(tuple, port);
		}
	}

	private void processXML(Tuple<T> tuple, URL url, int port) {
		HashSet<String> attributes = new HashSet<String>();
		
		try {
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			InputStream xml = connection.getInputStream();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xml);
			
			NodeList tags = document.getDocumentElement().getElementsByTagName(predicate);
			
			for(int i = 0; i < tags.getLength(); i++) {
				Node node = tags.item(i).getAttributes().getNamedItem(attribute);
				if(node != null) {
					attributes.add(node.getNodeValue());
				}
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			//TODO: better error handling
		}
		
		tuple = tuple.append(attributes, true);
		transfer(tuple, port);
	}
}
