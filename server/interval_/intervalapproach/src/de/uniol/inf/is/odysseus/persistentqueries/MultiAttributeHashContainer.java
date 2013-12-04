/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.persistentqueries;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

@SuppressWarnings({"rawtypes"})
public class MultiAttributeHashContainer {

	/**
	 * The relational tuple to store
	 * in a hash map.
	 */
	private Tuple tuple;
	
	private Tuple restrictedTuple;
	
	/**
	 * if the hashCode has been calculated once
	 * do not calculate it anymore;
	 */
	int hashCode;
	
	public MultiAttributeHashContainer(Tuple tuple, Tuple restrictedTuple){
		this.tuple = tuple;
		this.restrictedTuple = restrictedTuple;
		this.hashCode = this.restrictedTuple.hashCode();
	}
	
	@Override
    public int hashCode(){
		return this.hashCode;
	}
	
	public boolean equals(MultiAttributeHashContainer other){
		return this.restrictedTuple.equals(other.restrictedTuple);
	}
	
	public Tuple getTuple(){
		return this.tuple;
	}
	
	public Tuple getRestrictedTuple(){
		return this.restrictedTuple;
	}
}
