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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Dennis Geesen
 * Created at: 16.01.2012
 */
public class SerializeNode implements Serializable{
	
	private static final long serialVersionUID = 5890773561018183665L;
	
	private List<SerializeNode> childs = new ArrayList<SerializeNode>();	
	private Map<String, ISerializeProperty<?>> properties = new HashMap<String, ISerializeProperty<?>>();	
	private String keyname;
	private Class<?> clazz;
	

	public SerializeNode(Class<?> clazz){
		this.clazz = clazz;
		this.keyname = clazz.getCanonicalName();
	}
	
	public SerializeNode(String name){
		this.keyname = name;		
	}
	
	public SerializeNode(String name, Class<?> clazz){
		this.clazz = clazz;
		this.keyname = name;
	}
	
	public Class<?> getRepresentingClass(){
		return this.clazz;
	}
	
	public void setKeyName(String keyName){
		this.keyname = keyName;
	}

	public String getKeyName() {
		return keyname;
	}
	
	public List<SerializeNode> getChilds(){
		return this.childs;
	}
	
	
	public void addChild(SerializeNode node){
		this.childs.add(node);
	}
	
		
	public Map<String, ISerializeProperty<?>> getProperties(){
		return this.properties ;
	}
	
	public void setProperties(Map<String, ISerializeProperty<?>> properties){
		this.properties = properties;
	}
	
	public void addProperty(String name, ISerializeProperty<?> property){
		this.properties.put(name, property);
	}	
	
	public ISerializeProperty<?> getProperty(String name){
		return this.properties.get(name);
	}
	
	public Object getPropertyValue(String name){
		ISerializeProperty<?> prop = this.properties.get(name);
		if(prop!=null){
			return prop.getValue();
		}
		return null;
	}
	
	public void addPropertyValue(String name, Object value){
		SerializePropertyItem item = new SerializePropertyItem(value);
		this.properties.put(name, item);
	}
	
	@Override
	public String toString() {
		return "SerialNode ("+this.keyname+")";
		
	}
	
}
