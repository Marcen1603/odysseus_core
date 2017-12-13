package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class MailProtocolHandler<T extends KeyValueObject<?>> extends AbstractProtocolHandler<T> {
	
	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(MailProtocolHandler.class);

	private static String NAME= "Mail";
	
	private MailInputStream inputStream;
	
	public MailProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		super(direction, access, dataHandler, options);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		IProtocolHandler<T> instance = new MailProtocolHandler<T>(direction, access, options, dataHandler);
		return instance;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		if (!(other instanceof MailProtocolHandler<?>)) {
			return false;
		}
		// the datahandler was already checked in the
		// isSemanticallyEqual-Method of AbstracProtocolHandler
		return true;
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		InputStream in = this.getTransportHandler().getInputStream();
		if (inputStream == null || !(inputStream instanceof MailInputStream)){
			throw new IOException("Transport handler does not provide mail input stream"); 
		}
		this.inputStream = (MailInputStream)in;
	}

	@Override
	public boolean hasNext() throws IOException {
		return inputStream.HasNextMessage();
	}

	@Override
	public T getNext() throws IOException {
		T kvObject = null;//readNextMessage();
		return kvObject;
	}

	private void readNextMessage() {
		KeyValueObject<?> kvo = KeyValueObject.createInstance();
		
		Message message = inputStream.GetNextMessage();
		try {
			Address[] allRecepients = message.getAllRecipients();
			String[] allRecepientsStr = new String[allRecepients.length]; //insert strings instead of Address objects?
			for (int i =0; i < allRecepients.length; i++) {
				allRecepientsStr[i] = allRecepients[i].toString();
			}
			kvo.setAttribute("RECEPIENTS", allRecepientsStr);
		} catch (MessagingException e) {
			this.LOG.error(e.getMessage(), e);
		}
	}

	
}
