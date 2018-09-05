package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractTextPlainHandler;

public class TextPlainHandler extends AbstractTextPlainHandler<String> {

	@Override
	public String getContent(Part part) throws IOException, MessagingException {
		return (String) part.getContent();
	}

}
