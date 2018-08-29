package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

/**
 * Encapsulates all data required for accessing mail servers (including session
 * and store objects)
 * 
 * @author Thomas Vogelgesang
 *
 */
public abstract class MailConfiguration {

	private static final String HOST = "host";
	private static final String PORT = "port";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String FOLDER = "folder";
	private static final String KEEP = "keep";
	private static final String PATTERN = "pattern";
	private static final String READ_CONTENT = "readcontent";
	private static final String MIME_TYPE_HANDLER = "mimetypehandler";

	private static final String MARK_AS_READ = "markasread";
	private static final String UNREAD_ONLY = "unreadonly";

	private static final String DEFAULT_PATTERN = "";
	private static final String DEFAULT_FOLDER = "INBOX";
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_MIME_TYPE_HANDLER = "objectmap";

	private String host;
	private String username;
	private String password;
	private int port;
	private boolean keep;
	private String pattern;
	private String folder;
	private Store store;
	private Session session;
	private boolean readContent;
	private String mimeTypeHandler;
	private boolean markAsRead;
	private boolean unreadOnly;

	public Store getStore() {
		return store;
	}

	public void initStore() throws MessagingException {
		if (this.session == null) {
			this.initSession();
		}
		this.store = this.session.getStore(this.getProviderName());
	}

	public Session getSession() {
		return session;
	}

	private void initSession() {
		Properties properties = new Properties();

		InitProperties(properties);

		this.session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MailConfiguration.this.getUsername(),
						MailConfiguration.this.getPassword());
			}
		});
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

	/**
	 * 
	 * @return true if the email content should be read, false otherwise
	 */
	public boolean isReadContent() {
		return readContent;
	}

	/**
	 * 
	 * @param readContent
	 *            true if the email content should be read, false otherwise
	 */
	public void setReadContent(boolean readContent) {
		this.readContent = readContent;
	}

	/**
	 * 
	 * @return true if mails should be marked as read after reading, false
	 *         otherwise
	 */
	public boolean isMarkAsRead() {
		return markAsRead;
	}

	/**
	 * set to to true if mails should be marked as read after reading them,
	 * false otherwise
	 * 
	 * @param markAsRead
	 */
	public void setMarkAsRead(boolean markAsRead) {
		this.markAsRead = markAsRead;
	}

	/**
	 * 
	 * @return true if only unread mails should be loaded, false otherwise.
	 */
	public boolean isUnreadOnly() {
		return unreadOnly;
	}

	/**
	 * set to true if only unread mails should be read, false otherwise
	 * 
	 * @param unreadOnly
	 */
	public void setUnreadOnly(boolean unreadOnly) {
		this.unreadOnly = unreadOnly;
	}

	/**
	 * 
	 * @return the name of the mail protocol
	 */
	public abstract String getProviderName();

	/**
	 * 
	 * @return the default port for the respective protocol
	 */
	public abstract int getDefaultPort();

	/**
	 * Initializes the provider-specific properties required for creating the
	 * connection
	 * 
	 * @param properties
	 *            the property object to init
	 */
	protected abstract void InitProperties(Properties properties);

	public MailConfiguration() {
		super();
	}

	/**
	 * Initializes the MailConfiguration object from the given OptionMap
	 * 
	 * @param options
	 *            the OptionMap to use for initialization
	 */
	void init(final OptionMap options) {
		if (options.containsKey(MailConfiguration.HOST)) {
			this.setHost(options.get(MailConfiguration.HOST));
		} else {
			this.setHost(DEFAULT_HOST);
		}
		if (options.containsKey(MailConfiguration.PORT)) {
			this.setPort(Integer.parseInt(options.get(MailConfiguration.PORT)));
		} else {
			this.setPort(this.getDefaultPort());
		}
		if (options.containsKey(MailConfiguration.KEEP)) {
			this.setKeep(Boolean.parseBoolean(options.get(MailConfiguration.KEEP)));
		} else {
			this.setKeep(true);
		}
		if (options.containsKey(MailConfiguration.USERNAME)) {
			this.setUsername(options.get(MailConfiguration.USERNAME));
		}
		if (options.containsKey(MailConfiguration.PASSWORD)) {
			this.setPassword(options.get(MailConfiguration.PASSWORD));
		}
		if (options.containsKey(MailConfiguration.FOLDER)) {
			this.setFolder(options.get(MailConfiguration.FOLDER));
		} else {
			setFolder(MailConfiguration.DEFAULT_FOLDER);
		}
		if (options.containsKey(MailConfiguration.PATTERN)) {
			this.setPattern(options.get(MailConfiguration.PATTERN));
		} else {
			this.setPattern(MailConfiguration.DEFAULT_PATTERN);
		}
		if (options.containsKey(MailConfiguration.READ_CONTENT)) {
			this.setReadContent(Boolean.parseBoolean(options.get(MailConfiguration.READ_CONTENT)));
		}
		if (options.containsKey(MailConfiguration.MIME_TYPE_HANDLER)) {
			this.setMimeTypeHandler(options.get(MailConfiguration.MIME_TYPE_HANDLER));
		} else {
			this.setMimeTypeHandler(MailConfiguration.DEFAULT_MIME_TYPE_HANDLER);
		}
		if (options.containsKey(MailConfiguration.MARK_AS_READ)) {
			this.setMarkAsRead(Boolean.parseBoolean(options.get(MailConfiguration.MARK_AS_READ)));
		}
		if (options.containsKey(MailConfiguration.UNREAD_ONLY)) {
			this.setUnreadOnly(Boolean.parseBoolean(options.get(MailConfiguration.UNREAD_ONLY)));
		}
	}

	public boolean isSemanticallyEqualImpl(final MailConfiguration other) {
		if (other == null) {
			return false;
		}
		if (!this.host.equals(other.host)) {
			return false;
		} else if (!this.username.equals(other.username)) {
			return false;
		} else if (!this.folder.equals(other.folder)) {
			return false;
		} else if (!this.pattern.equals(other.pattern)) {
			return false;
		}
		return true;
	}

	public String getMimeTypeHandler() {
		return mimeTypeHandler;
	}

	public void setMimeTypeHandler(String mimeTypeHandler) {
		this.mimeTypeHandler = mimeTypeHandler;
	}

}
