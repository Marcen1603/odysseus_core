package de.uniol.inf.is.odysseus.iql.odl.typing;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;

public class ODLTypeBuilder extends AbstractIQLTypeBuilder<ODLTypeFactory> {

	@Inject
	public ODLTypeBuilder(ODLTypeFactory typeFactory) {
		super(typeFactory);
	}
	
	public void addParameter(Class<? extends IParameter<?>> parameterType, Class<?> parameterValueType) {
				
		JvmTypeReference pType = typeFactory.getTypeRef(parameterType);
		JvmTypeReference vType = typeFactory.getTypeRef(parameterValueType);

		typeFactory.addParameter(pType, vType);
		
	}

}
