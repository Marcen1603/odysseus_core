package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractMultipartMixedhandler;

public class MultipartMixedHandler extends AbstractMultipartMixedhandler<String> {

	private String seperator;

	public MultipartMixedHandler(String seperator) {
		this.seperator = seperator;
	}

	@Override
	public String getContent(Part part) throws IOException, MessagingException, MimeTypeException {
		String text = "";
		if (part instanceof MimeMultipart) {
			MimeMultipart mp = (MimeMultipart) part;
			text = getContent(mp);
			
		} else if (part instanceof MimeMessage) {
			MimeMessage message = (MimeMessage) part;

			text = this.getContent((MimeMultipart) message.getContent());
		}
		return text;
	}

	public String getContent(MimeMultipart mp) throws MessagingException, MimeTypeException, IOException {
		String text = "";
		
		for (int i = 0; i < mp.getCount(); i++) {
			Part p = mp.getBodyPart(i);

			if (text.length() > 0) {
				text += seperator;
			}
			text += this.getRegistry().HandlePart(p);
		}
		
		return text;
	}

}
