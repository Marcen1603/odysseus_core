package de.uniol.inf.is.odysseus.iql.odl.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLQualifiedNameProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;


public class ODLQualifiedNameProvider extends AbstractIQLQualifiedNameProvider<IODLTypeUtils>{
	@Inject
	public ODLQualifiedNameProvider(IODLTypeUtils typeUtils) {
		super(typeUtils);
	}



}
