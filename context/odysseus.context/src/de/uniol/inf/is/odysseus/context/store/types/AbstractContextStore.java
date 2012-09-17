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

package de.uniol.inf.is.odysseus.context.store.types;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.context.IContextStoreListener;
import de.uniol.inf.is.odysseus.context.physicaloperator.StorePO;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 22.03.2012
 */
public abstract class AbstractContextStore<T extends Tuple<? extends ITimeInterval>> implements IContextStore<T> {

	protected Logger logger = LoggerFactory.getLogger(AbstractContextStore.class);
	private SDFSchema schema;
	private String name;
	private List<IContextStoreListener> listeners = new ArrayList<IContextStoreListener>();
	private StorePO<T> writer;
	private int size;
	
	public AbstractContextStore(String name, SDFSchema schema, int size){
		this.schema = schema;
		this.name = name;
		this.size = size;
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getSize(){
		return this.size;
	}
	
	protected boolean validateSchemaSizeOfValue(T value){
		if(value.size()==schema.size()){
			return true;
		}
		return false;
	}
	
	public void addListener(IContextStoreListener listener){
		this.listeners.add(listener);
	}
	
	public void removeListener(IContextStoreListener listener){
		this.listeners.remove(listener);
	}
	
	public void notifyListener(){
		for(IContextStoreListener l : this.listeners){
			l.contextStoreChanged(this);
		}
	}
	
	public void setWriter(StorePO<T> storePO){
		this.writer = storePO;
		logger.debug("set "+this.writer+" as writer for context store "+this);
	}
	
	public boolean hasWriter(){
		return this.writer!=null;
	}
	public void removeWriter(){
		logger.debug("remove "+this.writer+" as writer for context store "+this);
		this.writer=null;		
	}
	public StorePO<T> getWriter(){
		return this.writer;
	}
	
	public void open(){
		notifyListener();
	}
	
	public void close(){
		internalClear();
	}

	abstract protected void internalClear();
	
	
}
