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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public abstract class AbstractViewableDatatype<T> implements IViewableDatatype<T> {

	private List<SDFDatatype> datatypes = new ArrayList<SDFDatatype>();
	private List<Class<?>> classes = new ArrayList<Class<?>>();
	
	
	protected void addProvidedSDFDatatype(SDFDatatype datatype){
		this.datatypes.add(datatype);
	}
	
	protected void addProvidedClass(Class<?> classe){
		this.classes.add(classe);
	}
	
	
	@Override
	public List<SDFDatatype> getProvidedSDFDatatypes() {
		return this.datatypes;
	}

	@Override
	public List<Class<?>> getProvidedClasses() {
		return this.classes;
	}

}
