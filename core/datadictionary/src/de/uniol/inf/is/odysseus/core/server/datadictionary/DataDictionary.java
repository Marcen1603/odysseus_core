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

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	private static Logger LOG = LoggerFactory.getLogger(DataDictionary.class);
	
	static private boolean useFilestore(){
		return OdysseusConfiguration.get("StoretypeDataDict").equalsIgnoreCase("Filestore");
	}
	
	static private boolean saveQueries(){
		return OdysseusConfiguration.getBoolean("Filestore.StoreQueries");
	}
	
	public DataDictionary(){
		super(null);
	}
	
	public DataDictionary(ITenant t){
		super(t);
	}
	
	@Override
	public IDataDictionary createInstance(ITenant t) {
		IDataDictionary dd = new DataDictionary(t);
		return dd;
	}
	
	@Override
	protected IStore<Resource, ILogicalOperator> createStreamDefinitionsStore() {
		return createStore("streamDefinitionsFilename", tenant);
	}

	@Override
	protected IStore<Resource, IUser> createViewOrStreamFromUserStore() {
		return createStore("streamOrViewFromUserFilename", tenant);
	}

	@Override
	protected IStore<Resource, ILogicalOperator> createViewDefinitionsStore() {
		return createStore("viewDefinitionsFilename", tenant);
	}

	@Override
	protected IStore<Resource, HashMap<String, ArrayList<Resource>>> createEntityUsedByStore() {
		return createStore("entityUsedByFileName", tenant);
	}

	@Override
	protected IStore<Resource, IUser> createEntityFromUserStore() {
		return createStore("entityFromUserFilename", tenant);
	}

	@Override
	protected IStore<String, SDFDatatype> createDatatypesStore() {
		return createStore("datatypesFromDatatypesFilename", tenant);
	}

	@Override
	protected IStore<Integer, ILogicalQuery> createSavedQueriesStore() {
		return createStore("queriesFilename", tenant, useFilestore()& saveQueries());
	}

	@Override
	protected IStore<Integer, IUser> createSavedQueriesForUserStore() {
		return createStore("queriesUserFilename", tenant, useFilestore()& saveQueries());
	}

	@Override
	protected IStore<Integer, String> createSavedQueriesBuildParameterNameStore() {
		return createStore("queriesBuildParamFilename", tenant, useFilestore()& saveQueries());
	}

	@Override
	protected IStore<Resource, ILogicalOperator> createSinkDefinitionsStore() {
		return createStore("sinkDefinitionsFilename", tenant);
	}

	@Override
	protected IStore<Resource, IUser> createSinkFromUserStore() {
		return createStore("sinkDefinitionsUserFilename", tenant);
	}


	@Override
	protected IStore<Resource, StoredProcedure> createStoredProceduresStore() {
		return createStore("storedProceduresFilename", tenant);
	}

	@Override
	protected IStore<Resource, IUser> createStoredProceduresFromUserStore() {
		return createStore("storedProceduresFromUserFilename", tenant);
	}
	
	private static <T extends Serializable & Comparable<? extends T>,U extends Serializable> IStore<T, U> createStore (String key, ITenant tenant){
		return createStore(key, tenant, useFilestore());
	}
	
	private static <T extends Serializable & Comparable<? extends T>,U extends Serializable> IStore<T, U> createStore (String key, ITenant tenant, boolean useFileStore){
		if (useFileStore){
			return tryCreateFileStore(key, tenant);
		}else{
			return newMemoryStore();
		}
	}
	
	
	private static <T extends Comparable<?>,U> MemoryStore<T,U> newMemoryStore(){
		return new MemoryStore<T, U>();
	}
	
	private static <T extends Serializable & Comparable<? extends T>,U extends Serializable> IStore<T, U> tryCreateFileStore(String key, ITenant tenant){
		try {
			return new FileStore<T, U>(OdysseusConfiguration.getFileProperty(key,tenant.getName()));
		} catch (IOException e) {
			LOG.error("Could not create fileStore-Instance for key " + key, e);
			return null;
		}
	}

}
