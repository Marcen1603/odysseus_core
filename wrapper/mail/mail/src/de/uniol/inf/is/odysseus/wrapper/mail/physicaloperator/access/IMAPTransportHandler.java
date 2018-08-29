package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class IMAPTransportHandler extends AbstractMailTransportHandler {

	private static final String NAME = "IMAP";

	public IMAPTransportHandler() {
        super();
    }
	
	public IMAPTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new IMAPTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public MailConfiguration CreateMailConfiguration() {
		return new IMAPMailConfiguration();
	}

}
