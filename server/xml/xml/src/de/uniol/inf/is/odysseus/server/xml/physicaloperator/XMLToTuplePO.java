package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XMLToTupleAO;

public class XMLToTuplePO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, Tuple<T>> {

	private IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>> tHandler = new TupleDataHandler();
	private Map<String, String> expressions;
	private SDFSchema schema;
	private boolean isMapped;
	private Map<String, Integer> mapping;

	public XMLToTuplePO(XMLToTupleAO operator) {
		this.tHandler = (IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>>) tHandler
				.createInstance(operator.getOutputSchema());
		setOutputSchema(operator.getOutputSchema());
		expressions = operator.getExpressions();
		schema = operator.getOutputSchema();
		mapping = new HashMap<>();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation, port);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	protected void process_next(XMLStreamObject<T> object, int port) {

		if (!isMapped) {
			int index = 0;
			for (SDFAttribute attribute : schema.getAttributes()) {
				mapping.put(attribute.getAttributeName().toLowerCase(), index++);
			}
			isMapped = true;
		}

		List<NodeList> nodeLists = new ArrayList<>();
		List<String> names = new ArrayList<>();
		int maxSize = 0;
		for (Map.Entry<String, String> e : expressions.entrySet()) {
			try {
				NodeList nl = object.getNodeList(e.getValue());
				if (nl.getLength() > maxSize) {
					maxSize = nl.getLength();
				}
				nodeLists.add(nl);
				names.add(e.getKey());
			} catch (XPathExpressionException e1) {
				e1.printStackTrace();
			}
		}

		for (int index2 = 0; index2 < maxSize; index2++) {

			Tuple<T> t = new Tuple<T>(schema.size(), false);
			
			int index = 0;//index for attribute
			while (index < nodeLists.size()) {
				int index3 = 0;
				NodeList nl = nodeLists.get(index);
				while (index3 < nl.getLength()) {
					if (nl.getLength() == 1) {
						t.setAttribute(
								mapping.get(names.get(index)), 
								nl.item(0).getTextContent()
						);
					} else {
						if (index2 < nl.getLength()) {
							t.setAttribute(
									mapping.get(names.get(index)), 
									nl.item(index2).getTextContent()
							);
						}
					}
					index3++;
				}
				index++;
			}

			if (object.getMetadata() != null) {
				t.setMetadata((T) object.getMetadata().clone());
			}

			index2++;
			transfer(t);
		}
	}
	
}
