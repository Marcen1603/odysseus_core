package de.uniol.inf.is.odysseus.iql.odl.typing.typeextension;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLService;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.AbstractIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.service.ODLServiceBinding;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

@Singleton
public class ODLTypeExtensionsDictionary extends AbstractIQLTypeExtensionsDictionary<IODLTypeDictionary, IODLTypeUtils> implements IODLTypeExtensionsDictionary {

	@Inject
	public ODLTypeExtensionsDictionary(IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
		init();
	}
	
	private void init() {
		for (IIQLTypeExtensions operators : ODLDefaultTypes.getTypeOperators()) {
			this.addTypeExtensions(operators);
		}
		ODLServiceBinding.getInstance().addListener(this);
		for (IIQLService service : ODLServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}
	
	@Override
	protected JvmTypeReference getExtendedType(JvmDeclaredType declaredType) {
		if (declaredType instanceof ODLOperator) {
			return typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet());
		} else {
			return declaredType.getExtendedClass();
		}
	}

}
