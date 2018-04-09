package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import org.w3c.dom.Element;

public class MapPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreamObject<T>> {
	private List<SDFAttribute> target;
	private List<SDFAttribute> source;

	public MapPO(List<SDFAttribute> _source, List<SDFAttribute> _target) {
		super();
		source = _source;
		target = _target;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation, port);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(XMLStreamObject<T> object, int port) {
		try {
			for (int i = 0; i < source.size(); i++) {
				
				NodeList sourceNodes = object.getValueFromExpression(source.get(i).getURI());//TODO rename method
				NodeList targetNodes = object.getNodeList(target.get(i).getURI());
				
				for (int j = 0; j < sourceNodes.getLength(); j++) {
					for (int k = 0; k < targetNodes.getLength(); k++) {
						
						if (!XMLStreamObject.hasParent(sourceNodes, sourceNodes.item(j))
								&& !XMLStreamObject.hasParent(targetNodes, targetNodes.item(k))) {
							
							Node newNode = sourceNodes.item(j).cloneNode(true);
							
							if (sourceNodes.item(j).getOwnerDocument() != object.getDocument().getOwnerDocument()) {
								newNode = object.getDocument().importNode(newNode, true);
							}
							
							if (newNode.getNodeType() == Node.ATTRIBUTE_NODE) {
								((Element) targetNodes.item(k)).setAttribute(newNode.getNodeName(), newNode.getNodeValue());
							} else {
								targetNodes.item(k).setTextContent(newNode.getTextContent());
							}
							
						}
					}
				}
				
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		transfer(object);
	}

}
