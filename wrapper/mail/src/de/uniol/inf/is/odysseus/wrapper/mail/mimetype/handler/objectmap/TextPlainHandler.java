package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractTextPlainHandler;

public class TextPlainHandler extends AbstractTextPlainHandler<ObjectMap<IMetaAttribute>> {

	@Override
	public ObjectMap<IMetaAttribute> getContent(Part part) throws IOException, MessagingException, MimeTypeException {
		String text = (String)part.getContent();
		return Util.BuildObjectMap(this.getMimeType(), text);
	}

}
