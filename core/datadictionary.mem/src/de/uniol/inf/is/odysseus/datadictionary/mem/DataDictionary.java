package de.uniol.inf.is.odysseus.datadictionary.mem;

import java.io.IOException;

import de.uniol.inf.is.odysseus.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	public DataDictionary() throws IOException {
		super();
		streamDefinitions = new MemoryStore<String, ILogicalOperator>();
		viewOrStreamFromUser = new MemoryStore<String, IUser>();
		viewDefinitions = new MemoryStore<String, ILogicalOperator>();
		entityMap = new MemoryStore<String, SDFEntity>();
		sourceTypeMap = new MemoryStore<String, String>();
		entityFromUser = new MemoryStore<String, IUser>();
		datatypes = new MemoryStore<String, SDFDatatype>();
		sinkDefinitions = new MemoryStore<String, ILogicalOperator>();
		sinkFromUser = new MemoryStore<String, IUser>();
		savedQueries = new MemoryStore<Integer, IQuery>();
		savedQueriesForUser = new MemoryStore<Integer, IUser>();
		initDatatypes();
	}

}
