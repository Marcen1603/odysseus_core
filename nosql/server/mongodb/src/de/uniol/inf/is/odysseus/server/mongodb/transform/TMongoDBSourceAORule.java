package de.uniol.inf.is.odysseus.server.mongodb.transform;

import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSourceAO;
import de.uniol.inf.is.odysseus.server.mongodb.physicaloperator.MongoDBSourcePO;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSourceAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSourcePO;
import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSourceAORule;

public class TMongoDBSourceAORule extends AbstractTNoSQLSourceAORule<MongoDBSourceAO> {

    @Override
    protected Class<? extends AbstractNoSQLSourceAO> getLogicalOperatorClass() {
        return MongoDBSourceAO.class;
    }

    @SuppressWarnings("unchecked")
	@Override
    protected Class<? extends AbstractNoSQLSourcePO<?,?>> getPhysicalOperatorClass() {
        return (Class<? extends AbstractNoSQLSourcePO<?, ?>>) MongoDBSourcePO.class;
    }
}