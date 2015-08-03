package de.uniol.inf.is.odysseus.iql.odl.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLQualifiedNameProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;


public class ODLQualifiedNameProvider extends AbstractIQLQualifiedNameProvider<ODLTypeUtils>{
	@Inject
	public ODLQualifiedNameProvider(ODLTypeUtils typeUtils) {
		super(typeUtils);
	}



}
