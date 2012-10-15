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
package de.uniol.inf.is.odysseus.core.server.metadata;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;



public class Container<Type, MetaType extends IMetaAttribute> extends AbstractStreamObject<MetaType> {
	
	private static final long serialVersionUID = -581770058118444611L;
	public Type cargo;

	public Container(Type cargo) {
		this.cargo = cargo;
	}
	
	public Container(Type cargo, MetaType metadata){
		this.cargo = cargo;
		this.setMetadata(metadata);
	}

	public Container(Container<Type, MetaType> name) {
		super(name);
		this.cargo = name.cargo;
	}

	@Override
	public Container<Type, MetaType> clone() {
		return new Container<Type, MetaType>(this);
	}
	
	public Type getCargo(){
		return this.cargo;
	}
	
	public void setType(Type c){
		this.cargo = c;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Container: " + this.cargo.toString() + "   <|>   " + this.getMetadata().toString());
		return sb.toString();
	}
}
