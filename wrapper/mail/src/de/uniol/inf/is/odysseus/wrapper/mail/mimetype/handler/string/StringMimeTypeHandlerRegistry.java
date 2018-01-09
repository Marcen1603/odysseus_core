package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeHandlerRegistry;

public class StringMimeTypeHandlerRegistry extends MimeTypeHandlerRegistry<String> {

	@Override
	protected void Init() {
		this.RegisterHandler(new TextPlainHandler());
		this.RegisterHandler(new TextHtmlHandler());
		this.RegisterHandler(new MultipartAlternativeHandler("text/plain"));
		this.RegisterHandler(new MultipartMixedHandler("\n"));
		this.RegisterHandler(new ApplicationOctetStreamHandler());
	}

	@Override
	public SDFDatatype GetOutputSchemaDataType() {
		return SDFDatatype.STRING;
	}

}
