package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.keyvalue;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractMultipartMixedhandler;

public class MultipartMixedHandler extends AbstractMultipartMixedhandler<KeyValueObject<IMetaAttribute>> {

	public MultipartMixedHandler() {
		super();
		this.setCombiner(new KeyValueContentCombiner(this.getMimeType()));
	}

}
