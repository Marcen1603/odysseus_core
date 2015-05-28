package de.uniol.inf.is.odysseus.server.nosql.base.transform;

import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLAO;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSinkPO;

/**
 *  AbstractTNoSQLSinkAORule ist the superclass for all transformation rules for sinks in the NoSQL context
 *
 * @param <A> the concrete logical operator class
 */
public abstract class AbstractTNoSQLSinkAORule<A extends AbstractNoSQLAO> extends AbstractTNoSQLAORule<A, AbstractNoSQLSinkPO<?>> {

    @Override
    protected abstract Class<? extends AbstractNoSQLSinkAO> getLogicalOperatorClass();

    @Override
    protected abstract Class<? extends AbstractNoSQLSinkPO<?>> getPhysicalOperatorClass();
}
