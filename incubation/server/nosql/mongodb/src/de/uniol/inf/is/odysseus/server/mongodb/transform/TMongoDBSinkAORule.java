package de.uniol.inf.is.odysseus.server.mongodb.transform;

import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSinkAO;
import de.uniol.inf.is.odysseus.server.mongodb.physicaloperator.MongoDBSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSinkAORule;

/**
 * Erstellt von RoBeaT
 * Date: 01.12.14
 */
@SuppressWarnings("UnusedDeclaration")
public class TMongoDBSinkAORule extends AbstractTNoSQLSinkAORule<MongoDBSinkAO> {

    @Override
    protected Class getLogicalOperatorClass() {
        return MongoDBSinkAO.class;
    }

    @Override
    protected Class getPhysicalOperatorClass() {
        return MongoDBSinkPO.class;
    }
}
