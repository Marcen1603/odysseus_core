package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.keyvalue;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public final class Util {

	public static final String MIME_TYPE = "mimetype";
	public static final String CONTENT = "content";

	public static KeyValueObject<IMetaAttribute> BuildKeyValueObject(String mimeType, Object content) {
		KeyValueObject<IMetaAttribute> kvo = new KeyValueObject<IMetaAttribute>();

		kvo.setAttribute(Util.MIME_TYPE, mimeType);
		kvo.setAttribute(Util.CONTENT, content);

		return kvo;
	}

}
