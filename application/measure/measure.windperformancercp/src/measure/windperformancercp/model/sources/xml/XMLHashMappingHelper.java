/** Copyright [2011] [The Odysseus Team]
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
package measure.windperformancercp.model.sources.xml;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import measure.windperformancercp.model.sources.ISource;

public class XMLHashMappingHelper extends XmlAdapter<MyHashMapType, HashMap<String, ISource>> {
	
	@Override
	public MyHashMapType marshal(HashMap<String,ISource> hmap) throws Exception {
		MyHashMapType sourceMap = new MyHashMapType();
		ArrayList<MyHashMapEntryType> list = new ArrayList<MyHashMapEntryType>();
		
		for(String key: hmap.keySet()){
			MyHashMapEntryType entry = new MyHashMapEntryType();
			entry.setKey(key);
			entry.setValue(hmap.get(key));
			list.add(entry);
		}
		sourceMap.setList(list);
		return sourceMap;
	}

	@Override
	public HashMap<String,ISource> unmarshal(MyHashMapType unmarshall) throws Exception {
		HashMap<String, ISource> hmap = new HashMap<String,ISource>();
		
		for(MyHashMapEntryType entry : unmarshall.getList()){
			hmap.put(entry.getKey(), entry.getValue());
		}
		return hmap;
	}
	
	protected XMLHashMappingHelper(){
	}

}
