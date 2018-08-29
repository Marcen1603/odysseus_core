package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.IContentCombiner;

public class ObjectMapContentCombiner implements IContentCombiner<ObjectMap<IMetaAttribute>> {

	private String mimeType;

	public ObjectMapContentCombiner(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public ObjectMap<IMetaAttribute> CombineSubContents(
			List<Pair<String, ObjectMap<IMetaAttribute>>> subcontents) {
		@SuppressWarnings("rawtypes")
		ArrayList<ObjectMap> content = new ArrayList<ObjectMap>();

		if (!subcontents.isEmpty()) {
			for (int i = 0; i < subcontents.size(); i++) {
				content.add(subcontents.get(i).getE2());
			}
		}

		return Util.BuildObjectMap(this.mimeType, content);

	}
}
