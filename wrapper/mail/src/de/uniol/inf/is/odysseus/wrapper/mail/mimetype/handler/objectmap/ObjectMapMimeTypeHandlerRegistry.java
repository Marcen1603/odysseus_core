package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.objectmap;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeHandlerRegistry;

public class ObjectMapMimeTypeHandlerRegistry extends MimeTypeHandlerRegistry<ObjectMap<IMetaAttribute>> {

	@Override
	protected void Init() {
		this.RegisterHandler(new TextPlainHandler());
		this.RegisterHandler(new TextHtmlHandler());
		this.RegisterHandler(new MultipartAlternativeHandler());
		this.RegisterHandler(new MultipartMixedHandler());
		this.RegisterHandler(new ApplicationOctetStreamHandler());
	}

	@Override
	public SDFDatatype GetOutputSchemaDataType() {
		return SDFDatatype.OBJECT_MAP;
	}

}
