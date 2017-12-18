package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler;

import java.io.IOException;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Part;

public abstract class AbstractMimeTypeHandler<T> implements IMimeTypeHandler<T> {

	private MimeTypeHandlerRegistry<T> registry;

	@Override
	public void setRegistry(MimeTypeHandlerRegistry<T> registry) {
		this.registry = registry;
	}

	protected MimeTypeHandlerRegistry<T> getRegistry() {
		return this.registry;
	}

	

}
