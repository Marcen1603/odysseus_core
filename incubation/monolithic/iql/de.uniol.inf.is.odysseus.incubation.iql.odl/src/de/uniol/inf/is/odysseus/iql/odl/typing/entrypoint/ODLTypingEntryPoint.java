package de.uniol.inf.is.odysseus.iql.odl.typing.entrypoint;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.typing.ParameterFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.AbstractIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.odl.typing.builder.IODLTypeBuilder;

@Singleton
public class ODLTypingEntryPoint extends AbstractIQLTypingEntryPoint<IODLTypeBuilder> {

	@Inject
	public ODLTypingEntryPoint(IODLTypeBuilder builder) {
		super(builder);
		this.initParameters();
	}
		
	
	private void initParameters() {
		for (Class<? extends IParameter<?>> element : ParameterFactory.getParameters()) {
			Class<?> value = ParameterFactory.getParameterValue(element);
			builder.addParameter(element, value);
		}		
	}


}
