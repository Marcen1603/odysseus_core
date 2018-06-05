package de.uniol.inf.is.odysseus.server.nosql.base.transform;

import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLAO;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSourceAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSourcePO;

/**
 *  AbstractTNoSQLSourceAORule ist the superclass for all transformation rules for sources in the NoSQL context
 *
 * @param <A> the concrete logical operator class
 */
public abstract class AbstractTNoSQLSourceAORule<A extends AbstractNoSQLAO> extends AbstractTNoSQLAORule<A, AbstractNoSQLSourcePO<?,?>> {

    @Override
    protected abstract Class<? extends AbstractNoSQLSourceAO> getLogicalOperatorClass();

    @Override
    protected abstract Class<? extends AbstractNoSQLSourcePO<?,?>> getPhysicalOperatorClass();

}

