package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSinkAORule;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.logicaloperator.ElasticSearchSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.physicaloperator.ElasticSearchSinkPO;

/**
 * Erstellt von RoBeaT
 * Date: 16.12.2014
 */
public class TElasticSearchSinkAORule extends AbstractTNoSQLSinkAORule<ElasticSearchSinkAO> {

    @Override
    public void execute(ElasticSearchSinkAO logical, TransformationConfiguration config) throws RuleException {
        ElasticSearchSinkPO physical = new ElasticSearchSinkPO(logical);
        defaultExecute(logical, physical, config, true, true);
    }

    @Override
    public String getName() {
        return "ElasticSearchSinkAO -> ElasticSearchSinkPO";
    }

    @Override
    public Class<? super ElasticSearchSinkAO> getConditionClass() {
        return ElasticSearchSinkAO.class;
    }
}
