package de.uniol.inf.is.odysseus.iql.basic.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;


public class BasicIQLQualifiedNameProvider extends AbstractIQLQualifiedNameProvider<BasicIQLTypeUtils>{

	@Inject
	public BasicIQLQualifiedNameProvider(BasicIQLTypeUtils typeUtils) {
		super(typeUtils);
	}



}
