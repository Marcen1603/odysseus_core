/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.datadictionary.filestore;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	private static Logger LOG = LoggerFactory.getLogger(DataDictionary.class);
	
	@Override
	protected IStore<String, ILogicalOperator> createStreamDefinitionsStore() {
		return tryCreateFileStore("streamDefinitionsFilename");
	}

	@Override
	protected IStore<String, IUser> createViewOrStreamFromUserStore() {
		return tryCreateFileStore("streamOrViewFromUserFilename");
	}

	@Override
	protected IStore<String, ILogicalOperator> createViewDefinitionsStore() {
		return tryCreateFileStore("viewDefinitionsFilename");
	}

	@Override
	protected IStore<String, SDFSchema> createEntityMapStore() {
		return tryCreateFileStore("entitiesFilename");
	}

	@Override
	protected IStore<String, IUser> createEntityFromUserStore() {
		return tryCreateFileStore("entityFromUserFilename");
	}

	@Override
	protected IStore<String, String> createSourceTypeMapStore() {
		return tryCreateFileStore("sourceTypeMapFilename");
	}

	@Override
	protected IStore<String, SDFDatatype> createDatatypesStore() {
		return tryCreateFileStore("datatypesFromDatatypesFilename");
	}

	@Override
	protected IStore<Integer, ILogicalQuery> createSavedQueriesStore() {
		return tryCreateFileStore("queriesFilename");
	}

	@Override
	protected IStore<Integer, IUser> createSavedQueriesForUserStore() {
		return tryCreateFileStore("queriesUserFilename");
	}

	@Override
	protected IStore<Integer, String> createSavedQueriesBuildParameterNameStore() {
		return tryCreateFileStore("queriesBuildParamFilename");
	}

	@Override
	protected IStore<String, ILogicalOperator> createSinkDefinitionsStore() {
		return tryCreateFileStore("sinkDefinitionsFilename");
	}

	@Override
	protected IStore<String, IUser> createSinkFromUserStore() {
		return tryCreateFileStore("sinkDefinitionsUserFilename");
	}

	private static <T extends Serializable & Comparable<? extends T>,U extends Serializable> IStore<T, U> tryCreateFileStore(String key){
		try {
			return new FileStore<T, U>(OdysseusConfiguration.get(key));
		} catch (IOException e) {
			LOG.error("Could not create fileStore-Instance for key " + key, e);
			return null;
		}
	}
}
