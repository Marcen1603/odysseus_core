package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.mail.search.SubjectTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeHandlerRegistry;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.MultipartAlternativeHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.MultipartMixedHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.TextHtmlHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.TextPlainHandler;

public abstract class AbstractMailTransportHandler
		extends AbstractSimplePullTransportHandler<KeyValueObject<IMetaAttribute>> {

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(AbstractMailTransportHandler.class);

	private MailConfiguration mailConfig = null;

	private Queue<KeyValueObject<IMetaAttribute>> messageQueue;

	private MimeTypeHandlerRegistry<String> mimeTypeHandlers;

	/* Keys for Key-Value objects */
	private static final String FLAG_EXTENSION = "Flags";
	private static final String SENT_DATE = "SentDate";
	private static final String SUBJECT = "Subject";
	private static final String REPLY_TO = "ReplyTo";
	private static final String BCC = "BCC";
	private static final String CC = "CC";
	private static final String FROM = "From";
	private static final String TO = "To";

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
		InitMimeTypeHandlers();
		messageQueue = new LinkedList<KeyValueObject<IMetaAttribute>>();
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
		return null;
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

	@Override
	public boolean hasNext() {
		if (messageQueue.isEmpty()) {
			try {
				fetch();
			} catch (IOException e) {
				this.LOG.error(e.getMessage(), e);
			}
		}
		return !messageQueue.isEmpty();
	}

	private KeyValueObject<IMetaAttribute> BuildOutputElement(Message message) {
		KeyValueObject<IMetaAttribute> kvo = KeyValueObject.createInstance();

		try {
			addAddresses(kvo, message);

			kvo.setAttribute(SUBJECT, message.getSubject());
			kvo.setAttribute(SENT_DATE, message.getSentDate());

			addFlags(kvo, message);

			kvo.setAttribute("content", this.mimeTypeHandlers.HandlePart(message));

		} catch (MessagingException | MimeTypeException | IOException e) {
			this.LOG.error(e.getMessage(), e);
		}

		System.out.println(kvo.toString());

		return kvo;
	}

	@Override
	public KeyValueObject<IMetaAttribute> getNext() {
		KeyValueObject<IMetaAttribute> kvo = messageQueue.poll();
		return kvo;
	}

	private void addAddresses(KeyValueObject<IMetaAttribute> kvo, Message message) throws MessagingException {
		addAddresses(kvo, FROM, message.getFrom());
		addAddresses(kvo, TO, message.getRecipients(RecipientType.TO));
		addAddresses(kvo, CC, message.getRecipients(RecipientType.CC));
		addAddresses(kvo, BCC, message.getRecipients(RecipientType.BCC));
		addAddresses(kvo, REPLY_TO, message.getReplyTo());
	}

	private void addFlags(KeyValueObject<IMetaAttribute> kvo, Message message) throws MessagingException {
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

	private void InitMimeTypeHandlers() {
		// TODO handler initialization should be more generic and extensible
		// (e.g. provided by a service)
		mimeTypeHandlers = new MimeTypeHandlerRegistry<>();
		mimeTypeHandlers.RegisterHandler(new TextPlainHandler());
		mimeTypeHandlers.RegisterHandler(new TextHtmlHandler());
		mimeTypeHandlers.RegisterHandler(new MultipartAlternativeHandler());
		mimeTypeHandlers.RegisterHandler(new MultipartMixedHandler("\n"));
	}

	/**
	 * Retrieves all message objects from the mail server in order to queue them
	 * 
	 * @throws IOException
	 */
	void fetch() throws IOException {
		try {
			this.mailConfig.getStore().connect(this.mailConfig.getHost(), this.mailConfig.getPort(),
					this.mailConfig.getUsername(), this.mailConfig.getPassword());

			Folder inbox = getInbox();
			Message[] messages = getMessages(inbox);

			if (messages.length > 0) {
				for (Message message : messages) {
					KeyValueObject<IMetaAttribute> kvo = BuildOutputElement(message);
					messageQueue.add(kvo);
					if (!mailConfig.isKeep()) {
						message.setFlag(Flags.Flag.DELETED, true);
					}
				}
			}

			inbox.close(true);

		} catch (final MessagingException e) {
			this.LOG.error(e.getMessage(), e);
			throw new IOException(e);
		} finally {
			try {
				this.mailConfig.getStore().close();
			} catch (MessagingException e) {
				this.LOG.error(e.getMessage(), e);
				throw new IOException(e);
			}
		}

	}

	private Folder getInbox() throws MessagingException {
		final Folder inbox = mailConfig.getStore().getFolder(mailConfig.getFolder());
		if (inbox == null) {
			this.LOG.error("No folder " + mailConfig.getFolder());
			throw new MessagingException("Folder not found: " + mailConfig.getFolder());
		}
		if (!inbox.isOpen()) {
			inbox.open(Folder.READ_ONLY);
		}
		return inbox;
	}

	private Message[] getMessages(Folder inbox) throws MessagingException {
		final Message[] messages;
		if ((mailConfig.getPattern() != null) && (!"".equals(mailConfig.getPattern()))) {
			SubjectTerm term = new SubjectTerm(mailConfig.getPattern());
			messages = inbox.search(term);
		} else {
			messages = inbox.getMessages();
		}
		return messages;
	}

}
