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

package de.uniol.inf.is.odysseus.context.store;

import java.util.List;

import de.uniol.inf.is.odysseus.context.IContextStoreListener;
import de.uniol.inf.is.odysseus.context.physicaloperator.StorePO;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 22.03.2012
 */
public interface IContextStore<T extends Tuple<? extends ITimeInterval>>{

	public String getName();
	public SDFSchema getSchema();
	public void insertValue(T value);	
	public List<T> getValues(ITimeInterval timeinterval);
	public List<T> getLastValues();	
	public List<T> getAllValues();	
	public void processTime(PointInTime time);
	
	public void addListener(IContextStoreListener listener);	
	public void removeListener(IContextStoreListener listener);	
	
	public void setWriter(StorePO<T> storePO);
	public boolean hasWriter();
	public void removeWriter();
	public StorePO<T> getWriter();
	public void close();
	public void open();
	public int getSize();	
	
	
}
