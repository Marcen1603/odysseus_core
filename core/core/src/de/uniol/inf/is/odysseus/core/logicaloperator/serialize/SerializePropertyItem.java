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

package de.uniol.inf.is.odysseus.core.logicaloperator.serialize;


/**
 * 
 * @author Dennis Geesen
 * Created at: 17.01.2012
 */
public class SerializePropertyItem implements ISerializeProperty<Object>{
	
	private Object value;
	private Class<?> type = Object.class;	

	public SerializePropertyItem(Object value){
		this.value = value;
		if(value!=null){
			this.type = value.getClass();
		}
	}
	
	public SerializePropertyItem(Object value, Class<?> type){
		this.value = value;
		this.type = type;		
	}
	
	
	public Class<?> getType(){
		return type;
	}
	
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isList() {
		return false;
	}

}
