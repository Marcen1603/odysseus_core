package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.DOMException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ToTupleAO;

public class ToTuplePO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, Tuple<T>> {
	
	private IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>> tHandler = new TupleDataHandler();
	private List<String> expressions;

	public ToTuplePO(ToTupleAO operator) {
		this.tHandler = (IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>>) tHandler
				.createInstance(operator.getOutputSchema());
		setOutputSchema(operator.getOutputSchema());
		expressions = operator.getExpressions();
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
	@SuppressWarnings("unchecked")
	protected void process_next(XMLStreamObject<T> object, int port) {
		
		Collection<String> values = new ArrayList<String>(getOutputSchema().size());

		expressions.stream().forEach(e -> {
			
			try {
				values.add(object.getNode(e).getTextContent());
			} catch (DOMException e1) {
				e1.printStackTrace();
			} catch (XPathExpressionException e1) {
				e1.printStackTrace();
			}
			
		});
		
		
		Tuple<T> output = (Tuple<T>) tHandler.readData(values.iterator());
		if (object.getMetadata() != null) {
			output.setMetadata((T) object.getMetadata().clone());
		}

		transfer(output);
	}
}
