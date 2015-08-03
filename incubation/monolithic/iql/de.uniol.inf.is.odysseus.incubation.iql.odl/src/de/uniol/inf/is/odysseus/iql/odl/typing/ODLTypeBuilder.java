package de.uniol.inf.is.odysseus.iql.odl.typing;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;

public class ODLTypeBuilder extends AbstractIQLTypeBuilder<ODLTypeFactory, ODLTypeUtils> {

	@Inject
	public ODLTypeBuilder(ODLTypeFactory typeFactory, ODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}
	
	public void addParameter(Class<? extends IParameter<?>> parameterType, Class<?> parameterValueType) {
				
		JvmTypeReference pType = typeUtils.createTypeRef(parameterType, typeFactory.getSystemResourceSet());
		JvmTypeReference vType = typeUtils.createTypeRef(parameterValueType, typeFactory.getSystemResourceSet());

		typeFactory.addParameter(pType, vType);
		
	}

}
