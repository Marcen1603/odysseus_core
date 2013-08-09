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
package de.uniol.inf.is.odysseus.core.server.datadictionary;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	private static Logger LOG = LoggerFactory.getLogger(DataDictionary.class);
	
	static private boolean useFilestore(){
		return OdysseusConfiguration.get("StoretypeDataDict").equalsIgnoreCase("Filestore");
	}
	
	@Override
	protected IStore<String, ILogicalOperator> createStreamDefinitionsStore() {
		return createStore("streamDefinitionsFilename");
	}

	@Override
	protected IStore<String, IUser> createViewOrStreamFromUserStore() {
		return createStore("streamOrViewFromUserFilename");
	}

	@Override
	protected IStore<String, ILogicalOperator> createViewDefinitionsStore() {
		return createStore("viewDefinitionsFilename");
	}

	@Override
	protected IStore<String, HashMap<String, ArrayList<String>>> createEntityUsedByStore() {
		return createStore("entityUsedByFileName");
	}

	@Override
	protected IStore<String, IUser> createEntityFromUserStore() {
		return createStore("entityFromUserFilename");
	}

	@Override
	protected IStore<String, SDFDatatype> createDatatypesStore() {
		return createStore("datatypesFromDatatypesFilename");
	}

	@Override
	protected IStore<Integer, ILogicalQuery> createSavedQueriesStore() {
		return createStore("queriesFilename");
	}

	@Override
	protected IStore<Integer, IUser> createSavedQueriesForUserStore() {
		return createStore("queriesUserFilename");
	}

	@Override
	protected IStore<Integer, String> createSavedQueriesBuildParameterNameStore() {
		return createStore("queriesBuildParamFilename");
	}

	@Override
	protected IStore<String, ILogicalOperator> createSinkDefinitionsStore() {
		return createStore("sinkDefinitionsFilename");
	}

	@Override
	protected IStore<String, IUser> createSinkFromUserStore() {
		return createStore("sinkDefinitionsUserFilename");
	}


	@Override
	protected IStore<String, StoredProcedure> createStoredProceduresStore() {
		return createStore("storedProceduresFilename");
	}

	@Override
	protected IStore<String, IUser> createStoredProceduresFromUserStore() {
		return createStore("storedProceduresFromUserFilename");
	}
	
	private static <T extends Serializable & Comparable<? extends T>,U extends Serializable> IStore<T, U> createStore (String key){
		if (useFilestore()){
			return tryCreateFileStore(key);
		}else{
			return newMemoryStore();
		}
	}
	
	private static <T extends Comparable<?>,U> MemoryStore<T,U> newMemoryStore(){
		return new MemoryStore<T, U>();
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
