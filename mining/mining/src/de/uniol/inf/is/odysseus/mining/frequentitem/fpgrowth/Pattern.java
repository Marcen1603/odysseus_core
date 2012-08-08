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
package de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Dennis Geesen
 *
 */
public class Pattern<M extends IMetaAttribute> {

	private ArrayList<Tuple<M>> pattern = new ArrayList<Tuple<M>>();
	private ArrayList<Integer> supports = new ArrayList<Integer>();
	private int support = Integer.MAX_VALUE;
	
	public Pattern() {
	
	}
	
	public Pattern(Pattern<M> pattern2) {
		this.pattern.addAll(pattern2.pattern);
		this.support = pattern2.support;
	}

	public void add(Tuple<M> t, int supportCount){
		this.pattern.add(t);
		this.supports.add(supportCount);
		if(supportCount <= this.support){
			this.support = supportCount;
		}
	}
	
	public boolean contains(Tuple<M> t){
		return this.pattern.contains(t);
	}
	
	public void reverse(){
		Collections.reverse(pattern);
	}
	
	public List<Tuple<M>> getPattern(){
		return this.pattern;
	}
	
	public int  getSupport(){
		return this.support;
	}

	/**
	 * @return
	 */
	public int length() {
		return this.pattern.size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Pattern<M> clone() {
		return new Pattern<M>(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {	
		return this.support+": "+this.pattern;
	}

	/**
	 * 
	 */
	public void removeFirst() {
		this.pattern.remove(0);		
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return this.pattern.isEmpty();
	}
}
