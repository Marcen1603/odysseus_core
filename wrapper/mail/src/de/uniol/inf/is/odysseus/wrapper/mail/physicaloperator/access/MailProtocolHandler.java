package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeHandlerRegistry;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.MultipartAlternativeHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.MultipartMixedHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.TextHtmlHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.TextPlainHandler;

public class MailProtocolHandler extends AbstractProtocolHandler<KeyValueObject<IMetaAttribute>> {

	private static final String FLAG_EXTENSION = "_flag";
	private static final String SENT_DATE = "SentDate";
	private static final String SUBJECT = "Subject";
	private static final String REPLY_TO = "ReplyTo";
	private static final String BCC = "BCC";
	private static final String CC = "CC";
	private static final String FROM = "From";
	private static final String TO = "To";

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(MailProtocolHandler.class);

	private static String NAME = "Mail";

	private MailInputStream inputStream;

	private MimeTypeHandlerRegistry<String> mimeTypeHandlers;

	public MailProtocolHandler() {
	}

	public MailProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
	}

	@Override
	public IProtocolHandler<KeyValueObject<IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler) {
		IProtocolHandler<KeyValueObject<IMetaAttribute>> instance = new MailProtocolHandler(direction, access, options,
				dataHandler);
		return instance;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		if (!(other instanceof MailProtocolHandler)) {
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
		if (in == null || !(in instanceof MailInputStream)) {
			throw new IOException("Transport handler does not provide mail input stream");
		}
		this.inputStream = (MailInputStream) in;

		InitMimeTypeHandlers();
	}

	private void InitMimeTypeHandlers() {
		// TODO handler initialization should be more generic and extensible
		// (e.g. provided by a service)
		mimeTypeHandlers = new MimeTypeHandlerRegistry<>();
		mimeTypeHandlers.RegisterHandler(new TextPlainHandler());
		mimeTypeHandlers.RegisterHandler(new TextHtmlHandler());
		mimeTypeHandlers.RegisterHandler(new MultipartAlternativeHandler());
		mimeTypeHandlers.RegisterHandler(new MultipartMixedHandler("\n"));
	}

	@Override
	public boolean hasNext() throws IOException {
		return inputStream.HasNextMessage();
	}

	@Override
	public KeyValueObject<IMetaAttribute> getNext() throws IOException {
		KeyValueObject<IMetaAttribute> kvo = KeyValueObject.createInstance();

		Message message = inputStream.GetNextMessage();
		if (message == null) {
			throw new IOException("Mail was null");
		}

		try {
			AddAddresses(kvo, message);

			kvo.setAttribute(SUBJECT, message.getSubject());
			kvo.setAttribute(SENT_DATE, message.getSentDate());

			AddFlags(kvo, message);

			kvo.setAttribute("content", this.mimeTypeHandlers.HandlePart(message));

		} catch (MessagingException | MimeTypeException e) {
			this.LOG.error(e.getMessage(), e);
		}
		
		System.out.println(kvo.toString());

		return kvo;
	}

	private void AddAddresses(KeyValueObject<IMetaAttribute> kvo, Message message) throws MessagingException {
		addAddresses(kvo, FROM, message.getFrom());
		addAddresses(kvo, TO, message.getRecipients(RecipientType.TO));
		addAddresses(kvo, CC, message.getRecipients(RecipientType.CC));
		addAddresses(kvo, BCC, message.getRecipients(RecipientType.BCC));
		addAddresses(kvo, REPLY_TO, message.getReplyTo());
	}

	private void AddFlags(KeyValueObject<IMetaAttribute> kvo, Message message) throws MessagingException {
		Flags flags = message.getFlags();
		if (flags != null) {
			addFlag(kvo, flags, Flag.ANSWERED);
			addFlag(kvo, flags, Flag.DELETED);
			addFlag(kvo, flags, Flag.DRAFT);
			addFlag(kvo, flags, Flag.FLAGGED);
			addFlag(kvo, flags, Flag.RECENT);
			addFlag(kvo, flags, Flag.SEEN);
			addFlag(kvo, flags, Flag.USER);
		}
	}

	private void addFlag(KeyValueObject<IMetaAttribute> kvo, Flags flags, Flag flag) {
		kvo.setAttribute(flag.toString() + FLAG_EXTENSION, flags.contains(flag));
	}

	private void addAddresses(KeyValueObject<IMetaAttribute> kvo, String key, Address[] addresses) {
		if (addresses != null && addresses.length > 0) {
			kvo.setAttribute(key, addressesToStrings(addresses));
		}
	}

	private String[] addressesToStrings(Address[] addresses) {
		String[] strings = new String[addresses.length];

		for (int i = 0; i < addresses.length; i++) {
			strings[i] = addresses[i].toString();
		}

		return strings;
	}

}
