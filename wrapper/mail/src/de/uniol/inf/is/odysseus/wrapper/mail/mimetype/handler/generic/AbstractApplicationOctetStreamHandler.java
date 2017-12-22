package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.AbstractMimeTypeHandler;

public abstract class AbstractApplicationOctetStreamHandler<T> extends AbstractMimeTypeHandler<T> {

	@Override
	public String getMimeType() {
		return "APPLICATION/octet-stream";
	}

}
