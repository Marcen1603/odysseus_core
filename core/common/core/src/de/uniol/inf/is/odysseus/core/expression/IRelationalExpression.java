package de.uniol.inf.is.odysseus.core.expression;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IRelationalExpression<M extends IMetaAttribute> extends IExpression<Tuple<M>, M> {

	void initVars(SDFSchema schema);

	void initVars(SDFSchema left, SDFSchema right);

	void initVars(List<SDFSchema> schema);

}
