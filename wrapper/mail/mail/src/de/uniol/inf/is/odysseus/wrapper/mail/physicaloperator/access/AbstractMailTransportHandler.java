package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.search.SubjectTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.IMimeTypeHandlerRegistry;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap.ObjectMapMimeTypeHandlerRegistry;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string.StringMimeTypeHandlerRegistry;

/**
 * 
 * @author Thomas Vogelgesang
 *
 */
public abstract class AbstractMailTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> {

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(AbstractMailTransportHandler.class);

	private MailConfiguration mailConfig = null;

	private Queue<Tuple<IMetaAttribute>> messageQueue;

	private IMimeTypeHandlerRegistry<?> mimeTypeHandlers;

	/* attribute index in tuple */
	private static final int FROM = 0;
	private static final int TO = 1;
	private static final int CC = 2;
	private static final int BCC = 3;
	private static final int REPLY_TO = 4;
	private static final int SUBJECT = 5;
	private static final int SENT_DATE = 6;
	private static final HashMap<Flag, Integer> FLAG_INDICES;
	static {
		FLAG_INDICES = new HashMap<Flag, Integer>();
		FLAG_INDICES.put(Flag.DELETED, 7);
		FLAG_INDICES.put(Flag.ANSWERED, 8);
		FLAG_INDICES.put(Flag.DRAFT, 9);
		FLAG_INDICES.put(Flag.FLAGGED, 10);
		FLAG_INDICES.put(Flag.RECENT, 11);
		FLAG_INDICES.put(Flag.SEEN, 12);
		FLAG_INDICES.put(Flag.USER, 13);
	}
	private static final int CONTENT = 14;

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
		InitCommandMap();
		messageQueue = new LinkedList<Tuple<IMetaAttribute>>();
		setSchema(buildOutputSchema());
	}

	private SDFSchema buildOutputSchema() {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();

		attributes.add(new SDFAttribute("", "From", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("", "To", SDFDatatype.LIST_STRING));
		attributes.add(new SDFAttribute("", "CC", SDFDatatype.LIST_STRING));
		attributes.add(new SDFAttribute("", "BCC", SDFDatatype.LIST_STRING));
		attributes.add(new SDFAttribute("", "ReplyTo", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("", "Subject", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("", "SendDate", SDFDatatype.DATE));

		attributes.add(new SDFAttribute("", "FlagDeleted", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("", "FlagAnswered", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("", "FlagDraft", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("", "FlagFlagged", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("", "FlagRecent", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("", "FlagSeen", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("", "FlagUser", SDFDatatype.BOOLEAN));

		attributes.add(new SDFAttribute("", "Content", mimeTypeHandlers.GetOutputSchemaDataType()));

		return SDFSchemaFactory.createNewSchema("", Tuple.class, attributes);
	}

	private void InitCommandMap() {
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
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

	private Tuple<IMetaAttribute> BuildOutputElement(Message message) {
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(this.getSchema().size(), false);

		try {
			addAddresses(tuple, message);

			tuple.setAttribute(SUBJECT, message.getSubject());
			tuple.setAttribute(SENT_DATE, message.getSentDate());

			addFlags(tuple, message);

			addContent(tuple, message);

		} catch (MessagingException | MimeTypeException | IOException e) {
			this.LOG.error(e.getMessage(), e);
		}

		return tuple;
	}

	private void addContent(Tuple<IMetaAttribute> tuple, Message message)
			throws MessagingException, MimeTypeException, IOException {
		if (mailConfig.isReadContent()) {
			Object content = this.mimeTypeHandlers.HandlePart(message);
			tuple.setAttribute(CONTENT, content);
		}
	}

	@Override
	public Tuple<IMetaAttribute> getNext() {
		Tuple<IMetaAttribute> next = messageQueue.poll();
		return next;
	}

	private void addAddresses(Tuple<IMetaAttribute> tuple, Message message) throws MessagingException {
		addAddresses(tuple, FROM, message.getFrom());
		addAddresses(tuple, TO, message.getRecipients(RecipientType.TO));
		addAddresses(tuple, CC, message.getRecipients(RecipientType.CC));
		addAddresses(tuple, BCC, message.getRecipients(RecipientType.BCC));
		addAddresses(tuple, REPLY_TO, message.getReplyTo());
	}

	private void addFlags(Tuple<IMetaAttribute> tuple, Message message) throws MessagingException {
		Flags flags = message.getFlags();
		if (flags != null) {
			addFlag(tuple, flags, Flag.DELETED);
			addFlag(tuple, flags, Flag.ANSWERED);
			addFlag(tuple, flags, Flag.DRAFT);
			addFlag(tuple, flags, Flag.FLAGGED);
			addFlag(tuple, flags, Flag.RECENT);
			addFlag(tuple, flags, Flag.SEEN);
			addFlag(tuple, flags, Flag.USER);
		}
	}

	private void addFlag(Tuple<IMetaAttribute> kvo, Flags flags, Flag flag) {
		kvo.setAttribute(FLAG_INDICES.get(flag), flags.contains(flag));
	}

	private void addAddresses(Tuple<IMetaAttribute> tuple, int pos, Address[] addresses) {
		if (addresses != null && addresses.length > 0) {
			tuple.setAttribute(pos, addressesToStrings(addresses));
		}
	}

	private ArrayList<String> addressesToStrings(Address[] addresses) {
		ArrayList<String> strings = new ArrayList<>();

		for (int i = 0; i < addresses.length; i++) {
			strings.add(addresses[i].toString());
		}

		return strings;
	}

	private void InitMimeTypeHandlers() {
		if (mailConfig.getMimeTypeHandler().equals("string")) {
			mimeTypeHandlers = new StringMimeTypeHandlerRegistry();
		} else if (mailConfig.getMimeTypeHandler().equals("objectmap")) {
			mimeTypeHandlers = new ObjectMapMimeTypeHandlerRegistry();
		} else {
			throw new IllegalArgumentException("unknown mime type handler: " + mailConfig.getMimeTypeHandler());
		}

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
					if (!mailConfig.isUnreadOnly() || !message.getFlags().contains(Flag.SEEN)) {
						Tuple<IMetaAttribute> tuple = BuildOutputElement(message);
						messageQueue.add(tuple);
						if (!mailConfig.isKeep()) {
							message.setFlag(Flags.Flag.DELETED, true);
						}
						if (mailConfig.isMarkAsRead()) {
							message.setFlag(Flags.Flag.SEEN, true);
						}
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
			inbox.open(Folder.READ_WRITE);
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
