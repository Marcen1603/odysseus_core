package de.uniol.inf.is.odysseus.recommendation.lod.physicaloperator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
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
	private NamedExpression urlExpression = null;
	
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
	protected void process_next(Tuple<T> tuple, int port) {
		int attributeIndex = getInputSchema(port).findAttributeIndex(urlExpression.expression.getExpressionString());
		URL url = null;
		try {
			url = new URL(tuple.getAttributes()[attributeIndex].toString());
		} catch (MalformedURLException e) {
			tuple = tuple.append("<Error>", true);//TODO: better error handling
			transfer(tuple, port);
			return;
		}

		if(type.toLowerCase().equals("xml")) {
			processXML(tuple, url, port);
		} else {
			tuple = tuple.append("<Error>", true);//TODO: better error handling
			transfer(tuple, port);
		}
	}

	private void processXML(Tuple<T> tuple, URL url, int port) {
		try {
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			InputStream xml = connection.getInputStream();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xml);
			
			NodeList tags = document.getDocumentElement().getElementsByTagName(predicate);
			
			
			
			
			
			//FIXME: from here on the code is just for testing some stuff and should be rewritten
			if(tags.getLength() > 0) {
				
				String test = "";
				
				if(attribute != null) {
					test = "" + tags.item(0).getAttributes().getNamedItem(attribute).getNodeValue();//FIXME: NullPointer
				} else {
					test = "no attribute specified";//FIXME
				}
				
				
				tuple = tuple.append(test, true);
				transfer(tuple, port);
				
				
			}
			//FIXME: end of testing-stuff 
			
		
		
			
			
			tuple = tuple.append("null", true);
			transfer(tuple, port);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			tuple = tuple.append("<Error>", true);//TODO: better error handling
			transfer(tuple, port);
		}
	}
}
