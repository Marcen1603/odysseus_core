package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.w3c.dom.Node;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

public class ProjectPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreamObject<T>> {
	private List<SDFAttribute> paths;

	public ProjectPO(List<SDFAttribute> paths) {
		super();
		this.paths = paths;
	}

	public ProjectPO(ProjectPO<T> po) {
		super(po);
		this.paths = po.paths;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(XMLStreamObject<T> object, int port) {
		DocumentBuilder docBuilder;
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Node root = object.getDocument();
			for (SDFAttribute path : this.paths) {
				ArrayList<Node> nodeArray = new ArrayList<Node>();
				String pathURI = path.getURI();
				NodeList nl = object.xpathToNodeList(pathURI);

				for (int i = 0; i < nl.getLength(); i++) {
					Node child = nl.item(i);
					if (!child.equals(root)) {
						nodeArray.add(doc.importNode(child.cloneNode(true), false));
						Node parent = child.getParentNode();
						while (!parent.equals(root)) {
							// get all parents until root node is found
							nodeArray.add(doc.importNode(parent.cloneNode(false), false));
							parent = parent.getParentNode();
						}

					}
				}
				for (int i = nodeArray.size() - 1; i >= 1; i--) {
					nodeArray.get(i).appendChild(nodeArray.get(i - 1));
				}
				int nodeToAppend = nodeArray.size() - 1;
				for (int i = nodeArray.size() - 1; i >= 1; i--) {
					if (doc.getElementsByTagName(nodeArray.get(i).getNodeName()).getLength() == 0) {
						nodeToAppend = i;
						break;
					}
				}
				if (nodeToAppend < nodeArray.size() - 1) {
					String nodeName = nodeArray.get(nodeToAppend + 1).getNodeName();
					doc.getElementsByTagName(nodeName).item(0).appendChild(nodeArray.get(nodeToAppend));
				} else
					doc.appendChild(nodeArray.get(nodeArray.size() - 1));
			}
			XMLStreamObject<IMetaAttribute> newObject = XMLStreamObject.createInstance(doc);
			newObject.setMetadata(object.getMetadata().clone());
			if (!newObject.isEmpty())
				transfer((XMLStreamObject<T>) newObject);
		} catch (ParserConfigurationException | XPathFactoryConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
