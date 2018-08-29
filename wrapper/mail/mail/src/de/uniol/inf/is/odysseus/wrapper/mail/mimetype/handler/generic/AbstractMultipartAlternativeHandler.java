package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic;

public abstract class AbstractMultipartAlternativeHandler<T> extends AbstractMultipartHandler<T> {

	@Override
	public String getMimeType() {
		return "multipart/alternative";
	}

}
