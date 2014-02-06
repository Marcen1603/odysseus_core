/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.smtp.physicaloperator.access;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;
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

    public SMTPTransportHandler(final IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    @Override
    public void send(final byte[] message) throws IOException {
        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setSubject(getSubject());
            mimeMessage.setText(ByteBuffer.wrap(message).asCharBuffer().toString());
            mimeMessage.setFrom(new InternetAddress(getFrom()));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(getTo()));
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        }
        catch (MessagingException e) {
            LOG.error(e.getMessage(), e);
            LOG.debug(ByteBuffer.wrap(message).asCharBuffer().toString());
            throw new IOException(e);
        }
    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        final SMTPTransportHandler handler = new SMTPTransportHandler(protocolHandler);
        handler.setOptionsMap(options);
        handler.init(options);
        return handler;
    }

    private void init(final Map<String, String> options) {
        if (options.containsKey(HOST)) {
            setHost(options.get(HOST));
        }
        else {
            setHost("localhost");
        }
        if (options.containsKey(PORT)) {
            setPort(Integer.parseInt(options.get(PORT)));
        }
        else {
            setPort(587);
        }
        if (options.containsKey(TLS)) {
            setTLS(Boolean.parseBoolean(options.get(TLS)));
        }
        else {
            setTLS(true);
        }
        if (options.containsKey(FROM)) {
            setFrom(options.get(FROM));
        }
        if (options.containsKey(TO)) {
            setTo(options.get(TO));
        }
        if (options.containsKey(SUBJECT)) {
            setSubject(options.get(SUBJECT));
        }
        if (options.containsKey(USERNAME)) {
            setUsername(options.get(USERNAME));
        }
        if (options.containsKey(PASSWORD)) {
            setPassword(options.get(PASSWORD));
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(String to) {
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
    public void setFrom(String from) {
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
    public void setSubject(String subject) {
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
    public void setUsername(String username) {
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
    public void setPassword(String password) {
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
    public void setHost(String host) {
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
    public void setPort(int port) {
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
    public void setTLS(boolean flag) {
        this.tls = flag;
    }

    /**
     * @return the tls
     */
    public boolean isTLS() {
        return this.tls;
    }

    private boolean isAuth() {
        return ((getUsername() != null) && (getPassword() != null));
    }

    public void processInOpen() throws UnknownHostException, IOException {

    }

    @Override
    public void processOutOpen() throws IOException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", getHost());
        properties.setProperty("mail.smtp.auth", Boolean.toString(isAuth()));
        properties.setProperty("mail.smtp.starttls.enable", Boolean.toString(isTLS()));
        properties.setProperty("mail.smtp.host", getHost());
        properties.setProperty("mail.smtp.port", Integer.toString(getPort()));
        try {
            session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(getUsername(), getPassword());
                }
            });
            transport = session.getTransport("smtp");
            transport.connect();
        }
        catch (MessagingException e) {
            LOG.error(e.getMessage(), e);
            LOG.debug(properties.toString());
            throw new IOException(e);
        }
    }

    @Override
    public void processInClose() throws IOException {
        this.fireOnDisconnect();
    }

    @Override
    public void processOutClose() throws IOException {

        transport = null;
        session = null;
        this.fireOnDisconnect();
    }

    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
        if (!(o instanceof SMTPTransportHandler)) {
            return false;
        }
        SMTPTransportHandler other = (SMTPTransportHandler) o;
        if (!this.host.equals(other.host)) {
            return false;
        }
        else if (!this.from.equals(other.from)) {
            return false;
        }
        else if (!this.to.equals(other.to)) {
            return false;
        }

        return true;
    }
}
