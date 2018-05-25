package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractNamedAttributeMapPO;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

public class XMLMapPO<K extends IMetaAttribute, T extends INamedAttributeStreamObject<K>> extends AbstractNamedAttributeMapPO<K, T> {
	
	
	public XMLMapPO(MapAO operator) {
		super(operator);
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
	@SuppressWarnings("unchecked")
	protected T createInstance() {
		return (T) XMLStreamObject.createInstance(super.getExpressions()
				.stream()
				.map(e -> e.name)
				.collect(Collectors.toList()));
	}

}
