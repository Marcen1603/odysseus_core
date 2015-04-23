package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;

public interface IPhysicalNoSQLOperator {

    Class<? extends NoSQLConnectionWrapper<?>> getNoSQLConnectionWrapperClass();

    void setupConnection(Object connection);
}
