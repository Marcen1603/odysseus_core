/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SubjectTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class IMAPTransportHandler extends AbstractPullTransportHandler {
    public static final String NAME = "IMAP";

    /** Logger */
    private final Logger LOG = LoggerFactory.getLogger(IMAPTransportHandler.class);

    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FOLDER = "folder";
    private static final String KEEP = "keep";
    private static final String PATTERN = "pattern";
    private String host;
    private String username;
    private String password;
    private int port;
    private boolean keep;
    private String pattern;
    private String folder;
    private Store store;
    private Session session;

    /**
     * 
     */
    public IMAPTransportHandler() {
        super();
    }

    public IMAPTransportHandler(final IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        super(protocolHandler, options);
        init(options);
    }

    @Override
    public void send(final byte[] message) throws IOException {

    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        final IMAPTransportHandler handler = new IMAPTransportHandler(protocolHandler, options);
        return handler;
    }

    private void init(final Map<String, String> options) {
        if (options.containsKey(IMAPTransportHandler.HOST)) {
            this.setHost(options.get(IMAPTransportHandler.HOST));
        }
        else {
            this.setHost("localhost");
        }
        if (options.containsKey(IMAPTransportHandler.PORT)) {
            this.setPort(Integer.parseInt(options.get(IMAPTransportHandler.PORT)));
        }
        else {
            this.setPort(993);
        }
        if (options.containsKey(IMAPTransportHandler.KEEP)) {
            this.setKeep(Boolean.parseBoolean(options.get(IMAPTransportHandler.KEEP)));
        }
        else {
            this.setKeep(true);
        }
        if (options.containsKey(IMAPTransportHandler.USERNAME)) {
            this.setUsername(options.get(IMAPTransportHandler.USERNAME));
        }
        if (options.containsKey(IMAPTransportHandler.PASSWORD)) {
            this.setPassword(options.get(IMAPTransportHandler.PASSWORD));
        }
        if (options.containsKey(IMAPTransportHandler.FOLDER)) {
            this.setFolder(options.get(IMAPTransportHandler.FOLDER));
        }
        else {
            setFolder("INBOX");
        }
        if (options.containsKey(IMAPTransportHandler.PATTERN)) {
            this.setPattern(options.get(IMAPTransportHandler.PATTERN));
        }
        else {
            this.setPattern("");
        }
    }

    @Override
    public String getName() {
        return IMAPTransportHandler.NAME;
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
     * @param keep
     *            the keep to set
     */
    public void setKeep(boolean keep) {
        this.keep = keep;
    }

    /**
     * @return the keep
     */
    public boolean isKeep() {
        return this.keep;
    }

    /**
     * @param pattern
     *            the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * @param folder
     *            the folder to set
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * @return the folder
     */
    public String getFolder() {
        return this.folder;
    }

    @Override
    public void processInOpen() throws UnknownHostException, IOException {
        final Properties properties = System.getProperties();

        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.host", this.getHost());
        properties.setProperty("mail.imap.port", Integer.toString(this.getPort()));
        properties.setProperty("mail.imap.socketFactory.port", Integer.toString(this.getPort()));
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        try {
            this.session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(IMAPTransportHandler.this.getUsername(), IMAPTransportHandler.this.getPassword());
                }
            });
            this.store = this.session.getStore("imap");
        }
        catch (final MessagingException e) {
            this.LOG.error(e.getMessage(), e);
            this.LOG.debug(properties.toString());
            throw new IOException(e);
        }
    }

    @Override
    public void processOutOpen() throws IOException {

    }

    @Override
    public void processInClose() throws IOException {
        this.store = null;
        this.session = null;
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
        return new IMAPInputStream(this.store, getFolder(), getPattern(), isKeep());
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
        if (!(o instanceof IMAPTransportHandler)) {
            return false;
        }
        final IMAPTransportHandler other = (IMAPTransportHandler) o;
        if (!this.host.equals(other.host)) {
            return false;
        }
        else if (!this.username.equals(other.username)) {
            return false;
        }
        else if (!this.folder.equals(other.folder)) {
            return false;
        }
        else if (!this.pattern.equals(other.pattern)) {
            return false;
        }
        return true;
    }

    private class IMAPInputStream extends InputStream {
        private InputStream inputStream;
        private final Store store;
        private final String folder;
        private final String pattern;
        private final boolean keep;

        public IMAPInputStream(final Store store, final String folder, final String pattern, final boolean keep) {
            this.store = store;
            this.folder = folder;
            this.pattern = pattern;
            this.keep = keep;
        }

        @Override
        public synchronized int read() throws IOException {
            if (this.inputStream != null) {
                return this.inputStream.read();
            }
            else {
                return -1;
            }
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            if (this.inputStream != null) {
                return this.inputStream.read(b, off, len);
            }
            else {
                return -1;
            }
        }

        @Override
        public void close() throws IOException {
            if (this.inputStream != null) {
                this.inputStream.close();
                this.inputStream = null;
            }
        }

        @Override
        public int available() throws IOException {
            if (this.inputStream == null) {
                try {
                    this.fetch();
                }
                catch (Exception e) {
                    IMAPTransportHandler.this.LOG.error(e.getMessage(), e);
                    throw e;
                }
            }
            if (this.inputStream != null) {
                return this.inputStream.available();
            }
            else {
                return 0;
            }
        }

        private void fetch() throws IOException {
            try {

                this.store.connect();
            }
            catch (final MessagingException e) {
                IMAPTransportHandler.this.LOG.error(e.getMessage(), e);
                throw new IOException(e);
            }
            try {
                final Folder inbox = this.store.getFolder(folder);
                if (inbox == null) {
                    IMAPTransportHandler.this.LOG.error("No folder " + folder);
                }
                if (!inbox.isOpen()) {
                    inbox.open(Folder.READ_ONLY);
                }
                final Message[] messages;
                if ((pattern != null) && (!"".equals(pattern))) {
                    SubjectTerm term = new SubjectTerm(pattern);
                    messages = inbox.search(term);
                }
                else {
                    messages = inbox.getMessages();
                }
                if (messages.length > 0) {
                    final Message message = messages[0];
                    Object content = message.getContent();
                    if (content instanceof String) {
                        this.inputStream = new ByteArrayInputStream(((String) content).getBytes());
                    }
                    else if (content instanceof Multipart) {
                        Multipart multiPart = (Multipart) content;
                        if (multiPart.getCount() > 0) {
                            BodyPart bodyPart = multiPart.getBodyPart(0);
                            this.inputStream = new ByteArrayInputStream(((String) bodyPart.getContent()).getBytes());
                        }
                    }
                    else if (content instanceof InputStream) {
                        this.inputStream = (InputStream) content;
                    }
                    if (!keep) {
                        message.setFlag(Flags.Flag.DELETED, true);
                    }
                }
                if (inbox.isOpen()) {
                    inbox.close(false);
                }
            }
            catch (final MessagingException e) {
                IMAPTransportHandler.this.LOG.error(e.getMessage(), e);
                throw new IOException(e);
            }
            finally {
                try {
                    this.store.close();
                }
                catch (final MessagingException e) {
                    IMAPTransportHandler.this.LOG.error(e.getMessage(), e);
                }
            }
        }
    }

}
