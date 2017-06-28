package de.uniol.inf.is.odysseus.core.expression;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IRelationalExpression<T extends IMetaAttribute> extends IExpression {

	void initVars(SDFSchema schema);

	void initVars(SDFSchema left, SDFSchema right);

	void initVars(List<SDFSchema> schema);

	Object evaluate(Tuple<T> object, List<ISession> sessions, List<Tuple<T>> history);

}
