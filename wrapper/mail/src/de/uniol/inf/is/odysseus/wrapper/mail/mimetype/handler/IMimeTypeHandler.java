package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

public interface IMimeTypeHandler<T> {

	public void setRegistry(MimeTypeHandlerRegistry<T> registry);
	
	public T getContent(Part part) throws IOException, MessagingException, MimeTypeException;
	
	public String getMimeType();
	
}
