package de.uniol.inf.is.odysseus.datadictionary.filestore;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusDefaults;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	public DataDictionary() throws IOException {
		super();
		streamDefinitions = new FileStore<String, ILogicalOperator>(
				OdysseusDefaults.get("streamDefinitionsFilename"));
		viewOrStreamFromUser = new FileStore<String, IUser>(
				OdysseusDefaults.get("streamOrViewFromUserFilename"));
		viewDefinitions = new FileStore<String, ILogicalOperator>(
				OdysseusDefaults.get("viewDefinitionsFilename"));
		entityMap = new FileStore<String, SDFSchema>(
				OdysseusDefaults.get("entitiesFilename"));
		sourceTypeMap = new FileStore<String, String>(
				OdysseusDefaults.get("sourceTypeMapFilename"));
		entityFromUser = new FileStore<String, IUser>(
				OdysseusDefaults.get("entityFromUserFilename"));
		datatypes = new FileStore<String, SDFDatatype>(
				OdysseusDefaults.get("datatypesFromDatatypesFilename"));
		sinkDefinitions = new FileStore<String, ILogicalOperator>(
				OdysseusDefaults.get("sinkDefinitionsFilename"));
		sinkFromUser = new FileStore<String, IUser>(
				OdysseusDefaults.get("sinkDefinitionsUserFilename"));
		savedQueries = new FileStore<Integer, ILogicalQuery>(
				OdysseusDefaults.get("queriesFilename"));
		savedQueriesForUser = new FileStore<Integer, IUser>(
				OdysseusDefaults.get("queriesUserFilename"));
		savedQueriesBuildParameterName = new FileStore<Integer, String>(
				OdysseusDefaults.get("queriesBuildParamFilename"));
		
		initDatatypes();
	}

}
