package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.transform;

import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSinkAORule;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.logicaloperator.ElasticSearchSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.physicaloperator.ElasticSearchSinkPO;


public class TElasticSearchSinkAORule extends AbstractTNoSQLSinkAORule<ElasticSearchSinkAO> {

    @Override
    protected Class<? extends AbstractNoSQLSinkAO> getLogicalOperatorClass() {
        return ElasticSearchSinkAO.class;
    }

    @SuppressWarnings("unchecked")
	@Override
    protected Class<? extends AbstractNoSQLSinkPO<?>> getPhysicalOperatorClass() {
        return (Class<? extends AbstractNoSQLSinkPO<?>>) ElasticSearchSinkPO.class;
    }
}
