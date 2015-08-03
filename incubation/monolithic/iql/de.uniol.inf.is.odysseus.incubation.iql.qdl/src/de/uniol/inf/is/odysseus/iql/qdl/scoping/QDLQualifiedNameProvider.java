package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLQualifiedNameProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;


public class QDLQualifiedNameProvider extends AbstractIQLQualifiedNameProvider<QDLTypeUtils>{

	@Inject
	public QDLQualifiedNameProvider(QDLTypeUtils typeUtils) {
		super(typeUtils);
	}



}
