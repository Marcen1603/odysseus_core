package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

public class XMLProjectPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreamObject<T>> {
	private List<SDFAttribute> paths;

	public XMLProjectPO(List<SDFAttribute> paths) {
		super();
		this.paths = paths;
	}

	public XMLProjectPO(XMLProjectPO<T> po) {
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

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(XMLStreamObject<T> object, int port) {
		for (SDFAttribute path : this.paths) {
			try {
				// Get nodes from x-path expression
				NodeList nl = object.getNodeList(path.getURI());

				for (int i = 0; i < nl.getLength(); i++) {

					if (!XMLStreamObject.hasParent(nl, nl.item(i))) {

						XMLStreamObject<IMetaAttribute> newObject;

						newObject = XMLStreamObject.createInstance(nl.item(i));
						newObject.setMetadata(object.getMetadata().clone());

						if (!newObject.isEmpty()) {
							transfer((XMLStreamObject<T>) newObject);
						}
					}
				}
			} catch (XPathFactoryConfigurationException e) {
				e.printStackTrace();
			} catch (XPathExpressionException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
