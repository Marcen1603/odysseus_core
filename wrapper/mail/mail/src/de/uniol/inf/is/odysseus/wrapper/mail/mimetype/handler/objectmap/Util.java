package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public final class Util {

	public static final String MIME_TYPE = "mimetype";
	public static final String CONTENT = "content";

	public static ObjectMap<IMetaAttribute> BuildObjectMap(String mimeType, Object content) {
		ObjectMap<IMetaAttribute> objectMap = new ObjectMap<IMetaAttribute>();

		objectMap.setAttribute(Util.MIME_TYPE, mimeType);
		objectMap.setAttribute(Util.CONTENT, content);

		return objectMap;
	}

}
