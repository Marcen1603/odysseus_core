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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;


public class FusionContextStore<T> {
	
	private static HashMap<Integer, Tuple<? extends IFusionProbability>> contextStore = null;
	private static SDFSchema storeSchema;
	
	public static void insertNew(int id,Tuple<? extends IFusionProbability> tuple){
		System.out.println(id);
		contextStore.put(id, tuple);
	}
	
	public static void update(int id ,Tuple<? extends IFusionProbability> tuple){
		System.out.println(id);
		contextStore.put(id,  tuple);
	}
	
	public static void setStoreSchema(final SDFSchema schema) {
		storeSchema = schema;
    }
	
	public static SDFSchema getStoreSchema(){
		return storeSchema;
	}

	public static HashMap<Integer, Tuple<? extends IFusionProbability>> getStoreMap(){
		if(contextStore == null){
			contextStore = new HashMap<Integer, Tuple<? extends IFusionProbability>>();
		}
		return contextStore;
	}
	
	public static int getNextStoreId() {
	    return contextStore.size();
    }
	
}
