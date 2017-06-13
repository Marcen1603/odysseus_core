package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.XMLStreanObject;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ToTupleAO;

public class ToTuplePO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreanObject<T>>
{
	private IStreamObjectDataHandler<XMLStreanObject<? extends IMetaAttribute>> tHandler = new TupleDataHandler();
	private List<String> expressions;

	public ToTuplePO(ToTupleAO operator)
	{
		this.tHandler = (IStreamObjectDataHandler<XMLStreanObject<? extends IMetaAttribute>>) tHandler.createInstance(operator.getOutputSchema());
		setOutputSchema(operator.getOutputSchema());
		expressions = operator.getExpressions();
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

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	protected void process_next(XMLStreamObject<T> object, int port)
	{
		// Lacking DataType support
		List<String> dataValues = new ArrayList<String>(getOutputSchema().size());
		for (String attr : expressions)
		{
			String xpath = attr;
			dataValues.add(object.xpathToNode(xpath).getTextContent());
		}

		XMLStreanObject<T> output = (XMLStreanObject<T>) tHandler.readData(dataValues.iterator());
		if (object.getMetadata() != null)
			output.setMetadata((T) object.getMetadata().clone());

		transfer(output);
	}

}
