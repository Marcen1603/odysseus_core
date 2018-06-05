package de.uniol.inf.is.odysseus.server.mongodb.transform;

import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSinkAO;
import de.uniol.inf.is.odysseus.server.mongodb.physicaloperator.MongoDBSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSinkAORule;

public class TMongoDBSinkAORule extends AbstractTNoSQLSinkAORule<MongoDBSinkAO> {

    @Override
    protected Class<? extends AbstractNoSQLSinkAO> getLogicalOperatorClass() {
        return MongoDBSinkAO.class;
    }

    @SuppressWarnings("unchecked")
	@Override
    protected Class<? extends AbstractNoSQLSinkPO<?>> getPhysicalOperatorClass() {
        return (Class<? extends AbstractNoSQLSinkPO<?>>) MongoDBSinkPO.class;
    }
}
