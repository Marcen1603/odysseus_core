package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.AbstractMimeTypeHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;

public class MultipartAlternativeHandler extends AbstractMimeTypeHandler<String> {

	@Override
	public String getContent(Part part) throws IOException, MessagingException, MimeTypeException {
		Multipart mp = (Multipart) part.getContent();
		
		// TODO which of alternative parts to prefer?
		Part body = mp.getBodyPart(0);
		return this.getRegistry().HandlePart(body);
	}

	@Override
	public String getMimeType() {
		return "multipart/alternative";
	}

}
