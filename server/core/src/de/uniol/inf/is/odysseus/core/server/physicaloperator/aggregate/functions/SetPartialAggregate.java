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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

public class SetPartialAggregate<T> extends AbstractPartialAggregate<T> implements Iterable<T>{
	

	private static final long serialVersionUID = -1236414242464365484L;

	final Set<T> elems;
	
	public SetPartialAggregate(T elem) {
		elems = new HashSet<T>();
		addElem(elem);
	}
	
	public SetPartialAggregate(SetPartialAggregate<T> p) {
		this.elems = new HashSet<T>(p.elems);
	}

	public Set<T> getElems() {
		return elems;
	}
	
	public SetPartialAggregate<T> addElem(T elem) {
		this.elems.add(elem);
		return this;
	}

	public SetPartialAggregate<T> addAll(SetPartialAggregate<T> list){
		this.elems.addAll(list.elems);
		return this;
	}
	
	@Override
	public String toString() {
		return ""+elems;
	}
	
	@Override
	public ElementPartialAggregate<T> clone(){
		return new ElementPartialAggregate<T>(this);
	}

	@Override
	public Iterator<T> iterator() {
		return elems.iterator();
	}

	public int size() {
		return elems.size();
	}
	
}
