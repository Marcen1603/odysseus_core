package de.uniol.inf.is.odysseus.iql.qdl.typing;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.AbstractIQLTypeOperatorsFactory;

@Singleton
public class QDLTypeOperatorsFactory extends AbstractIQLTypeOperatorsFactory<QDLTypeFactory> {

	@Inject
	public QDLTypeOperatorsFactory(QDLTypeFactory typeFactory) {
		super(typeFactory);
	}

}
