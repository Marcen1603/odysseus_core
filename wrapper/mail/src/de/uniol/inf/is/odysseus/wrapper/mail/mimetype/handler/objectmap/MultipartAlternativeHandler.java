package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractMultipartAlternativeHandler;

public class MultipartAlternativeHandler extends AbstractMultipartAlternativeHandler<ObjectMap<IMetaAttribute>> {

	public MultipartAlternativeHandler(){
		super();
		this.setCombiner(new ObjectMapContentCombiner(this.getMimeType()));
	}

}
