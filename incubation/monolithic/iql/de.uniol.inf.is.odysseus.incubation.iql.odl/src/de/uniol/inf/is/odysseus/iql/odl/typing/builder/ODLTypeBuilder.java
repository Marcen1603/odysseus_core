package de.uniol.inf.is.odysseus.iql.odl.typing.builder;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.odl.typing.factory.IODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLTypeBuilder extends AbstractIQLTypeBuilder<IODLTypeFactory, IODLTypeUtils> implements IODLTypeBuilder {

	@Inject
	public ODLTypeBuilder(IODLTypeFactory typeFactory, IODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}
	
	@Override
	public void addParameter(Class<? extends IParameter<?>> parameterType, Class<?> parameterValueType) {	
		JvmTypeReference pType = typeUtils.createTypeRef(parameterType, typeFactory.getSystemResourceSet());
		JvmTypeReference vType = typeUtils.createTypeRef(parameterValueType, typeFactory.getSystemResourceSet());
		typeFactory.addParameter(pType, vType);
		
	}

}
