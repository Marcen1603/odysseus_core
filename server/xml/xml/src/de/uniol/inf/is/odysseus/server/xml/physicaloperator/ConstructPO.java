package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ConstructAO;

public class ConstructPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>,  XMLStreamObject<T>>
{
	private List<String> newExpressions;
	private List<String> expressions;

	public ConstructPO(List<String> _expressions, List<String> _newExpressions)
	{
		expressions = _expressions; 
		newExpressions = _newExpressions;
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
