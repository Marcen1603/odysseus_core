package de.uniol.inf.is.odysseus.server.hadoop.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.server.hadoop.logicaloperator.HadoopSinkAO;
import de.uniol.inf.is.odysseus.server.hadoop.physicaloperator.HadoopSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.transform.AbstractTNoSQLSinkAORule;

@SuppressWarnings("UnusedDeclaration")
public class THadoopSinkAORule extends AbstractTNoSQLSinkAORule<HadoopSinkAO> {

    @Override
    protected Class<? extends AbstractNoSQLSinkAO> getLogicalOperatorClass() {
        return HadoopSinkAO.class;
    }

    @Override
    protected Class<? extends AbstractNoSQLSinkPO> getPhysicalOperatorClass() {
        return HadoopSinkPO.class;
    }

    @Override
    public boolean isExecutable(HadoopSinkAO operator, TransformationConfiguration config) {
        return operator.getUser() != null;
    }
}
