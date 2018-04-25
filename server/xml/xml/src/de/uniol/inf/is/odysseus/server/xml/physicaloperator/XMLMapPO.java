package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractNamedAttributeMapPO;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XMLMapAO;

public class XMLMapPO<K extends IMetaAttribute, T extends INamedAttributeStreamObject<K>> extends AbstractNamedAttributeMapPO<K, T> {
	
	private T currentObj;
	private boolean isTupleOutput;
	
	public XMLMapPO(MapAO operator) {
		super(operator);
		
		if (!(operator instanceof XMLMapAO)) {
			throw new IllegalArgumentException();
		} else {
			this.isTupleOutput = ((XMLMapAO) operator).isTupleOutput();
		}
		
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
	public void process(T object, int port) {
		currentObj = object;
		super.process(object, port);
	}

	
	@Override
	protected T createInstance() {
		return currentObj;
	}

}
