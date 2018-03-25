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
	private boolean clone;

	public MapPO(List<SDFAttribute> _source, List<SDFAttribute> _target) {
		super();
		clone = true;
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
				NodeList sourceNodes = object.getNodeList(source.get(i).getURI());
				NodeList targetNodes;
				targetNodes = object.getNodeList(target.get(i).getURI());
				for (int j = 0; j < sourceNodes.getLength(); j++) {
					for (int k = 0; k < targetNodes.getLength(); k++) {
						if (!XMLStreamObject.hasParent(sourceNodes, sourceNodes.item(j))
								&& !XMLStreamObject.hasParent(targetNodes, targetNodes.item(k))) {
							Node newNode = sourceNodes.item(j).cloneNode(true);
							if (sourceNodes.item(j).getOwnerDocument() != object.getDocument().getOwnerDocument()) {
								newNode = object.getDocument().importNode(newNode, true);
							}
							if (clone) {
								if (newNode.getNodeType() == Node.ATTRIBUTE_NODE) {
									((Element) targetNodes.item(k)).setAttribute(newNode.getNodeName(),
											newNode.getNodeValue());
								} else {
									targetNodes.item(k).appendChild(newNode);
								}
							} else {
								targetNodes.item(k).appendChild(sourceNodes.item(j));
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transfer(object);
	}

}
