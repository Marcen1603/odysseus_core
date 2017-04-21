package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ToTupleAO;

public class ToTuplePO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, Tuple<T>>
{

	private IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>> tHandler = new TupleDataHandler();

	public ToTuplePO(ToTupleAO operator)
	{
		this.tHandler = (IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>>) tHandler.createInstance(operator.getOutputSchema());
		setOutputSchema(operator.getOutputSchema());
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

	@Override
	protected void process_next(XMLStreamObject<T> object, int port)
	{
		// TODO Auto-generated method stub

	}

}
