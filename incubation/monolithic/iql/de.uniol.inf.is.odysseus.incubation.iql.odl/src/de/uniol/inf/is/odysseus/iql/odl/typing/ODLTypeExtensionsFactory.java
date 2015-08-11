package de.uniol.inf.is.odysseus.iql.odl.typing;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.AbstractIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;

@Singleton
public class ODLTypeExtensionsFactory extends AbstractIQLTypeExtensionsFactory<ODLTypeFactory, ODLTypeUtils> {

	@Inject
	public ODLTypeExtensionsFactory(ODLTypeFactory typeFactory, ODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
		init();
	}
	
	private void init() {
		for (IIQLTypeExtensions operators : ODLDefaultTypes.getTypeOperators()) {
			this.addTypeExtensions(operators);
		}
	}
	
	@Override
	protected JvmTypeReference getExtendedType(JvmDeclaredType declaredType) {
		if (declaredType instanceof ODLOperator) {
			return typeUtils.createTypeRef(AbstractPipe.class, typeFactory.getSystemResourceSet());
		} else {
			return declaredType.getExtendedClass();
		}
	}

}
