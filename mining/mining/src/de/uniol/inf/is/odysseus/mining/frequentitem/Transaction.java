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
package de.uniol.inf.is.odysseus.mining.frequentitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * @author Dennis Geesen
 *
 */
public class Transaction<T> {
	
	private List<T> elements = new ArrayList<T>();
	private ITimeInterval timeinterval;
	
	public void setTimeInterval(ITimeInterval ti){
		this.timeinterval = ti;
	}
	
	public ITimeInterval getTimeInterval(){
		return this.timeinterval;
	}
	
	public void addElement(T tuple){
		this.elements.add(tuple);
	}
	public List<T> getElements(){
		return Collections.unmodifiableList(elements);
	}
	
	@Override
	public String toString() {
		String s = "Transaction ["+this.timeinterval+")";
		for(T t : elements){
			s=s+"\n\t"+t;
		}
		return s;
	}

}
