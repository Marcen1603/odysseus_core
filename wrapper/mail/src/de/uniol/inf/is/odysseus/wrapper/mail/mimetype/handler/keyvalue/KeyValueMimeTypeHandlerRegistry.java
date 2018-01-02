package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.keyvalue;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeHandlerRegistry;

public class KeyValueMimeTypeHandlerRegistry extends MimeTypeHandlerRegistry<KeyValueObject<IMetaAttribute>> {

	@Override
	protected void Init() {
		this.RegisterHandler(new TextPlainHandler());
		this.RegisterHandler(new TextHtmlHandler());
		this.RegisterHandler(new MultipartAlternativeHandler());
		this.RegisterHandler(new MultipartMixedHandler());
		this.RegisterHandler(new ApplicationOctetStreamHandler());
	}

}
