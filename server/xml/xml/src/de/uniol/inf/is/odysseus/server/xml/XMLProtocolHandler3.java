package de.uniol.inf.is.odysseus.server.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ganesh.transformer.DynamicXMLBuilder;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.util.ByteBufferBackedInputStream;

/**
 * XML Protocol Handler which splits a xml document by a given tag.
 *
 * @author Henrik Surm
 * @author Marco Grawunder
 * @author Stephan Sachal
 *
 */
public class XMLProtocolHandler3<T extends XMLStreamObject<? extends IMetaAttribute>> extends AbstractProtocolHandler<T> {

	private static final Logger log = LoggerFactory.getLogger(XMLProtocolHandler3.class);
	
	public static final String NAME = "XML3";
	public static final String TAG_TO_STRIP = "tag_to_strip";

	private InputStream input;
	private OutputStream output;
	private String tagToStrip;
	private DynamicXMLBuilder<?> builder = new DynamicXMLBuilder<>();

	
	public XMLProtocolHandler3() {
		super();
	}

	public XMLProtocolHandler3(final ITransportDirection direction, final IAccessPattern access,
			IStreamObjectDataHandler<T> dataHandler, OptionMap options) {
		super(direction, access, dataHandler, options);
		init_internal();
	}

	private void init_internal() {
		OptionMap options = optionsMap;
		if (options.get(TAG_TO_STRIP) != null) {
			this.tagToStrip = options.get(TAG_TO_STRIP).toString();
		}
		
	}

	@Override
	public void close() throws IOException {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			if (this.input != null) {
				this.input.close();
			}
		} else {
			this.output.close();
		}
		this.getTransportHandler().close();
	}

	@Override
	public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access,
			final OptionMap options, final IStreamObjectDataHandler<T> dataHandler) {
		return new XMLProtocolHandler3<T>(direction, access, dataHandler, options);
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public T getNext() throws IOException {
		parseXml(input);
		return null;
	}

	@Override
	public boolean hasNext() throws IOException {
		try {
			return this.input.available() > 0;
		} catch (Exception e) {
			log.error("error occurred during hashNext():" + e.getMessage());
			return false;
		}
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		this.getTransportHandler().open();
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			if (this.getAccessPattern().equals(IAccessPattern.PULL)
					|| this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)) {

				input = this.getTransportHandler().getInputStream();
			}
		} else {
			this.output = this.getTransportHandler().getOutputStream();
		}

		log.debug("open()");
	}

	private void parseXml(InputStream input) {
		
		try {
			if (input.available() == 0) {
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// if no tag was provided, return the complete document
		if (tagToStrip == null) {
			try {
				getTransfer().transfer(getDataHandler().readData(input));
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		builder.splitDocument(input, tagToStrip).forEach(d -> {
			getTransfer().transfer(getDataHandler().readData(d));
		});
			
	}

	@Override
	public void process(String message) {
		parseXml(new ByteArrayInputStream(message.getBytes()));
	}

	@Override
	public void process(String[] message) {
		parseXml(new ByteArrayInputStream(String.join("", message).getBytes()));
	}

	@Override
	public void process(InputStream message) {
		parseXml(message);
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		parseXml(new ByteBufferBackedInputStream(message));
	}

	@Override
	public void write(final T object) throws IOException {
		this.output.write(object.toString(true).getBytes());
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}
}
