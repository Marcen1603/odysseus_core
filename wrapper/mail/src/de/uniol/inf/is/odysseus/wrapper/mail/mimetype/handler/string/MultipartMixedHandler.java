package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.AbstractMimeTypeHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;

public class MultipartMixedHandler extends AbstractMimeTypeHandler<String> {

	private String seperator;
	
	public MultipartMixedHandler(String seperator) {
		this.seperator = seperator;
	}

	@Override
	public String getContent(Part part) throws IOException, MessagingException, MimeTypeException {
		String text = "";
		Multipart mp = (Multipart) part;

		for (int i = 0; i < mp.getCount(); i++) {
			Part p = mp.getBodyPart(i);

			if (text.length() > 0) {
				text += seperator;
			}
			text += this.getRegistry().HandlePart(p);
		}

		return text;
	}

	@Override
	public String getMimeType() {
		return "multipart/mixed";
	}

}
