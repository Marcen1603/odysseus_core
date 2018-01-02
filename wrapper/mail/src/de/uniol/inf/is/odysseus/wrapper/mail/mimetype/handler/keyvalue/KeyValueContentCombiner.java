package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.keyvalue;

import java.lang.reflect.Array;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.IContentCombiner;

public class KeyValueContentCombiner implements IContentCombiner<KeyValueObject<IMetaAttribute>> {

	private String mimeType;

	public KeyValueContentCombiner(String mimeType) {
		this.mimeType = mimeType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public KeyValueObject<IMetaAttribute> CombineSubContents(
			List<Pair<String, KeyValueObject<IMetaAttribute>>> subcontents) {
		KeyValueObject<IMetaAttribute>[] content = null;

		if (!subcontents.isEmpty()) {
			content = (KeyValueObject<IMetaAttribute>[]) Array.newInstance(subcontents.get(0).getE2().getClass(),
					subcontents.size());
			content = subcontents.toArray(content);
		}

		return Util.BuildKeyValueObject(this.mimeType, content);

	}
}
