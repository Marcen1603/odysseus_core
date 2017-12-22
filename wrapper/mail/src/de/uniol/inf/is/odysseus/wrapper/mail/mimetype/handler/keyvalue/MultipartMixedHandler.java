package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.keyvalue;

import java.lang.reflect.Array;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractMultipartMixedhandler;

public class MultipartMixedHandler extends AbstractMultipartMixedhandler<KeyValueObject<IMetaAttribute>> {

	@SuppressWarnings("unchecked")
	@Override
	protected KeyValueObject<IMetaAttribute> CombineSubContents(List<KeyValueObject<IMetaAttribute>> subcontents) {
		KeyValueObject<IMetaAttribute>[] content = null;
		
		if (!subcontents.isEmpty()) {
			content = (KeyValueObject<IMetaAttribute>[]) Array.newInstance(subcontents.get(0).getClass(), subcontents.size());
			content = subcontents.toArray(content);
		}
		
		return Util.BuildKeyValueObject(this.getMimeType(), content);

	}

}
