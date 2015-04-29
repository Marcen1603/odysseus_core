package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class SDFMetaSchema extends SDFSchema {

	private static final long serialVersionUID = -3864023446708534293L;

	final private Class<? extends IMetaAttribute> metaAttribute;

	public SDFMetaSchema(String uri, Class<? extends IStreamObject<?>> type,
			Collection<SDFAttribute> attributes, Class<? extends IMetaAttribute> metaAttribute) {
		super(uri, type, attributes);
		this.metaAttribute = metaAttribute;
	}

	public Class<? extends IMetaAttribute> getMetaAttribute() {
		return metaAttribute;
	}

}
