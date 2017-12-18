package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SubjectTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MailInputStream extends InputStream {

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(MailInputStream.class);

	private Queue<Message> messageQueue;

	private MailConfiguration mailConfiguration;

	public Queue<Message> getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(Queue<Message> messageQueue) {
		this.messageQueue = messageQueue;
	}

	public MailConfiguration getMailConfiguration() {
		return mailConfiguration;
	}

	public MailInputStream(MailConfiguration mailConfiguration) {
		this.mailConfiguration = mailConfiguration;
		messageQueue = new LinkedList<Message>();
	}

	@Override
	public int read() throws IOException {
		throw new NotImplementedException();
	}

	public boolean HasNextMessage() {
		return !messageQueue.isEmpty();
	}

	public Message GetNextMessage() {
		return messageQueue.poll();
	}

	@Override
	public int available() throws IOException {
		// TODO Auto-generated method stub
		return super.available();
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		super.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		// TODO Auto-generated method stub
		super.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		// TODO Auto-generated method stub
		return super.markSupported();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		return super.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		// TODO Auto-generated method stub
		return super.read(b);
	}

	@Override
	public synchronized void reset() throws IOException {
		// TODO Auto-generated method stub
		super.reset();
	}

	@Override
	public long skip(long n) throws IOException {
		// TODO Auto-generated method stub
		return super.skip(n);
	}

	/**
	 * Retrieves all message objects from the mail server in order to queue them
	 * 
	 * @throws IOException
	 */
	void fetch() throws IOException {
		try {
			this.mailConfiguration.getStore().connect();

			Folder inbox = getInbox();
			Message[] messages = getMessages(inbox);
			QueueMessages(messages);
			
			inbox.close(true);

		} catch (final MessagingException e) {
			MailInputStream.this.LOG.error(e.getMessage(), e);
			throw new IOException(e);
		} finally {
			try {
				this.mailConfiguration.getStore().close();
			} catch (MessagingException e) {
				MailInputStream.this.LOG.error(e.getMessage(), e);
				throw new IOException(e);
			}
		}

	}

	private Folder getInbox() throws MessagingException {
		final Folder inbox = mailConfiguration.getStore().getFolder(mailConfiguration.getFolder());
		if (inbox == null) {
			MailInputStream.this.LOG.error("No folder " + mailConfiguration.getFolder());
			throw new MessagingException("Folder not found: " + mailConfiguration.getFolder());
		}
		if (!inbox.isOpen()) {
			inbox.open(Folder.READ_ONLY);
		}
		return inbox;
	}

	private Message[] getMessages(Folder inbox) throws MessagingException {
		final Message[] messages;
		if ((mailConfiguration.getPattern() != null) && (!"".equals(mailConfiguration.getPattern()))) {
			SubjectTerm term = new SubjectTerm(mailConfiguration.getPattern());
			messages = inbox.search(term);
		} else {
			messages = inbox.getMessages();
		}
		return messages;
	}

	private void QueueMessages(Message[] messages) throws MessagingException {
		if (messages.length > 0){
			for (Message message : messages) {
				messageQueue.add(message);
				if (!mailConfiguration.isKeep()) {
		            message.setFlag(Flags.Flag.DELETED, true);
		        }
			}
		}
	}

}
