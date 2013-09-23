package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;

public class AccessAOPQLStatementGenerator extends AbstractAccessAOPQLStatementGenerator<AccessAO> {

	@Override
	public Class<AccessAO> getOperatorClass() {
		return AccessAO.class;
	}
}
