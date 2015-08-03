package de.uniol.inf.is.odysseus.iql.odl.typing;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.AbstractIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

@Singleton
public class ODLTypeOperatorsFactory extends AbstractIQLTypeExtensionsFactory<ODLTypeFactory, ODLTypeUtils> {

	@Inject
	public ODLTypeOperatorsFactory(ODLTypeFactory typeFactory, ODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
		init();
	}
	
	private void init() {
		for (IIQLTypeExtensions operators : ODLDefaultTypes.getTypeOperators()) {
			this.addTypeExtensions(operators);
		}
	}

}
