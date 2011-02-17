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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class ViewableDatatypeRegistry {

	private static ViewableDatatypeRegistry instance = null;
	
	private Map<SDFDatatype, IViewableDatatype<?>> types = new HashMap<SDFDatatype, IViewableDatatype<?>>();
	
	private ViewableDatatypeRegistry(){
		
	}
	
	public static synchronized ViewableDatatypeRegistry getInstance(){
		if(instance==null){
			instance = new ViewableDatatypeRegistry();
		}
		return instance;
	}
	
	
	public void register(IViewableDatatype<?>  datatype){
		for(SDFDatatype type : datatype.getProvidedSDFDatatypes()){
			if(this.types.containsKey(type)){
				System.out.println("WARN: double entry for "+type+" in ViewableDatatypeRegistry! Value overwritten!");
			}
			this.types.put(type, datatype);
		}
	}
	
	public void unregister(IViewableDatatype<?> datatype){
		this.types.remove(datatype);
	}
	
	public Collection<IViewableDatatype<?>> getRegisteredTypes(){
		return this.types.values();
	}

	public IViewableDatatype<?> getConverter(SDFDatatype sdfDatatype) {
		return this.types.get(sdfDatatype);		
	}
	
	
	public boolean isAllowedDataType(SDFDatatype datatype) {
		return this.types.containsKey(datatype);				
	} 
}
