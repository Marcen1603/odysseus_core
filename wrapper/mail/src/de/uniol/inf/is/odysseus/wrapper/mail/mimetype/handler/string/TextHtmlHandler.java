package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.AbstractMimeTypeHandler;

public class TextHtmlHandler extends AbstractMimeTypeHandler<String> {

	@Override
	public String getContent(Part part) throws IOException, MessagingException {
		return (String)part.getContent(); // TODO optionally filter HTML tags to extract plain text from HTML
	}

	@Override
	public String getMimeType() {
		return "text/html";
	}

}
