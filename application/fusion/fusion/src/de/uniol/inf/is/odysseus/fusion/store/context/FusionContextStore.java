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
package de.uniol.inf.is.odysseus.fusion.store.context;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class FusionContextStore<T> {
	
	private HashMap<Integer, Tuple<? extends IMetaAttribute>> contextStore;
	private SDFSchema storeSchema;
	
	public FusionContextStore(SDFSchema schema) {
		storeSchema = schema;
		contextStore = new HashMap<Integer, Tuple<? extends IMetaAttribute>>();
	}
	
	public void insertValue(Tuple<? extends IMetaAttribute> tuple){
		contextStore.put((Integer)tuple.getAttribute(0), tuple);
	}
	
	public SDFSchema getStoreSchema(){
		return storeSchema;
	}

}
