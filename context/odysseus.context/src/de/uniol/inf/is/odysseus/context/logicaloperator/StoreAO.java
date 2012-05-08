/** Copyright 2012 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package de.uniol.inf.is.odysseus.context.logicaloperator;

import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.04.2012
 */

@LogicalOperator(name = "STORE", minInputPorts = 1, maxInputPorts = 1)
public class StoreAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = -8367889240385241169L;
	
	private String storeName;
	
	public StoreAO(){
		super();
	}
	
	public StoreAO(String storeName){
		super();
		this.storeName = storeName;
	}

	public StoreAO(StoreAO storeAO) {
		super();
		this.storeName = storeAO.storeName;		
	}
	
	
	public String getStoreName() {
		return storeName;
	}

	@Parameter(name = "storeName", type = StringParameter.class)
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new StoreAO(this);
	}

	
	@Override
	public void initialize() {	
		super.initialize();
		IContextStore<Tuple<ITimeInterval>> store = ContextStoreManager.getStore(storeName);
		if(!this.getOutputSchema().compatibleTo(store.getSchema())){
			throw new IllegalArgumentException("Schemas of the store-operator and the store itself are not compatible. They have to be at least union-compatible");
		}
		if(store.hasWriter()){
			throw new IllegalArgumentException("There is already a store-operator for \""+storeName+"\". Use the existing one instead, because otherwise there all writes into the store will not be chronologically ordered");
		}
	}
}
