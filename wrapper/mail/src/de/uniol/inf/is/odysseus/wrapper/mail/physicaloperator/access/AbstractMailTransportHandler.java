package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public abstract class AbstractMailTransportHandler extends AbstractPullTransportHandler {

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(AbstractMailTransportHandler.class);

	private MailConfiguration mailConfig = null;
	
	private MailInputStream inputStream = null;
	
	/**
	 * 
	 */
	public AbstractMailTransportHandler() {
		super();
	}

	public AbstractMailTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		mailConfig = CreateMailConfiguration();
		mailConfig.init(options);
		inputStream = new MailInputStream(mailConfig);
	}
	
	@Override
	public void send(final byte[] message) throws IOException {

	}

	@Override
	public abstract String getName();

	/**
	 * 
	 * @return a newly created mail configuration object
	 */
	public abstract MailConfiguration CreateMailConfiguration();

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		try {
			this.mailConfig.initStore();
		} catch (final MessagingException e) {
			this.LOG.error(e.getMessage(), e);
			throw new IOException(e);
		}
	}

	@Override
	public void processOutOpen() throws IOException {

	}

	@Override
	public void processInClose() throws IOException {
		this.fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
		this.fireOnDisconnect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() {
		return this.inputStream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
		if (!(o instanceof AbstractMailTransportHandler)) {
			return false;
		}
		final AbstractMailTransportHandler other = (AbstractMailTransportHandler) o;
		return this.mailConfig.isSemanticallyEqualImpl(other.CreateMailConfiguration());
	}

}
