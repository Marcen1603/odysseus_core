package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractApplicationOctetStreamHandler;

public class ApplicationOctetStreamHandler
		extends AbstractApplicationOctetStreamHandler<ObjectMap<IMetaAttribute>> {

	@Override
	public ObjectMap<IMetaAttribute> getContent(Part part)
			throws IOException, MessagingException, MimeTypeException {
		Object content = ReadInputAsByteArray(part);
		return Util.BuildObjectMap(this.getMimeType(), content);
	}

}
