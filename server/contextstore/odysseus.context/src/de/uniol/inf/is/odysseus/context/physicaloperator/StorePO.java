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

package de.uniol.inf.is.odysseus.context.physicaloperator;

import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.04.2012
 */
public class StorePO<T extends Tuple<? extends ITimeInterval>> extends AbstractSink<T>{

	
	private IContextStore<T> store;	

	public StorePO(StorePO<T> storePO){
		super();
		this.store = storePO.store;
	}
	
	
	public StorePO(IContextStore<T> store) {
		super();
		this.store = store;
	}	
	
	@Override
	protected void process_next(T object, int port) {
		this.store.insertValue(object);
		this.store.processTime(object.getMetadata().getStart());
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.store.processTime(punctuation.getTime());
	}

	@Override
	public AbstractSink<T> clone() {
		return new StorePO<T>(this);
	}
	
	@Override
	protected void process_close() {	
		super.process_close();
		if((this.store.getWriter()!=null)&&(this.store.getWriter().equals(this))){
			this.store.removeWriter();
		}
		// wirklich machen, weil eigentlich nur zum debuggen sinnvoll, wenn die zeit wieder von vorne beginnt...
		this.store.close();
	}

}
