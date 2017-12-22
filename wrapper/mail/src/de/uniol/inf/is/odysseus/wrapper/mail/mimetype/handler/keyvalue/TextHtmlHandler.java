package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.keyvalue;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractTextHtmlHandler;

public class TextHtmlHandler extends AbstractTextHtmlHandler<KeyValueObject<IMetaAttribute>> {

	@Override
	public KeyValueObject<IMetaAttribute> getContent(Part part)
			throws IOException, MessagingException, MimeTypeException {
		String html = (String) part.getContent();
		String processed = this.processHtml(html);

		return Util.BuildKeyValueObject(this.getMimeType(), processed);
	}

}
