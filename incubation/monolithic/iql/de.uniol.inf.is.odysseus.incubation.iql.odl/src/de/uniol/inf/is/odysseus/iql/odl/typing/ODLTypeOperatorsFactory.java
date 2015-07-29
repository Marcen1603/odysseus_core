package de.uniol.inf.is.odysseus.iql.odl.typing;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.AbstractIQLTypeOperatorsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.ITypeOperators;

@Singleton
public class ODLTypeOperatorsFactory extends AbstractIQLTypeOperatorsFactory<ODLTypeFactory> {

	@Inject
	public ODLTypeOperatorsFactory(ODLTypeFactory typeFactory) {
		super(typeFactory);
		init();
	}
	
	private void init() {
		for (ITypeOperators operators : ODLDefaultTypes.getTypeOperators()) {
			this.addTypeOperators(operators);
		}
	}

}
