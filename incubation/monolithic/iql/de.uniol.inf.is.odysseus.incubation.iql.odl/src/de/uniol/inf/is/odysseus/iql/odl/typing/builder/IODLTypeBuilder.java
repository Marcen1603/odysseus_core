package de.uniol.inf.is.odysseus.iql.odl.typing.builder;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLTypeBuilder;

public interface IODLTypeBuilder extends IIQLTypeBuilder {

	void addParameter(Class<? extends IParameter<?>> parameterType,	Class<?> parameterValueType);

}
