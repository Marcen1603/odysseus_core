/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.logicaloperator.serialize;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author Dennis Geesen
 * Created at: 17.01.2012
 */
public class SerializePropertyList implements ISerializeProperty<Collection<SerializePropertyItem>> {

	private Collection<SerializePropertyItem> values = new ArrayList<SerializePropertyItem>();
	private Class<?> type;
	
	public SerializePropertyList(){
		type = ArrayList.class;
	}
	
	public SerializePropertyList(Collection<SerializePropertyItem> values){
		this.values = values;
		this.type = values.getClass();
	}
	
	public Class<?> getType(){
		return this.type;
	}
	
	public void addItemValue(Object value){
		this.values.add(new SerializePropertyItem(value));
	}
	
	public void addItem(SerializePropertyItem value){
		this.values.add(value);
	}
	
	@Override
	public boolean isList() {	
		return true;
	}

	@Override
	public Collection<SerializePropertyItem> getValue() {
		return this.values;
	}

}
