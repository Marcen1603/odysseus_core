/**
 *
 */
package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class SMTPTransportHandler extends AbstractPushTransportHandler {
	public static final String NAME = "SMTP";

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(SMTPTransportHandler.class);

	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String TLS = "tls";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String SUBJECT = "subject";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	private String from;
	private String to;
	private String subject;
	private String host;
	private String username;
	private String password;
	private int port;
	private boolean tls;
	private Session session;
	private Transport transport;

	/**
	 *
	 */
	public SMTPTransportHandler() {
		super();
	}

	public SMTPTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	@Override
	public void send(final byte[] message) throws IOException {
		try {
			String str = new String(message, StandardCharsets.UTF_8);
			final Message mimeMessage = new MimeMessage(this.session);
			mimeMessage.setSubject(this.getSubject());
			mimeMessage.setText(str);
			mimeMessage.setFrom(new InternetAddress(this.getFrom()));
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(this.getTo()));
			if (!this.transport.isConnected()){
				reconnect();
			}
			this.transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		} catch (final MessagingException e) {
			this.LOG.error(e.getMessage(), e);
			this.LOG.debug(ByteBuffer.wrap(message).asCharBuffer().toString());
			throw new IOException(e);
		}
	}

	@Override
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		final SMTPTransportHandler handler = new SMTPTransportHandler(protocolHandler, options);
		return handler;
	}

	private void init(final OptionMap options) {
		setHost(options.get(SMTPTransportHandler.HOST, "localhost"));
		setPort(options.getInt(SMTPTransportHandler.PORT, 587));
		setTLS(options.getBoolean(SMTPTransportHandler.TLS, true));
		setFrom(options.get(SMTPTransportHandler.FROM, ""));
		setTo(options.get(SMTPTransportHandler.TO, ""));
		setSubject(options.get(SMTPTransportHandler.SUBJECT, ""));
		setUsername(options.get(SMTPTransportHandler.USERNAME, ""));
		setPassword(options.get(SMTPTransportHandler.PASSWORD, ""));
	}

	@Override
	public String getName() {
		return SMTPTransportHandler.NAME;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final String to) {
		this.to = to;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return this.to;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(final String from) {
		this.from = from;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(final int port) {
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @param tls
	 *            the tls to set
	 */
	public void setTLS(final boolean flag) {
		this.tls = flag;
	}

	/**
	 * @return the tls
	 */
	public boolean isTLS() {
		return this.tls;
	}

	private boolean isAuth() {
		return ((this.getUsername() != "") && (this.getPassword() != ""));
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {

	}

	@Override
	public void processOutOpen() throws IOException {
		reconnect();
	}

	private void reconnect() throws IOException {
		final Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.auth", Boolean.toString(this.isAuth()));
		properties.setProperty("mail.smtp.starttls.enable", Boolean.toString(this.isTLS()));
		properties.setProperty("mail.smtp.host", this.getHost());
		properties.setProperty("mail.smtp.port", Integer.toString(this.getPort()));

		try {
			if (this.isAuth()) {
				this.session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(SMTPTransportHandler.this.getUsername(),
								SMTPTransportHandler.this.getPassword());
					}
				});
			} else {
				this.session = Session.getInstance(properties);
			}
			this.transport = this.session.getTransport("smtp");
			this.transport.connect();
		} catch (final MessagingException e) {
			this.LOG.error(e.getMessage(), e);
			this.LOG.debug(properties.toString());
			throw new IOException(e);
		}
	}

	@Override
	public void processInClose() throws IOException {
		this.fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
		this.transport = null;
		this.session = null;
		this.fireOnDisconnect();
	}

	@Override
	public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
		if (!(o instanceof SMTPTransportHandler)) {
			return false;
		}
		final SMTPTransportHandler other = (SMTPTransportHandler) o;
		if (!this.from.equals(other.from)) {
			return false;
		} else if (!this.to.equals(other.to)) {
			return false;
		} else if (!this.subject.equals(other.subject)) {
			return false;
		}
		return true;
	}
}
