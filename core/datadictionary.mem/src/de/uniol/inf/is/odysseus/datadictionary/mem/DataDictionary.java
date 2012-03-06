package de.uniol.inf.is.odysseus.datadictionary.mem;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	public DataDictionary() throws IOException {
		super();
		streamDefinitions = new MemoryStore<String, ILogicalOperator>();
		viewOrStreamFromUser = new MemoryStore<String, IUser>();
		viewDefinitions = new MemoryStore<String, ILogicalOperator>();
		entityMap = new MemoryStore<String, SDFSchema>();
		sourceTypeMap = new MemoryStore<String, String>();
		entityFromUser = new MemoryStore<String, IUser>();
		datatypes = new MemoryStore<String, SDFDatatype>();
		sinkDefinitions = new MemoryStore<String, ILogicalOperator>();
		sinkFromUser = new MemoryStore<String, IUser>();
		savedQueries = new MemoryStore<Integer, ILogicalQuery>();
		savedQueriesForUser = new MemoryStore<Integer, IUser>();
		savedQueriesBuildParameterName = new MemoryStore<Integer, String>();
		initDatatypes();
	}

}
