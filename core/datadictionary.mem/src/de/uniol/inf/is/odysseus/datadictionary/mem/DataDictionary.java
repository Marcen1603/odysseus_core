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
package de.uniol.inf.is.odysseus.datadictionary.mem;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	@Override
	protected IStore<String, ILogicalOperator> createStreamDefinitionsStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, IUser> createViewOrStreamFromUserStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, ILogicalOperator> createViewDefinitionsStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, SDFSchema> createEntityMapStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, IUser> createEntityFromUserStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, SDFDatatype> createDatatypesStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<Integer, ILogicalQuery> createSavedQueriesStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<Integer, IUser> createSavedQueriesForUserStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<Integer, String> createSavedQueriesBuildParameterNameStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, ILogicalOperator> createSinkDefinitionsStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, IUser> createSinkFromUserStore() {
		return newMemoryStore();
	}
	
	private static <T extends Comparable<?>,U> MemoryStore<T,U> newMemoryStore(){
		return new MemoryStore<T, U>();
	}
}
