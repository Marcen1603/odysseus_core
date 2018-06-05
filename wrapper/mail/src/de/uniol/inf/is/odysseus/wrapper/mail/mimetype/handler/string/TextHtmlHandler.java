package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractTextHtmlHandler;

public class TextHtmlHandler extends AbstractTextHtmlHandler<String> {

	@Override
	public String getContent(Part part) throws IOException, MessagingException {
		String html = (String) part.getContent();
		return processHtml(html);
	}

}
