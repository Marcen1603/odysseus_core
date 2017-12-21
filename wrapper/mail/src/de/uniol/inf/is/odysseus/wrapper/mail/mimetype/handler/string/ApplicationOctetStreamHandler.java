package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.AbstractMimeTypeHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;

public class ApplicationOctetStreamHandler extends AbstractMimeTypeHandler<String> {

	private static final int BUFFER_SIZE = 1024;

	@Override
	public String getContent(Part part) throws IOException, MessagingException, MimeTypeException {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = new byte[BUFFER_SIZE];
		InputStream in = part.getInputStream();

		while (in.read(bytes) > 0) {
			sb.append(new String(bytes));
		}

		return sb.toString();
	}

	@Override
	public String getMimeType() {
		return "APPLICATION/octet-stream";
	}

}
