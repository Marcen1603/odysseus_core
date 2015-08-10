package de.uniol.inf.is.odysseus.iql.qdl.typing;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.AbstractIQLTypeExtensionsFactory;

@Singleton
public class QDLTypeExtensionsFactory extends AbstractIQLTypeExtensionsFactory<QDLTypeFactory, QDLTypeUtils> {

	@Inject
	public QDLTypeExtensionsFactory(QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

}
