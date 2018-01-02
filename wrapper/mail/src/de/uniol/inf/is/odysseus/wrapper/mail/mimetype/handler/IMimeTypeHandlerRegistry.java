package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

public interface IMimeTypeHandlerRegistry<T> {

	public T HandlePart(Part p) throws MessagingException, MimeTypeException, IOException;

}
