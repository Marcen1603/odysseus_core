package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public interface ITransportHandlerRegistry {

	ITransportHandler getInstance(String name, IProtocolHandler<?> protocolHandler, OptionMap options);

	ImmutableList<String> getHandlerNames();

	ITransportHandler getITransportHandlerClass(String transportHandler);

}
