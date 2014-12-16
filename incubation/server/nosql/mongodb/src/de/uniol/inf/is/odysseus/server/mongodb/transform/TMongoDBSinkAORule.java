package de.uniol.inf.is.odysseus.server.mongodb.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSinkAO;
import de.uniol.inf.is.odysseus.server.mongodb.physicaloperator.MongoDBSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSinkAORule;

/**
 * Erstellt von RoBeaT
 * Date: 01.12.14
 */
@SuppressWarnings("UnusedDeclaration")
public class TMongoDBSinkAORule extends AbstractTNoSQLSinkAORule<MongoDBSinkAO> {

    public TMongoDBSinkAORule() {
        super();
        logicalOperatorClass  = MongoDBSinkAO.class;
        physicalOperatorClass = MongoDBSinkPO.class;
    }
}
