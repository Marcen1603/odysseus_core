package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic;

public abstract class AbstractMultipartMixedhandler<T> extends AbstractMultipartHandler<T> {

	@Override
	public String getMimeType() {
		return "multipart/mixed";
	}
	
	

}
