package de.uniol.inf.is.odysseus.server.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 */
public class XMLProtocolHandler3<T extends XMLStreamObject<? extends IMetaAttribute>> extends AbstractProtocolHandler<T> {

	private static final Logger log = LoggerFactory.getLogger(XMLProtocolHandler3.class);

	public static final String NAME = "XML3";
	public static final String TAG_TO_STRIP = "tag_to_strip";

	private InputStream input;
	private OutputStream output;
	private String tagToStrip;
	private Collection<T> result = new LinkedList<>();
	private XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	private XMLEventReader eventReader;

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
		return parseXml(input);
	}

	@Override
	public boolean hasNext() throws IOException {
		try {
			return result.size() > 0 || this.input.available() > 0;
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

	private T parseXml(InputStream input) {

		// no tag was provided, close connection and throw exception
		if (tagToStrip == null) {
			try {
				close();
			} catch (IOException e) {  
				e.printStackTrace();
			}
			throw new IllegalArgumentException("option " + TAG_TO_STRIP + " was null: ");
		}
		
		try {

			if (input.available() == 0) {
				return null;
			}

			eventReader = inputFactory.createXMLEventReader(input);

			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLEventWriter eventWriter = null;
			StringWriter XMLOutput = new StringWriter();
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.getEventType() == XMLStreamConstants.START_ELEMENT
						&& event.asStartElement().getName().getLocalPart().equalsIgnoreCase(tagToStrip)) {
					eventWriter = outputFactory.createXMLEventWriter(XMLOutput);
					eventWriter.add(event);
				} else if (event.getEventType() == XMLStreamConstants.END_ELEMENT
						&& event.asEndElement().getName().getLocalPart().equalsIgnoreCase(tagToStrip)) {
					eventWriter.add(event);
					eventWriter.close();
					eventWriter = null;
					return this.getDataHandler().readData(XMLOutput.toString());
				} else if (eventWriter != null) {
					eventWriter.add(event);
				}
			}
			eventReader.close();

		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
			log.error("error occurred during parseXml():" + e.getMessage());
		}

		return null;
	}

	@Override
	public void process(String message) {
		getTransfer().transfer(parseXml(new ByteArrayInputStream(message.getBytes())));
	}

	@Override
	public void process(String[] message) {
		getTransfer().transfer(parseXml(new ByteArrayInputStream(String.join("", message).getBytes())));
	}

	@Override
	public void process(InputStream message) {
		getTransfer().transfer(parseXml(message));
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		getTransfer().transfer(parseXml(new ByteBufferBackedInputStream(message)));
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
