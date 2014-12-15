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
public class TMongoDBSinkAORule extends AbstractTNoSQLSinkAORule<MongoDBSinkAO> {
	
    @Override
    public void execute(MongoDBSinkAO mongoDBSink, TransformationConfiguration config) throws RuleException {
        MongoDBSinkPO physical = new MongoDBSinkPO(mongoDBSink);
        defaultExecute(mongoDBSink, physical, config, true, true);
    }

    @Override
    public String getName() {
        return "MongoDBSinkAO -> MongoDBSinkPO";
    }

    @Override
    public Class<? super MongoDBSinkAO> getConditionClass() {
        return MongoDBSinkAO.class;
    }
}
