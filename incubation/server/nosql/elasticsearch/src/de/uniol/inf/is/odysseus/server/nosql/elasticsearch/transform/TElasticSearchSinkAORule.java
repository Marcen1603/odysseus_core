package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.transform;

import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSinkAORule;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.logicaloperator.ElasticSearchSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.physicaloperator.ElasticSearchSinkPO;

/**
 * Erstellt von RoBeaT
 * Date: 16.12.2014
 */
@SuppressWarnings("UnusedDeclaration")
public class TElasticSearchSinkAORule extends AbstractTNoSQLSinkAORule<ElasticSearchSinkAO> {

    @Override
    protected Class getLogicalOperatorClass() {
        return ElasticSearchSinkAO.class;
    }

    @Override
    protected Class getPhysicalOperatorClass() {
        return ElasticSearchSinkPO.class;
    }
}
