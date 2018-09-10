package de.uniol.inf.is.odysseus.server.keyvalue.expression;

import java.util.List;

import de.uniol.inf.is.odysseus.core.expression.NamedAttributeExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class KeyValueExpression<M extends IMetaAttribute> extends NamedAttributeExpression<KeyValueObject<M>, M> {

	private static final long serialVersionUID = -8581946193403760772L;

	public KeyValueExpression(SDFExpression expression, List<SDFSchema> inputSchema) {
		super(expression, inputSchema);
	}

}
