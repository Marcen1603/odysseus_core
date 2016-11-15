package de.uniol.inf.is.odysseus.core.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IGenericMetaAttribute extends IMetaAttribute {

	void clearSchema();
	void addSchema(SDFSchema schema);
	void addSchema(SDFMetaSchema schema);
	void addSchema(List<SDFMetaSchema> schema);
	
	void setContent(Tuple<?> tuple, List<SDFMetaSchema> schema);
	Tuple<?> getContent();
	
	
}
