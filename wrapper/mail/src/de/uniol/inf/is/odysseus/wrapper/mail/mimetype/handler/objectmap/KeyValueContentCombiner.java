package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.IContentCombiner;

public class KeyValueContentCombiner implements IContentCombiner<ObjectMap<IMetaAttribute>> {

	private String mimeType;

	public KeyValueContentCombiner(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public ObjectMap<IMetaAttribute> CombineSubContents(
			List<Pair<String, ObjectMap<IMetaAttribute>>> subcontents) {
		@SuppressWarnings("rawtypes")
		ObjectMap[] content = null;

		if (!subcontents.isEmpty()) {
			content = new ObjectMap[subcontents.size()];
			for (int i = 0; i < subcontents.size(); i++) {
				content[i] = subcontents.get(i).getE2();
			}
		}

		return Util.BuildObjectMap(this.mimeType, content);

	}
}
