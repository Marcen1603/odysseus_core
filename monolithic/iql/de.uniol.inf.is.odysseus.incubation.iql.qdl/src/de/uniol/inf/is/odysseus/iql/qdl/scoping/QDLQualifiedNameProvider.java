package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLQualifiedNameProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;


public class QDLQualifiedNameProvider extends AbstractIQLQualifiedNameProvider<IQDLTypeUtils>{

	@Inject
	public QDLQualifiedNameProvider(IQDLTypeUtils typeUtils) {
		super(typeUtils);
	}



}
