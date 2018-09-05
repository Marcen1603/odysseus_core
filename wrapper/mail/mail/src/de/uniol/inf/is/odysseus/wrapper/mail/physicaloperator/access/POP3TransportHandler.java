package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class POP3TransportHandler extends AbstractMailTransportHandler {

	public static final String NAME = "POP3";
	
	public POP3TransportHandler(){
		super();
	}
	
	public POP3TransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new POP3TransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public MailConfiguration CreateMailConfiguration() {
		return new POP3MailConfiguration();
	}

}
