package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.AbstractMimeTypeHandler;

public abstract class AbstractApplicationOctetStreamHandler<T> extends AbstractMimeTypeHandler<T> {

	private static final int BUFFER_SIZE = 1024;

	@Override
	public String getMimeType() {
		return "APPLICATION/octet-stream";
	}

	/**
	 * Reads the input stream of the given part as string
	 * 
	 * @param part
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
	protected String ReadInputAsString(Part part) throws IOException, MessagingException {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = new byte[BUFFER_SIZE];
		InputStream in = part.getInputStream();

		while (in.read(bytes) > 0) {
			sb.append(new String(bytes));
		}

		return sb.toString();
	}

	/**
	 * Reads the input stream of the given part as a byte array
	 * 
	 * @param part
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
	protected byte[] ReadInputAsByteArray(Part part) throws IOException, MessagingException {
		InputStream in = part.getInputStream();
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[BUFFER_SIZE];

			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}

			return out.toByteArray();
		}
	}
}
