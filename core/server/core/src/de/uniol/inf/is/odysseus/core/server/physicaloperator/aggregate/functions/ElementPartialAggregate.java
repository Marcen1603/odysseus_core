/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;


public class ElementPartialAggregate<T> extends AbstractPartialAggregate<T> {

	private static final long serialVersionUID = -7568230052134641977L;
	
	T elem;
	final String datatype;
		
	public ElementPartialAggregate(T elem, String datatype) {
		setElem(elem);
		this.datatype = datatype;
	}
	
	public ElementPartialAggregate(IPartialAggregate<T> p) {
		setElem(((ElementPartialAggregate<T>)p).getElem());
		this.datatype = ((ElementPartialAggregate<T>)p).getDatatype();
	}

	public T getElem() {
		return elem;
	}
	
	public String getDatatype() {
		return datatype;
	}
		
	public void setElem(T elem) {
		this.elem = elem;
	}

	@Override
	public String toString() {
		return ""+elem;
	}
	
	@Override
	public ElementPartialAggregate<T> clone(){
		return new ElementPartialAggregate<T>(this);
	}
	
}
