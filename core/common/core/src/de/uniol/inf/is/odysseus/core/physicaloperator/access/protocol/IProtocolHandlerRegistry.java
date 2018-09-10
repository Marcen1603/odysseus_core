package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public interface IProtocolHandlerRegistry {

	IProtocolHandler<?> getInstance(String name, ITransportDirection direction, IAccessPattern access,
			OptionMap options, @SuppressWarnings("rawtypes") IStreamObjectDataHandler dataHandler);

	ImmutableList<String> getHandlerNames();

	IProtocolHandler<?> getIProtocolHandlerClass(String protocolHandler);

}
