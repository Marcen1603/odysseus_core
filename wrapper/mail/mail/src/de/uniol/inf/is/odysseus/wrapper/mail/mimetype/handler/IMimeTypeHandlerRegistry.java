package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public interface IMimeTypeHandlerRegistry<T> {

	public T HandlePart(Part p) throws MessagingException, MimeTypeException, IOException;

	public abstract SDFDatatype GetOutputSchemaDataType();

}
