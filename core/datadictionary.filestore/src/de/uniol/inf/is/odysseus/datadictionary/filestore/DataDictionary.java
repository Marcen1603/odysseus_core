package de.uniol.inf.is.odysseus.datadictionary.filestore;

import java.io.IOException;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.store.FileStore;
import de.uniol.inf.is.odysseus.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	public DataDictionary() throws IOException {
		super();
		streamDefinitions = new FileStore<String, ILogicalOperator>(
				OdysseusDefaults.get("streamDefinitionsFilename"));
		viewOrStreamFromUser = new FileStore<String, IUser>(
				OdysseusDefaults.get("streamOrViewFromUserFilename"));
		viewDefinitions = new FileStore<String, ILogicalOperator>(
				OdysseusDefaults.get("viewDefinitionsFilename"));
		entityMap = new FileStore<String, SDFAttributeList>(
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
		savedQueries = new FileStore<Integer, IQuery>(
				OdysseusDefaults.get("queriesFilename"));
		savedQueriesForUser = new FileStore<Integer, IUser>(
				OdysseusDefaults.get("queriesUserFilename"));

		initDatatypes();
	}

}
