package de.uniol.inf.is.odysseus.wrapper.mail.contentreader;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Thomas Vogelgesang
 *
 */
public class StringContentReader implements IContentReader<String> {

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(StringContentReader.class);

	@Override
	public String ReadContent(Message message) {
		try {
			return getText(message);
		} catch (IOException | MessagingException e) {
			this.LOG.error(e.getMessage(), e);
			return null;
		}
	}

	private String getText(Part part) throws IOException, MessagingException {
		if (part.isMimeType("text/plain")) {
			return (String) part.getContent();
		} else if (part.isMimeType("text/html")) {
			return (String) part.getContent(); // TODO optionally filter
												// HTML markup
		} else if (part.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart) part.getContent();
			return getText(mp.getBodyPart(0));// TODO which of alternative
												// parts to prefer?
		} else if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			String text = "";
			for (int i = 0; i < mp.getCount(); i++) {
				Part subPart = mp.getBodyPart(i);
				text += getText(subPart);// TODO seperator required?
			}
			return text;
		} else {
			throw new MessagingException("MIME type not supported");
		}
	}
}
