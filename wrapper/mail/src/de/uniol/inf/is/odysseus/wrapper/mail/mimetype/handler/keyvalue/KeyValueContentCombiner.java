package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.keyvalue;

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

	@Override
	public KeyValueObject<IMetaAttribute> CombineSubContents(
			List<Pair<String, KeyValueObject<IMetaAttribute>>> subcontents) {
		@SuppressWarnings("rawtypes")
		KeyValueObject[] content = null;

		if (!subcontents.isEmpty()) {
			content = new KeyValueObject[subcontents.size()];
			for (int i = 0; i < subcontents.size(); i++) {
				content[i] = subcontents.get(i).getE2();
			}
		}

		return Util.BuildKeyValueObject(this.mimeType, content);

	}
}
