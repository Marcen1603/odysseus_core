package de.uniol.inf.is.odysseus.server.mongodb.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSinkAO;
import de.uniol.inf.is.odysseus.server.mongodb.physicaloperator.MongoDBSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Erstellt von RoBeaT
 * Date: 01.12.14
 */
public class TMongoDBSinkAORule extends AbstractTransformationRule<MongoDBSinkAO> {
	
    @Override
    public void execute(MongoDBSinkAO mongoDBSink, TransformationConfiguration config) throws RuleException {
        MongoDBSinkPO physical = new MongoDBSinkPO(mongoDBSink.getMongoDBName(), mongoDBSink.getCollectionName());
        defaultExecute(mongoDBSink, physical, config, true, true);
    }

    @Override
    public boolean isExecutable(MongoDBSinkAO operator, TransformationConfiguration config) {
//        return operator.isAllPhysicalInputSet();
        return true;
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
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
