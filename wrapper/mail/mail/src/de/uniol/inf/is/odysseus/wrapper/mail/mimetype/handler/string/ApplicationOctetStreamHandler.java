package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractApplicationOctetStreamHandler;

public class ApplicationOctetStreamHandler extends AbstractApplicationOctetStreamHandler<String> {

	@Override
	public String getContent(Part part) throws IOException, MessagingException, MimeTypeException {
		return ReadInputAsString(part);
	}

}
